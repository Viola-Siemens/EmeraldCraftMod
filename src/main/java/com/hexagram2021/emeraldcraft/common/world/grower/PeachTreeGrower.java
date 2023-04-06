package com.hexagram2021.emeraldcraft.common.world.grower;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatureKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

public class PeachTreeGrower extends AbstractTreeGrower {
	@Override
	protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean bee) {
		return bee ? ECConfiguredFeatureKeys.TreeConfiguredFeatures.PEACH_BEES : ECConfiguredFeatureKeys.TreeConfiguredFeatures.PEACH;
	}
}
