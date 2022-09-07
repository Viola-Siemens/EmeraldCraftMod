package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class IceMakerBlock extends AbstractFurnaceBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.METAL)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.strength(3.5F);

	public IceMakerBlock(Properties properties) {
		super(properties);
	}

	@Override @Nonnull
	public ActionResultType use(@Nonnull BlockState blockState, @Nonnull World level, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity player,
								@Nonnull Hand interactionHand, @Nonnull BlockRayTraceResult blockHitResult) {
		if (level.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		this.openContainer(level, blockPos, player);
		return ActionResultType.CONSUME;
	}

	@Override
	protected void openContainer(World level, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
		TileEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof IceMakerBlockEntity) {
			player.openMenu((INamedContainerProvider)blockentity);
		}
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader getter, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(@Nonnull IBlockReader level) {
		return new IceMakerBlockEntity();
	}


	@Override
	public void animateTick(BlockState state, @Nonnull World level, @Nonnull BlockPos blockPos, @Nonnull Random random) {
		if (state.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = (double)blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, ECSounds.VILLAGER_WORK_ICER, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
}
