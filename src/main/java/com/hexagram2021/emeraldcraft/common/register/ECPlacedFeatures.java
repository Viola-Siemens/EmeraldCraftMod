package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.placement_modifiers.AboveHeightmapFilter;
import net.minecraft.core.Direction;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("deprecation")
public class ECPlacedFeatures {
	public static final PlacedFeature ORE_LAPIS_EXTRA = register(
			"ore_lapis_extra", OreFeatures.ORE_LAPIS,
			commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))
	);
	public static final PlacedFeature ORE_EMERALD_EXTRA = register(
			"ore_emerald_extra", OreFeatures.ORE_EMERALD,
			commonOrePlacement(80, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))
	);
	public static final PlacedFeature ORE_DEBRIS_EXTRA = register(
			"ore_debris_extra", ECConfiguredFeatures.OreConfiguredFeatures.ORE_DEBRIS_EXTRA, List.of(
					InSquarePlacement.spread(),
					HeightRangePlacement.triangle(VerticalAnchor.absolute(16), VerticalAnchor.absolute(79)),
					BiomeFilter.biome()
			));
	public static final PlacedFeature ORE_QUARTZ_EXTRA = register(
			"ore_quartz_extra", ECConfiguredFeatures.OreConfiguredFeatures.ORE_QUARTZ_EXTRA,
			commonOrePlacement(20, PlacementUtils.RANGE_10_10)
	);

	public static final PlacedFeature ZOMBIE_VILLAGER_ROOM = register(
			"zombie_villager_room", ECConfiguredFeatures.StructureConfiguredFeatures.ZOMBIE_VILLAGER_ROOM, List.of(
					CountPlacement.of(8), InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.top()),
					BiomeFilter.biome()
			));

	public static final PlacedFeature FLOWER_PETUNIA_PLAINS = register(
			"flower_petunia_plains", ECConfiguredFeatures.VegetationFeatures.FLOWER_PETUNIA_PLAINS, List.of(
					NoiseThresholdCountPlacement.of(-0.8D, 18, 6),
					RarityFilter.onAverageOnceEvery(32),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP,
					BiomeFilter.biome()
			));

	public static final PlacedFeature FLOWER_HIGAN_BANA = register(
			"flower_higan_bana", ECConfiguredFeatures.VegetationFeatures.FLOWER_HIGAN_BANA, List.of(
					CountPlacement.of(8),
					InSquarePlacement.spread(),
					PlacementUtils.FULL_RANGE,
					BiomeFilter.biome()
			)
	);

	public static final PlacedFeature TREES_GINKGO = register(
			"trees_ginkgo", ECConfiguredFeatures.TreeConfiguredFeatures.TREES_GINKGO,
			VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), ECBlocks.Plant.GINKGO_SAPLING.get())
	);

	public static final PlacedFeature TREES_PALM = register(
			"trees_palm", ECConfiguredFeatures.TreeConfiguredFeatures.TREES_PALM,
			VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1F, 1), ECBlocks.Plant.PALM_SAPLING.get())
	);

	public static final PlacedFeature TREES_PEACH = register(
			"trees_peach", ECConfiguredFeatures.TreeConfiguredFeatures.TREES_PEACH,
			VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.2F, 2), ECBlocks.Plant.PEACH_SAPLING.get())
	);

	public static final PlacedFeature PURPURACEUS_FUNGI = register(
			"purpuraceus_fungi", ECConfiguredFeatures.TreeConfiguredFeatures.PURPURACEUS_FUNGUS,
			List.of(CountOnEveryLayerPlacement.of(3), BiomeFilter.biome())
	);

	public static final PlacedFeature PURPURACEUS_SWAMP_VEGETATION = register(
			"purpuraceus_swamp_vegetation", ECConfiguredFeatures.VegetationFeatures.PURPURACEUS_SWAMP_VEGETATION,
			List.of(CountOnEveryLayerPlacement.of(4), BiomeFilter.biome())
	);


	public static final PlacedFeature VOLCANIC_CAVES_LAVA_POOL = register(
			"volcanic_caves_lava_pool", ECConfiguredFeatures.SpecialFeatures.VOLCANIC_CAVES_LAVA_POOL, List.of(
					CountPlacement.of(126), InSquarePlacement.spread(),
					PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
					EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
					RandomOffsetPlacement.vertical(ConstantInt.of(1)),
					BiomeFilter.biome()
			)
	);

	public static final PlacedFeature XANADU_DELTA = register(
			"xanadu_delta", ECConfiguredFeatures.SpecialFeatures.XANADU_DELTA, List.of(
					CountOnEveryLayerPlacement.of(3), BiomeFilter.biome(),
					new AboveHeightmapFilter(Heightmap.Types.OCEAN_FLOOR_WG)
			)
	);
	public static final PlacedFeature KARST_DELTA = register(
			"karst_delta", ECConfiguredFeatures.SpecialFeatures.KARST_DELTA, List.of(
					CountOnEveryLayerPlacement.of(1), BiomeFilter.biome(),
					new AboveHeightmapFilter(Heightmap.Types.OCEAN_FLOOR_WG)
			)
	);
	public static final PlacedFeature PURPURACEUS_SWAMP_DELTA = register(
			"purpuraceus_swamp_delta", ECConfiguredFeatures.SpecialFeatures.PURPURACEUS_SWAMP_DELTA, List.of(
					CountOnEveryLayerPlacement.of(3), BiomeFilter.biome()
			)
	);
	public static final PlacedFeature PURPURACEUS_SWAMP_LAVA_DELTA = register(
			"purpuraceus_swamp_lava_delta", ECConfiguredFeatures.SpecialFeatures.PURPURACEUS_SWAMP_LAVA_DELTA, List.of(
					CountOnEveryLayerPlacement.of(1), BiomeFilter.biome()
			)
	);


	private static PlacedFeature register(String id, ConfiguredFeature<?, ?> cf, List<PlacementModifier> modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(MODID, id), cf.placed(List.copyOf(modifiers)));
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier countModifier, PlacementModifier heightModifier) {
		return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
		return orePlacement(CountPlacement.of(count), heightModifier);
	}
}
