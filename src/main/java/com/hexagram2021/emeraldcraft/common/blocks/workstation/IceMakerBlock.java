package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class IceMakerBlock extends AbstractFurnaceBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.METAL)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.strength(3.5F);

	public IceMakerBlock(Properties properties) {
		super(properties);
	}

	@Override @Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		return createIceMakerTicker(level, type, ECBlockEntity.ICE_MAKER.get());
	}

	@Override @NotNull
	public InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player,
								 @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		this.openContainer(level, blockPos, player);
		return InteractionResult.CONSUME;
	}

	@Override
	protected void openContainer(Level level, @NotNull BlockPos pos, @NotNull Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IceMakerBlockEntity) {
			player.openMenu((MenuProvider)blockentity);
		}
	}

	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos blockPos, @NotNull PathComputationType type) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new IceMakerBlockEntity(blockPos, blockState);
	}


	@Override
	public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
		if (state.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = random.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
			double d6 = random.nextDouble() * 9.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
			level.addParticle(ParticleTypes.WHITE_ASH, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createIceMakerTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends IceMakerBlockEntity> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(type, blockEntityType, IceMakerBlockEntity::serverTick);
	}
}
