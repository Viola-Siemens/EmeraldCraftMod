package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.DarkOakFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.OptionalInt;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECConfiguredFeatures {
	public static final class OreConfiguredFeatures {
		public static final ConfiguredFeature<?, ?> ORE_DEBRIS_EXTRA = register2(
				"ore_debris_extra", Feature.NO_SURFACE_ORE, new OreFeatureConfig(
						OreFeatureConfig.FillerBlockType.NETHER_ORE_REPLACEABLES,
						Blocks.ANCIENT_DEBRIS.defaultBlockState(),
						4
				),
				Placement.RANGE.configured(new TopSolidRangeConfig(16, 32, 80))
		);
		public static final ConfiguredFeature<?, ?> ORE_QUARTZ_EXTRA = register2(
				"ore_quartz_extra", Feature.ORE, new OreFeatureConfig(
						OreFeatureConfig.FillerBlockType.NETHERRACK,
						Blocks.NETHER_QUARTZ_ORE.defaultBlockState(),
						16
				),
				Placement.DEPTH_AVERAGE.configured(new DepthAverageConfig(32, 32))
		);
		public static ConfiguredFeature<?, ?> ORE_LAPIS_EXTRA = register2(
				"ore_lapis_extra", Feature.ORE, new OreFeatureConfig(
						OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						Blocks.LAPIS_ORE.defaultBlockState(),
						7
				),
				Placement.RANGE.configured(new TopSolidRangeConfig(32, 32, 80)), 20
		);
		public static ConfiguredFeature<?, ?> ORE_EMERALD_EXTRA = register2("ore_emerald_extra",
				Feature.ORE, new OreFeatureConfig(
						OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						Blocks.LAPIS_ORE.defaultBlockState(),
						5
				),
				Placement.RANGE.configured(new TopSolidRangeConfig(32, 32, 80)), 20
		);

		public static final ConfiguredFeature<?, ?> ORE_DEEPSLATE = register2("ore_deepslate",
				Feature.ORE, new OreFeatureConfig(
						OreFeatureConfig.FillerBlockType.NATURAL_STONE,
						ECBlocks.Decoration.DEEPSLATE.defaultBlockState(),
						64
				),
				Placement.RANGE.configured(new TopSolidRangeConfig(0, 8, 16)), 2
		);


		private OreConfiguredFeatures() {}

		private static void init() {}
	}

	public static final class StructureConfiguredFeatures {
		public static ConfiguredFeature<?, ?> ZOMBIE_VILLAGER_ROOM = register2(
				"zombie_villager_room", ECFeatures.ZOMBIE_VILLAGER_ROOM, IFeatureConfig.NONE,
				256, 8
		);

		private StructureConfiguredFeatures() {}

		private static void init() {}
	}

	public static final class TreeConfiguredFeatures {
		public static final BlockState GINKGO_LOG = ECBlocks.Plant.GINKGO_LOG.defaultBlockState();
		public static final BlockState GINKGO_LEAVES = ECBlocks.Plant.GINKGO_LEAVES.defaultBlockState();
		public static final BlockState PALM_LOG = ECBlocks.Plant.PALM_LOG.defaultBlockState();
		public static final BlockState PALM_LEAVES = ECBlocks.Plant.PALM_LEAVES.defaultBlockState();
		public static final BlockState PEACH_LOG = ECBlocks.Plant.PEACH_LOG.defaultBlockState();
		public static final BlockState PEACH_LEAVES = ECBlocks.Plant.PEACH_LEAVES.defaultBlockState();

		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> GINKGO_1 = register(
				"ginkgo_1", Feature.TREE, createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 3, 2, 1, 2
				).build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> GINKGO_2 = register(
				"ginkgo_2", Feature.TREE, createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 3, 2, 2, 3
				).build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> GINKGO_3 = register(
				"ginkgo_3", Feature.TREE, createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 4, 2, 6, 3
				).build()
		);

		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PALM_1 = register(
				"palm_1", Feature.TREE, createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 4, 2, 1, 2
				).ignoreVines().build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PALM_2 = register(
				"palm_2", Feature.TREE, createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 5, 2, 2, 3
				).ignoreVines().build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PALM_3 = register(
				"palm_3", Feature.TREE, createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 6, 2, 6, 3
				).ignoreVines().build()
		);

		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PEACH_1 = register(
				"peach_1", Feature.TREE, createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 4, 2, 1, 2
				).ignoreVines().build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PEACH_2 = register(
				"peach_2", Feature.TREE, createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 5, 2, 2, 3
				).ignoreVines().build()
		);
		public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> PEACH_3 = register(
				"peach_3", Feature.TREE, createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 6, 2, 1, 3
				).ignoreVines().build()
		);

		public static final ConfiguredFeature<?, ?> TREES_GINKGO = register(
				"trees_ginkgo", Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(
						ImmutableList.of(
								TreeConfiguredFeatures.GINKGO_2.weighted(0.3F),
								TreeConfiguredFeatures.GINKGO_3.weighted(0.2F)
						),
						TreeConfiguredFeatures.GINKGO_1
				),
				Features.Placements.HEIGHTMAP_SQUARE,
				Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(10, 0.1F, 1)),
				3
		);

		public static final ConfiguredFeature<?, ?> TREES_PALM = register(
				"trees_palm", Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(
						ImmutableList.of(
								TreeConfiguredFeatures.PALM_2.weighted(0.3F),
								TreeConfiguredFeatures.PALM_3.weighted(0.2F)
						),
						TreeConfiguredFeatures.PALM_1
				),
				Features.Placements.HEIGHTMAP_SQUARE,
				Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(1, 0.1F, 1)),
				1
		);

		public static final ConfiguredFeature<?, ?> TREES_PEACH = register(
				"trees_peach", Feature.RANDOM_SELECTOR, new MultipleRandomFeatureConfig(
						ImmutableList.of(
								TreeConfiguredFeatures.PEACH_2.weighted(0.3F),
								TreeConfiguredFeatures.PEACH_3.weighted(0.2F)
						),
						TreeConfiguredFeatures.PEACH_1
				),
				Features.Placements.HEIGHTMAP_SQUARE,
				Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(3, 0.2F, 2)),
				1
		);

		private static BaseTreeFeatureConfig.Builder createStraightBlobGinkgoTree(BlockState logBlock, BlockState leavesBlock,
																				  int baseHeight, int heightRandA, int heightRandB,
																				  int radius) {
			return new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(logBlock),
					new SimpleBlockStateProvider(leavesBlock),
					new BlobFoliagePlacer(FeatureSpread.fixed(radius), FeatureSpread.fixed(0), 4),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					new TwoLayerFeature(1, 0, 1)
			);
		}

		private static BaseTreeFeatureConfig.Builder createStraightBlobPalmTree(BlockState logBlock, BlockState leavesBlock,
																				int baseHeight, int heightRandA, int heightRandB,
																				int radius) {
			return new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(logBlock),
					new SimpleBlockStateProvider(leavesBlock),
					new AcaciaFoliagePlacer(FeatureSpread.fixed(radius), FeatureSpread.fixed(0)),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					new TwoLayerFeature(1, 0, 2)
			);
		}

		private static BaseTreeFeatureConfig.Builder createStraightBlobPeachTree(BlockState logBlock, BlockState leavesBlock,
																				int baseHeight, int heightRandA, int heightRandB,
																				int radius) {
			return new BaseTreeFeatureConfig.Builder(
					new SimpleBlockStateProvider(logBlock),
					new SimpleBlockStateProvider(leavesBlock),
					new DarkOakFoliagePlacer(FeatureSpread.fixed(radius), FeatureSpread.fixed(0)),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					new ThreeLayerFeature(1, 1, 0, 1, 2, OptionalInt.empty())
			).heightmap(Heightmap.Type.MOTION_BLOCKING).ignoreVines();
		}

		private TreeConfiguredFeatures() {}

		private static void init() {}
	}

	public static final class VegetationFeatures {
		public static final BlockState CYAN_PETUNIA = ECBlocks.Plant.CYAN_PETUNIA.defaultBlockState();
		public static final BlockState MAGENTA_PETUNIA = ECBlocks.Plant.MAGENTA_PETUNIA.defaultBlockState();

		public static final ConfiguredFeature<?, ?> FLOWER_PETUNIA_PLAINS = register(
				"flower_petunia_plains", Feature.FLOWER, new BlockClusterFeatureConfig.Builder(
						(new WeightedBlockStateProvider())
								.add(Blocks.ALLIUM.defaultBlockState(), 2)
								.add(CYAN_PETUNIA, 4)
								.add(MAGENTA_PETUNIA, 4)
								.add(Blocks.POPPY.defaultBlockState(), 1)
								.add(Blocks.DANDELION.defaultBlockState(), 1),
						SimpleBlockPlacer.INSTANCE
				).tries(64).build(),
				Features.Placements.ADD_32, Features.Placements.HEIGHTMAP_SQUARE, 4
		);
		
		public static final ConfiguredFeature<?, ?> FLOWER_HIGAN_BANA = register(
				"flower_higan_bana", Feature.FLOWER, new BlockClusterFeatureConfig.Builder(
						new SimpleBlockStateProvider(ECBlocks.Plant.HIGAN_BANA.defaultBlockState()),
						SimpleBlockPlacer.INSTANCE
				).tries(96).build(),
				Features.Placements.ADD_32, Features.Placements.HEIGHTMAP_SQUARE, 6
		);

		private VegetationFeatures() {}

		private static void init() {}
	}

	public static final class SpecialFeatures {
		public static final ConfiguredFeature<?, ?> XANADU_DELTA = register(
				"xanadu_delta", Feature.DELTA_FEATURE,
				new BasaltDeltasFeature(
						Blocks.WATER.defaultBlockState(),
						ECBlocks.Plant.MOSS_BLOCK.defaultBlockState(),
						FeatureSpread.of(3, 7),
						FeatureSpread.fixed(0)
				),
				Placement.COUNT_MULTILAYER.configured(new FeatureSpreadConfig(6))
		);

		public static final ConfiguredFeature<?, ?> KARST_DELTA = register(
				"karst_delta", Feature.DELTA_FEATURE,
				new BasaltDeltasFeature(
						Blocks.WATER.defaultBlockState(),
						Blocks.GRAVEL.defaultBlockState(),
						FeatureSpread.of(5, 8),
						FeatureSpread.of(0, 2)
				),
				Placement.COUNT_MULTILAYER.configured(new FeatureSpreadConfig(2))
		);

		private SpecialFeatures() {}

		private static void init() {}
	}

	public static void init() {
		OreConfiguredFeatures.init();
		StructureConfiguredFeatures.init();
		TreeConfiguredFeatures.init();
		VegetationFeatures.init();
		SpecialFeatures.init();
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> register(String id, F f, FC fc) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc));
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<?, ?> register(String id, F f, FC fc, ConfiguredPlacement<?> placement) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc).decorated(placement));
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<?, ?> register(String id, F f, FC fc, ConfiguredPlacement<?> placement1, ConfiguredPlacement<?> placement2, int count) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc).decorated(placement1).decorated(placement2).count(count));
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<?, ?> register2(String id, F f, FC fc, ConfiguredPlacement<?> placement) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc).decorated(placement).squared());
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<?, ?> register2(String id, F f, FC fc, ConfiguredPlacement<?> placement, int count) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc).decorated(placement).squared().count(count));
	}

	private static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<?, ?> register2(String id, F f, FC fc, int range, int count) {
		return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), new ConfiguredFeature<>(f, fc).range(range).squared().count(count));
	}
}
