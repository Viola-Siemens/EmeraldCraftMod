package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.placement_modifiers.AboveHeightmapFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPlacedFeatures {
	public static Holder<PlacedFeature> ORE_LAPIS_EXTRA = register(
			"ore_lapis_extra", OreFeatures.ORE_LAPIS,
			commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))
	);
	public static Holder<PlacedFeature> ORE_EMERALD_EXTRA = register(
			"ore_emerald_extra", OreFeatures.ORE_EMERALD,
			commonOrePlacement(80, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))
	);
	public static Holder<PlacedFeature> ORE_DEBRIS_EXTRA = register(
			"ore_debris_extra", ECConfiguredFeatures.OreConfiguredFeatures.ORE_DEBRIS_EXTRA, List.of(
					InSquarePlacement.spread(),
					HeightRangePlacement.triangle(VerticalAnchor.absolute(16), VerticalAnchor.absolute(79)),
					BiomeFilter.biome()
			));
	public static Holder<PlacedFeature> ORE_QUARTZ_EXTRA = register(
			"ore_quartz_extra", ECConfiguredFeatures.OreConfiguredFeatures.ORE_QUARTZ_EXTRA,
			commonOrePlacement(20, PlacementUtils.RANGE_10_10)
	);

	public static Holder<PlacedFeature> ZOMBIE_VILLAGER_ROOM = register(
			"zombie_villager_room", ECConfiguredFeatures.StructureConfiguredFeatures.ZOMBIE_VILLAGER_ROOM, List.of(
					CountPlacement.of(8), InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()),
					BiomeFilter.biome()
			));

	public static final Holder<PlacedFeature> FLOWER_PETUNIA_PLAINS = register(
			"flower_petunia_plains", ECConfiguredFeatures.VegetationFeatures.FLOWER_PETUNIA_PLAINS, List.of(
					NoiseThresholdCountPlacement.of(-0.8D, 18, 6),
					RarityFilter.onAverageOnceEvery(32),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP,
					BiomeFilter.biome()
			));

	public static final Holder<PlacedFeature> TREES_GINKGO = register(
			"trees_ginkgo", ECConfiguredFeatures.TreeConfiguredFeatures.TREES_GINKGO,
			VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1))
	);

	public static final Holder<PlacedFeature> VOLCANIC_CAVES_LAVA_POOL = register(
			"volcanic_caves_lava_pool", ECConfiguredFeatures.SpecialFeatures.VOLCANIC_CAVES_LAVA_POOL, List.of(
					CountPlacement.of(126), InSquarePlacement.spread(),
					PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
					EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
					RandomOffsetPlacement.vertical(ConstantInt.of(1)),
					BiomeFilter.biome()
			));

	public static final Holder<PlacedFeature> XANADU_DELTA = register(
			"xanadu_delta", ECConfiguredFeatures.SpecialFeatures.XANADU_DELTA, List.of(
					CountOnEveryLayerPlacement.of(3), BiomeFilter.biome(),
					new AboveHeightmapFilter(Heightmap.Types.OCEAN_FLOOR_WG)
			)
	);
	public static final Holder<PlacedFeature> KARST_DELTA = register(
			"karst_delta", ECConfiguredFeatures.SpecialFeatures.KARST_DELTA, List.of(
					CountOnEveryLayerPlacement.of(1), BiomeFilter.biome(),
					new AboveHeightmapFilter(Heightmap.Types.OCEAN_FLOOR_WG)
			)
	);

	public static final Holder<PlacedFeature> XANADU_TREES = register(
			"xanadu_trees", TreeFeatures.AZALEA_TREE, List.of(
					PlacementUtils.countExtra(0, 0.05F, 1), InSquarePlacement.spread(),
					VegetationPlacements.TREE_THRESHOLD, PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
					BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.AZALEA.defaultBlockState(), BlockPos.ZERO)),
					BiomeFilter.biome()
			));

	private static Holder<PlacedFeature> register(String id, Holder<? extends ConfiguredFeature<?, ?>> cf, List<PlacementModifier> modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, id), new PlacedFeature(Holder.hackyErase(cf), List.copyOf(modifiers)));
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier countModifier, PlacementModifier heightModifier) {
		return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
		return orePlacement(CountPlacement.of(count), heightModifier);
	}
}
