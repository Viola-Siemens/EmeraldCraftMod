package com.hexagram2021.emeraldcraft.common.entities.ai.behaviors;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class GoToNearestDarkPosition<E extends LivingEntity & InventoryCarrier> extends Behavior<E> {
	private final int maxDist;
	private final float speedModifier;

	public GoToNearestDarkPosition(float speedModifier, boolean registered, int maxDist) {
		super(ImmutableMap.of(
				MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
				MemoryModuleType.WALK_TARGET, registered ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT,
				ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get(), MemoryStatus.REGISTERED,
				ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT
		), 120);
		this.maxDist = maxDist;
		this.speedModifier = speedModifier;
	}

	@Override
	protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
		if(entity.getMainHandItem().isEmpty() || !(entity.getMainHandItem().getItem() instanceof BlockItem)) {
			return false;
		}
		BlockPos blockPos = this.getClosestDarkLocation(level, entity);
		return blockPos != null && blockPos.closerThan(entity.blockPosition(), this.maxDist);
	}

	@Override
	protected boolean canStillUse(@NotNull ServerLevel level, @NotNull E entity, long tick) {
		return this.checkExtraStartConditions(level, entity);
	}

	@Override
	protected void start(@NotNull ServerLevel level, @NotNull E entity, long tick) {
		BehaviorUtils.setWalkAndLookTargetMemories(entity, Objects.requireNonNull(this.getClosestDarkLocation(level, entity)), this.speedModifier, 0);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void tick(@NotNull ServerLevel level, @NotNull E entity, long tick) {
		if(entity.getBrain().checkMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_PRESENT)) {
			return;
		}
		BlockPos pos = this.getClosestDarkLocation(level, entity);
		boolean isBlockItemInHand = entity.getMainHandItem().getItem() instanceof BlockItem;
		//boolean isFlint = entity.getMainHandItem().is(Items.FLINT_AND_STEEL) || entity.getMainHandItem().is(Items.FLINT);
		if(pos == null || entity.getMainHandItem().isEmpty() || !isBlockItemInHand) {
			entity.getBrain().setMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), 200);
			entity.getBrain().eraseMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get());
			return;
		}
		if(pos.closerToCenterThan(entity.position(), 1.0D)) {
			if(isBlockItemInHand) {
				Block torch = ((BlockItem) (entity.getMainHandItem().getItem())).getBlock();
				if (level.getBlockState(pos).isAir() && torch.canSurvive(torch.defaultBlockState(), level, pos) && !entity.getInventory().getItem(0).isEmpty()) {
					level.setBlock(pos, torch.defaultBlockState(), Block.UPDATE_ALL);
					if (entity.getInventory().getItem(0).sameItem(entity.getMainHandItem())) {
						entity.getInventory().getItem(0).shrink(1);
					} else {
						entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(entity.getInventory().getItem(0).getItem()));
						entity.getInventory().getItem(0).shrink(1);
					}
					entity.getBrain().setMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), 200);
					entity.getBrain().eraseMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get());
				}
			}
		}
	}

	@Override
	protected void stop(@NotNull ServerLevel level, @NotNull E entity, long tick) {
		if(entity.getBrain().checkMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT)) {
			entity.getBrain().setMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), 400);
		}
		entity.getBrain().eraseMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get());
		entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
		entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
	}

	@Nullable
	private BlockPos getClosestDarkLocation(@NotNull ServerLevel level, @NotNull E entity) {
		return entity.getBrain().getMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get()).orElseGet(() -> {
			BlockPos current = entity.blockPosition();
			Block torch = ((BlockItem)(entity.getMainHandItem().getItem())).getBlock();
			BlockPos pos = VerticalSearch(current, torch, level);
			if(pos != null) {
				entity.getBrain().setMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get(), pos);
				return pos;
			}
			for(int d = 1; d <= 20; ++d) {
				for(int x = -d; x <= d; ++x) {
					pos = VerticalSearch(current.mutable().move(x, 0, -d), torch, level);
					if(pos == null) {
						pos = VerticalSearch(current.mutable().move(x, 0, d), torch, level);
					}
					if(pos != null) {
						entity.getBrain().setMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get(), pos);
						return pos;
					}
				}
				for(int z = -d + 1; z < d; ++z) {
					pos = VerticalSearch(current.mutable().move(-d, 0, z), torch, level);
					if(pos == null) {
						pos = VerticalSearch(current.mutable().move(d, 0, z), torch, level);
					}
					if(pos != null) {
						entity.getBrain().setMemory(ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get(), pos);
						return pos;
					}
				}
			}
			entity.getBrain().setMemory(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(), 400);
			return null;
		});
	}

	@Nullable
	private BlockPos VerticalSearch(@NotNull BlockPos current, @NotNull Block torch, @NotNull ServerLevel level) {
		for(int h = 0; h <= 3; ++h) {
			BlockPos pos = current.below(h);
			if(level.getBlockState(pos).isAir()) {
				if(level.getBrightness(LightLayer.BLOCK, pos) < 3) {
					if(torch.canSurvive(torch.defaultBlockState(), level, pos)) {
						return pos;
					}
				}
			} else {
				break;
			}
		}
		return null;
	}
}
