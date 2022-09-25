package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.GlassKilnBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class GlassKilnBlock extends AbstractFurnaceBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.STONE)
			.sound(SoundType.STONE)
			.requiresCorrectToolForDrops()
			.strength(3.5F);

	public GlassKilnBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@NotNull
	public InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos,
								 @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.openContainer(level, blockPos, player);
			return InteractionResult.CONSUME;
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		return level.isClientSide ? null : createTickerHelper(type, ECBlockEntity.GLASS_KILN.get(), GlassKilnBlockEntity::serverTick);
	}

	@Override
	protected void openContainer(Level level, @NotNull BlockPos pos, @NotNull Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof GlassKilnBlockEntity) {
			player.openMenu((MenuProvider)blockentity);
		//	player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new GlassKilnBlockEntity(pos, state);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof GlassKilnBlockEntity glassKilnBlockEntity) {
				glassKilnBlockEntity.setCustomName(itemStack.getHoverName());
			}
		}
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean b) {
		if (!blockState.is(newBlockState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof GlassKilnBlockEntity glassKilnBlockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					Containers.dropContents(serverLevel, blockPos, glassKilnBlockEntity);
					glassKilnBlockEntity.getRecipesToAwardAndPopExperience(serverLevel, Vec3.atCenterOf(blockPos));
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}

			super.onRemove(blockState, level, blockPos, newBlockState, b);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos blockPos, Random random) {
		if (state.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = (double)blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = random.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
			double d6 = random.nextDouble() * 9.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
			level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
}
