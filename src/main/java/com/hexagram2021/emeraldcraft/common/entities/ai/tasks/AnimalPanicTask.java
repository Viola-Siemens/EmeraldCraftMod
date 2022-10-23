package com.hexagram2021.emeraldcraft.common.entities.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class AnimalPanicTask extends Task<CreatureEntity> {
	private static final int PANIC_MIN_DURATION = 100;
	private static final int PANIC_MAX_DURATION = 120;
	private static final int PANIC_DISTANCE_HORIZONTAL = 5;
	private static final int PANIC_DISTANCE_VERTICAL = 4;
	
	private final float speedMultiplier;
	
	public AnimalPanicTask(float speedMultiplier) {
		super(ImmutableMap.of(ECMemoryModuleTypes.IS_PANICKING.get(), MemoryModuleStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryModuleStatus.VALUE_PRESENT), PANIC_MIN_DURATION, PANIC_MAX_DURATION);
		this.speedMultiplier = speedMultiplier;
	}
	
	@Override
	protected boolean canStillUse(@Nonnull ServerWorld level, @Nonnull CreatureEntity entity, long tick) {
		return true;
	}
	
	@Override
	protected void start(@Nonnull ServerWorld level, CreatureEntity entity, long tick) {
		entity.getBrain().setMemory(ECMemoryModuleTypes.IS_PANICKING.get(), true);
		entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
	}
	
	@Override
	protected void stop(@Nonnull ServerWorld level, CreatureEntity entity, long tick) {
		entity.getBrain().eraseMemory(ECMemoryModuleTypes.IS_PANICKING.get());
	}
	
	@Override
	protected void tick(@Nonnull ServerWorld level, CreatureEntity entity, long tick) {
		if(entity.getNavigation().isDone()) {
			Vector3d vec3 = this.getPanicPos(entity, level);
			if(vec3 != null) {
				entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedMultiplier, 0));
			}
		}
	}
	
	@Nullable
	private Vector3d getPanicPos(CreatureEntity entity, ServerWorld level) {
		if(entity.isOnFire()) {
			Optional<Vector3d> optional = this.lookForWater(level, entity).map(Vector3d::atBottomCenterOf);
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		
		return RandomPositionGenerator.getLandPos(entity, PANIC_DISTANCE_HORIZONTAL, PANIC_DISTANCE_VERTICAL);
	}
	
	private Optional<BlockPos> lookForWater(IBlockReader level, Entity entity) {
		BlockPos blockPos = entity.blockPosition();
		return !level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty() ?
				Optional.empty() : BlockPos.findClosestMatch(blockPos, PANIC_DISTANCE_HORIZONTAL, 1, pos -> level.getFluidState(pos).is(FluidTags.WATER));
	}
}
