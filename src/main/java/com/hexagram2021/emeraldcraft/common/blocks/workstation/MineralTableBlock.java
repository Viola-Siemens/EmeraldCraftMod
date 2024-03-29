package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.MineralTableBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class MineralTableBlock extends AbstractFurnaceBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of()
			.requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.STONE);

	public MineralTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override @Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createMineralTableTicker(level, type, ECBlockEntity.MINERAL_TABLE.get());
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		this.openContainer(level, blockPos, player);
		return InteractionResult.CONSUME;
	}

	@Override
	protected void openContainer(Level level, BlockPos pos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof MineralTableBlockEntity) {
			player.openMenu((MenuProvider)blockentity);
		}
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new MineralTableBlockEntity(blockPos, blockState);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof MineralTableBlockEntity mineralTableBlockEntity) {
				mineralTableBlockEntity.setCustomName(itemStack.getHoverName());
			}
		}
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean b) {
		if (!blockState.is(newBlockState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof MineralTableBlockEntity mineralTableBlockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					Containers.dropContents(serverLevel, blockPos, mineralTableBlockEntity);
					mineralTableBlockEntity.getRecipesToAwardAndPopExperience(serverLevel, Vec3.atCenterOf(blockPos));
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}

			super.onRemove(blockState, level, blockPos, newBlockState, b);
		}
	}


	@Override
	public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource random) {
		if (state.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = random.nextDouble() * 0.6D - 0.3D;
			double d5 = axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
			double d6 = random.nextDouble() * 9.0D / 16.0D;
			double d7 = axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
			level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createMineralTableTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends MineralTableBlockEntity> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(type, blockEntityType, MineralTableBlockEntity::serverTick);
	}
}
