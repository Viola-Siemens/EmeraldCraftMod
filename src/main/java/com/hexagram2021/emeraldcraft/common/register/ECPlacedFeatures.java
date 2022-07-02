package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPlacedFeatures {
	public static PlacedFeature ORE_LAPIS_EXTRA = register("ore_lapis_extra",
			OreFeatures.ORE_LAPIS.placed(commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))));
	public static PlacedFeature ORE_EMERALD_EXTRA = register("ore_emerald_extra",
			OreFeatures.ORE_EMERALD.placed(commonOrePlacement(80, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))));
	public static PlacedFeature ORE_DEBRIS_EXTRA = register("ore_debris_extra",
			ECConfiguredFeatures.OreConfiguredFeatures.ORE_DEBRIS_EXTRA.placed(InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(16), VerticalAnchor.absolute(79)), BiomeFilter.biome()));
	public static PlacedFeature ORE_QUARTZ_EXTRA = register("ore_quartz_extra",
			ECConfiguredFeatures.OreConfiguredFeatures.ORE_QUARTZ_EXTRA.placed(commonOrePlacement(20, PlacementUtils.RANGE_10_10)));

	public static PlacedFeature ZOMBIE_VILLAGER_ROOM = register("zombie_villager_room",
			ECConfiguredFeatures.StructureConfiguredFeatures.ZOMBIE_VILLAGER_ROOM.placed(
					CountPlacement.of(8), InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()), BiomeFilter.biome()
			));

	public static final PlacedFeature FLOWER_PETUNIA_PLAINS = register("flower_petunia_plains",
			ECConfiguredFeatures.VegetationFeatures.FLOWER_PETUNIA_PLAINS.placed(
					NoiseThresholdCountPlacement.of(-0.8D, 18, 6),
					RarityFilter.onAverageOnceEvery(32),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP,
					BiomeFilter.biome()
			));

	public static final PlacedFeature TREES_GINKGO = register("trees_ginkgo",
			ECConfiguredFeatures.TreeConfiguredFeatures.TREES_GINKGO.placed(
					VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1))
			));

	public static final PlacedFeature VOLCANIC_CAVES_LAVA_POOL = register("volcanic_caves_lava_pool",
			ECConfiguredFeatures.SpecialFeatures.VOLCANIC_CAVES_LAVA_POOL.placed(
					CountPlacement.of(126), InSquarePlacement.spread(),
					PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
					EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
					RandomOffsetPlacement.vertical(ConstantInt.of(1)),
					BiomeFilter.biome()
			));

	public static final PlacedFeature XANADU_TREES = register("xanadu_trees",
			TreeFeatures.AZALEA_TREE.placed(
					PlacementUtils.countExtra(0, 0.05F, 1), InSquarePlacement.spread(),
					VegetationPlacements.TREE_THRESHOLD, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
					BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.AZALEA.defaultBlockState(), BlockPos.ZERO)),
					BiomeFilter.biome()
			));

	private static PlacedFeature register(String id, PlacedFeature placedFeature) {
		return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, id), placedFeature);
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier countModifier, PlacementModifier heightModifier) {
		return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
		return orePlacement(CountPlacement.of(count), heightModifier);
	}
}
