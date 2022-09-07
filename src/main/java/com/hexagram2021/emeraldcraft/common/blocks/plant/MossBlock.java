package com.hexagram2021.emeraldcraft.common.blocks.plant;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.block.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class MossBlock extends Block implements IGrowable {
	public MossBlock(AbstractBlock.Properties props) {
		super(props);
	}

	@Override
	public boolean isValidBonemealTarget(@Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState, boolean v) {
		return level.getBlockState(blockPos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(@Nonnull World level, @Nonnull Random random, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
		return true;
	}

	@Override
	public void performBonemeal(@Nonnull ServerWorld level, @Nonnull Random random, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
		BlockPos blockpos = blockPos.above();
		BlockState grass = Blocks.GRASS.defaultBlockState();

		for(int i = 0; i < 128; ++i) {
			BlockPos targetPos = blockpos;

			targetPos = targetPos.offset(random.nextInt(7) - 3, random.nextInt(5) - 2, random.nextInt(7) - 3);
			BlockState stoneBlockState = level.getBlockState(targetPos);
			if(stoneBlockState.is(BlockTags.BASE_STONE_OVERWORLD) || stoneBlockState.is(Blocks.COBBLESTONE) || stoneBlockState.is(ECBlocks.Decoration.COBBLED_DEEPSLATE.get())) {
				level.setBlock(targetPos, ECBlocks.Plant.MOSS_BLOCK.defaultBlockState(), 3);
				continue;
			}
			if(level.getBlockState(targetPos).isCollisionShapeFullBlock(level, targetPos)) {
				continue;
			}

			BlockState target = level.getBlockState(targetPos);
			if (target.is(grass.getBlock()) && random.nextInt(10) == 0) {
				((IGrowable)grass.getBlock()).performBonemeal(level, random, targetPos, target);
			}

			if (target.isAir() && level.getBlockState(targetPos).is(ECBlocks.Plant.MOSS_BLOCK.get())) {
				BlockState nextState;
				if (random.nextInt(4) == 0) {
					nextState = ECBlocks.Plant.MOSS_CARPET.defaultBlockState();
				} else {
					nextState = grass;
				}

				if (nextState.canSurvive(level, targetPos)) {
					level.setBlock(targetPos, nextState, 3);
				}
			}
		}
	}
}