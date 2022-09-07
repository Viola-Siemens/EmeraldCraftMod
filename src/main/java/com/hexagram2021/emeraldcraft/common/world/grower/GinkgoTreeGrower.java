package com.hexagram2021.emeraldcraft.common.world.grower;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;

public class GinkgoTreeGrower extends Tree {
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random random, boolean bee) {
		switch (random.nextInt(3)) {
			case 0:
				return ECConfiguredFeatures.TreeConfiguredFeatures.GINKGO_1;
			case 1:
				return ECConfiguredFeatures.TreeConfiguredFeatures.GINKGO_2;
			case 2:
				return ECConfiguredFeatures.TreeConfiguredFeatures.GINKGO_3;
			default:
				throw new AssertionError();
		}
	}
}
