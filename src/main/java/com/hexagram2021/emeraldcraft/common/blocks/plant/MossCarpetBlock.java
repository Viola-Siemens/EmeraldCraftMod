package com.hexagram2021.emeraldcraft.common.blocks.plant;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;

public class MossCarpetBlock extends Block {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

	public MossCarpetBlock(AbstractBlock.Properties props) {
		super(props);
	}

	@Override @Nonnull
	public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext context) {
		return SHAPE;
	}

	@Override @Nonnull
	public BlockState updateShape(BlockState blockState, @Nonnull Direction direction, @Nonnull BlockState neighbor, @Nonnull IWorld level,
								  @Nonnull BlockPos blockPos, @Nonnull BlockPos neighborPos) {
		return !blockState.canSurvive(level, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, neighbor, level, blockPos, neighborPos);
	}

	@Override
	public boolean canSurvive(@Nonnull BlockState blockState, IWorldReader level, BlockPos blockPos) {
		return !level.isEmptyBlock(blockPos.below());
	}
}
