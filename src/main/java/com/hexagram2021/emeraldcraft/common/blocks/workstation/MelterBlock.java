package com.hexagram2021.emeraldcraft.common.blocks.workstation;


import com.hexagram2021.emeraldcraft.common.blocks.entity.MelterBlockEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class MelterBlock extends AbstractFurnaceBlock {
	public static final Supplier<Properties> PROPERTIES = () -> Properties.of(Material.METAL)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.strength(3.5F);

	public MelterBlock(Properties properties) {
		super(properties);
	}

	@Override @Nonnull
	public ActionResultType use(@Nonnull BlockState blockState, World level, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity player,
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
		if (blockentity instanceof MelterBlockEntity) {
			player.openMenu((INamedContainerProvider) blockentity);
		}
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(@Nonnull IBlockReader level) {
		return new MelterBlockEntity();
	}

	@Override
	public void setPlacedBy(@Nonnull World level, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState, @Nonnull LivingEntity livingEntity, ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			TileEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof MelterBlockEntity) {
				((MelterBlockEntity)blockentity).setCustomName(itemStack.getHoverName());
			}
		}
	}

	@Override
	public void onRemove(BlockState blockState, @Nonnull World level, @Nonnull BlockPos blockPos, BlockState newBlockState, boolean b) {
		if (!blockState.is(newBlockState.getBlock())) {
			TileEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof MelterBlockEntity) {
				if (level instanceof ServerWorld) {
					InventoryHelper.dropContents(level, blockPos, (MelterBlockEntity)blockentity);
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}

			super.onRemove(blockState, level, blockPos, newBlockState, b);
		}
	}


	@Override
	public void animateTick(BlockState state, @Nonnull World level, @Nonnull BlockPos blockPos, @Nonnull Random random) {
		if (state.getValue(LIT)) {
			double d0 = (double)blockPos.getX() + 0.5D;
			double d1 = (double)blockPos.getY();
			double d2 = (double)blockPos.getZ() + 0.5D;
			if (random.nextDouble() < 0.1D) {
				level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
