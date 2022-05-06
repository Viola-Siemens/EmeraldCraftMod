package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredFeatures {
	public static ConfiguredFeature<?, ?> ORE_LAPIS_EXTRA = register("ore_gold_extra",
			Feature.ORE.configured(new OreConfiguration(Features.ORE_LAPIS_TARGET_LIST, 7))
					.rangeUniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(79)).squared().count(20));
	public static ConfiguredFeature<?, ?> ORE_EMERALD_EXTRA = register("ore_emerald_extra",
			Feature.ORE.configured(new OreConfiguration(Features.ORE_EMERALD_TARGET_LIST, 5))
					.rangeUniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(79)).squared().count(20));
	public static final ConfiguredFeature<?, ?> ORE_DEBRIS_EXTRA = register("ore_debris_extra",
			Feature.SCATTERED_ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 4, 0.8F))
					.rangeTriangle(VerticalAnchor.absolute(16), VerticalAnchor.absolute(79)).squared());
	public static final ConfiguredFeature<?, ?> ORE_QUARTZ_EXTRA = register("ore_quartz_extra",
			Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NETHERRACK,
					Blocks.NETHER_QUARTZ_ORE.defaultBlockState(), 16))
					.range(Features.Decorators.RANGE_10_10).squared().count(20));
	public static ConfiguredFeature<?, ?> ZOMBIE_VILLAGER_ROOM = register("zombie_villager_room",
			ECFeatures.ZOMBIE_VILLAGER_ROOM.configured(FeatureConfiguration.NONE)
					.range(Features.Decorators.FULL_RANGE).squared().count(8));

	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), configuredFeature);
	}
}
