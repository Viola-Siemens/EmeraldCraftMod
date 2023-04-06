package com.hexagram2021.emeraldcraft.common.world.grower;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatureKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class PalmTreeGrower extends AbstractTreeGrower {
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bee) {
		return switch (random.nextInt(3)) {
			case 0, 2 -> ECConfiguredFeatureKeys.TreeConfiguredFeatures.PALM;
			case 1 -> ECConfiguredFeatureKeys.TreeConfiguredFeatures.PALM_TALL;
			default -> throw new AssertionError();
		};
	}
}
