package com.hexagram2021.emeraldcraft.common.world.grower;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class PalmTreeGrower extends AbstractTreeGrower {
	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bee) {
		return switch (random.nextInt(3)) {
			case 0 -> ECConfiguredFeatures.TreeConfiguredFeatures.PALM_1;
			case 1 -> ECConfiguredFeatures.TreeConfiguredFeatures.PALM_2;
			case 2 -> ECConfiguredFeatures.TreeConfiguredFeatures.PALM_3;
			default -> throw new AssertionError();
		};
	}
}
