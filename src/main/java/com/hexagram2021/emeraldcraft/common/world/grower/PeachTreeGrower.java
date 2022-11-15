package com.hexagram2021.emeraldcraft.common.world.grower;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatures;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Random;

public class PeachTreeGrower extends AbstractTreeGrower {
	@Override
	protected ConfiguredFeature<?, ?> getConfiguredFeature(Random random, boolean bee) {
		return switch (random.nextInt(3)) {
			case 0 -> ECConfiguredFeatures.TreeConfiguredFeatures.PEACH_1;
			case 1 -> ECConfiguredFeatures.TreeConfiguredFeatures.PEACH_2;
			case 2 -> ECConfiguredFeatures.TreeConfiguredFeatures.PEACH_3;
			default -> throw new AssertionError();
		};
	}
}
