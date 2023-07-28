package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CabbageBlock extends CropBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 3.0D, 12.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 5.0D, 12.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 9.0D, 13.0D)};

	public CabbageBlock(Properties props) {
		super(props);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return ECItems.CABBAGE_SEED;
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return SHAPE_BY_AGE[blockState.getValue(this.getAgeProperty())];
	}
}
