package com.hexagram2021.emeraldcraft.common.world.features;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Predicate;

public class RawBerylFeature extends Feature<NoneFeatureConfiguration> {
	private static final BlockState RAW_BERYL_BLOCK = ECBlocks.Decoration.RAW_BERYL_BLOCK.defaultBlockState();

	public RawBerylFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
		BlockPos origin = context.origin();
		RandomSource random = context.random();
		WorldGenLevel worldgenlevel = context.level();
		int height = random.nextInt(3, 9);
		int radius = random.nextInt(2, 5);
		boolean flag = false;

		for(int x = -5; x <= 5; ++x) {
			for(int y = -3; y <= 3; ++y) {
				for(int z = -5; z <= 5; ++z) {
					BlockPos current = origin.offset(x, y, z);
					if(worldgenlevel.getBlockState(current).is(ECBlocks.Decoration.CUT_JADEITE_SANDSTONE.get())) {
						if(height > 6) {
							origin = current.below();
						} else {
							origin = current;
						}
						flag = true;
					}
				}
			}
		}
		if(!flag) {
			return false;
		}

		for(int y = 0; y < height - 1; ++y) {
			int r = Mth.floor((double)(radius * (height - y)) / height);
			for(int x = -r; x <= r; ++x) {
				for(int z = -r; z <= r; ++z) {
					double v = (double)(x * x + z * z) / (r * r + 0.1D);
					if(v * v < random.nextDouble()) {
						this.safeSetBlock(worldgenlevel, origin.offset(x, y, z), RAW_BERYL_BLOCK, predicate);
					}
				}
			}
		}
		return true;
	}
}