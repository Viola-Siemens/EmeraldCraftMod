package com.hexagram2021.emeraldcraft.common.world.features;

import com.hexagram2021.emeraldcraft.common.world.features.configuration.VineGrowthConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class VineGrowthFeature extends Feature<VineGrowthConfiguration> {
	public VineGrowthFeature(Codec<VineGrowthConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(@NotNull FeaturePlaceContext<VineGrowthConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		Random random = context.random();
		VineGrowthConfiguration config = context.config();
		if (isNotAirOrWater(level.getBlockState(origin))) {
			return false;
		}

		List<Direction> list = config.getShuffledDirections(random);
		if (placeGrowthIfPossible(level, origin, level.getBlockState(origin), config, list)) {
			return true;
		}
		BlockPos.MutableBlockPos mutable = origin.mutable();

		for(Direction direction : list) {
			mutable.set(origin);
			List<Direction> list1 = config.getShuffledDirectionsExcept(random, direction.getOpposite());

			for(int i = 0; i < config.searchRange; ++i) {
				mutable.setWithOffset(origin, direction);
				BlockState blockstate = level.getBlockState(mutable);
				if (isNotAirOrWater(blockstate) && !blockstate.is(config.placeBlock)) {
					break;
				}

				if (placeGrowthIfPossible(level, mutable, blockstate, config, list1)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean placeGrowthIfPossible(WorldGenLevel level, BlockPos pos, BlockState blockState,
												VineGrowthConfiguration configuration, List<Direction> directions) {
		BlockPos.MutableBlockPos mutable = pos.mutable();

		for(Direction direction : directions) {
			BlockState blockstate = level.getBlockState(mutable.setWithOffset(pos, direction));
			if (blockstate.is(configuration.canBePlacedOn)) {
				BlockState placement = getStateForPlacement(configuration.placeBlock, blockState, direction);
				if (placement == null) {
					return false;
				}

				level.setBlock(pos, placement, Block.UPDATE_ALL);
				level.getChunk(pos).markPosForPostprocessing(pos);

				return true;
			}
		}

		return false;
	}

	private static boolean isNotAirOrWater(BlockState blockState) {
		return !blockState.isAir() && !blockState.is(Blocks.WATER);
	}

	private static BlockState getStateForPlacement(Block block, BlockState blockState, Direction direction) {
		BlockState ret = block.defaultBlockState().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), Boolean.TRUE);
		if(blockState.is(Blocks.WATER)) {
			if(block.defaultBlockState().hasProperty(BlockStateProperties.WATERLOGGED)) {
				ret = ret.setValue(BlockStateProperties.WATERLOGGED, true);
			} else {
				ret = null;
			}
		}
		return ret;
	}
}
