package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.OptionalInt;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECConfiguredFeatures {


	public static final class OreConfiguredFeatures {
		public static final ConfiguredFeature<OreConfiguration, ?> ORE_DEBRIS_EXTRA = register(
				"ore_debris_extra", Feature.SCATTERED_ORE.configured(new OreConfiguration(
						OreFeatures.NETHER_ORE_REPLACEABLES,
						Blocks.ANCIENT_DEBRIS.defaultBlockState(),
						4, 0.8F
				))
		);
		public static final ConfiguredFeature<OreConfiguration, ?> ORE_QUARTZ_EXTRA = register(
				"ore_quartz_extra", Feature.ORE.configured(new OreConfiguration(
						OreFeatures.NETHERRACK,
						Blocks.NETHER_QUARTZ_ORE.defaultBlockState(),
						16
				))
		);

		private OreConfiguredFeatures() {}
	}

	public static final class StructureConfiguredFeatures {
		public static final ConfiguredFeature<NoneFeatureConfiguration, ?> ZOMBIE_VILLAGER_ROOM = register(
				"zombie_villager_room", ECFeatures.ZOMBIE_VILLAGER_ROOM.configured(FeatureConfiguration.NONE)
		);

		private StructureConfiguredFeatures() {}
	}

	public static final class TreeConfiguredFeatures {
		public static final Block GINKGO_LOG = ECBlocks.Plant.GINKGO_LOG.get();
		public static final Block GINKGO_LEAVES = ECBlocks.Plant.GINKGO_LEAVES.get();
		public static final Block PALM_LOG = ECBlocks.Plant.PALM_LOG.get();
		public static final Block PALM_LEAVES = ECBlocks.Plant.PALM_LEAVES.get();
		public static final Block PEACH_LOG = ECBlocks.Plant.PEACH_LOG.get();
		public static final Block PEACH_LEAVES = ECBlocks.Plant.PEACH_LEAVES.get();

		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_1 = register(
				"ginkgo_1", Feature.TREE.configured(createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 3, 2, 1, 2
				).build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_2 = register(
				"ginkgo_2", Feature.TREE.configured(createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 3, 2, 2, 3
				).build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_3 = register(
				"ginkgo_3", Feature.TREE.configured(createStraightBlobGinkgoTree(
						GINKGO_LOG, GINKGO_LEAVES, 4, 2, 6, 3
				).build())
		);

		public static final ConfiguredFeature<TreeConfiguration, ?> PALM_1 = register(
				"palm_1", Feature.TREE.configured(createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 4, 2, 1, 2
				).ignoreVines().build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> PALM_2 = register(
				"palm_2", Feature.TREE.configured(createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 5, 2, 2, 3
				).ignoreVines().build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> PALM_3 = register(
				"palm_3", Feature.TREE.configured(createStraightBlobPalmTree(
						PALM_LOG, PALM_LEAVES, 6, 2, 6, 3
				).ignoreVines().build())
		);

		public static final ConfiguredFeature<TreeConfiguration, ?> PEACH_1 = register(
				"peach_1", Feature.TREE.configured(createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 4, 2, 1, 2
				).ignoreVines().build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> PEACH_2 = register(
				"peach_2", Feature.TREE.configured(createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 5, 2, 2, 3
				).ignoreVines().build())
		);
		public static final ConfiguredFeature<TreeConfiguration, ?> PEACH_3 = register(
				"peach_3", Feature.TREE.configured(createStraightBlobPeachTree(
						PEACH_LOG, PEACH_LEAVES, 6, 2, 1, 3
				).ignoreVines().build())
		);

		public static final ConfiguredFeature<HugeFungusConfiguration, ?> PURPURACEUS_FUNGUS = register(
				"purpuraceus_fungus", Feature.HUGE_FUNGUS.configured(new HugeFungusConfiguration(
						ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_STEM.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_WART_BLOCK.defaultBlockState(),
						Blocks.SHROOMLIGHT.defaultBlockState(), false
				))
		);
		public static final ConfiguredFeature<HugeFungusConfiguration, ?> PURPURACEUS_FUNGUS_PLANTED = register(
				"purpuraceus_fungus_planted", Feature.HUGE_FUNGUS.configured(new HugeFungusConfiguration(
						ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_STEM.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_WART_BLOCK.defaultBlockState(),
						Blocks.SHROOMLIGHT.defaultBlockState(), true
				))
		);

		public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_GINKGO = register(
				"trees_ginkgo", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
						List.of(
								new WeightedPlacedFeature(TreeConfiguredFeatures.GINKGO_2.placed(), 0.3F),
								new WeightedPlacedFeature(TreeConfiguredFeatures.GINKGO_3.placed(), 0.2F)
						),
						TreeConfiguredFeatures.GINKGO_1.placed()
				))
		);

		public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_PALM = register(
				"trees_palm", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
						List.of(
								new WeightedPlacedFeature(TreeConfiguredFeatures.PALM_2.placed(), 0.3F),
								new WeightedPlacedFeature(TreeConfiguredFeatures.PALM_3.placed(), 0.2F)
						),
						TreeConfiguredFeatures.PALM_1.placed()
				))
		);

		public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_PEACH = register(
				"trees_peach", Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
						ImmutableList.of(
								new WeightedPlacedFeature(TreeConfiguredFeatures.PEACH_2.placed(), 0.3F),
								new WeightedPlacedFeature(TreeConfiguredFeatures.PEACH_3.placed(), 0.2F)
						),
						TreeConfiguredFeatures.PEACH_1.placed()
				))
		);

		@SuppressWarnings("SameParameterValue")
		private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobGinkgoTree(Block logBlock, Block leavesBlock,
																							   int baseHeight, int heightRandA, int heightRandB,
																							   int radius) {
			return new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(logBlock),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					BlockStateProvider.simple(leavesBlock),
					new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 4),
					new TwoLayersFeatureSize(1, 0, 1)
			);
		}

		@SuppressWarnings("SameParameterValue")
		private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobPalmTree(Block logBlock, Block leavesBlock,
																							   int baseHeight, int heightRandA, int heightRandB,
																							   int radius) {
			return new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(logBlock),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					BlockStateProvider.simple(leavesBlock),
					new AcaciaFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0)),
					new TwoLayersFeatureSize(1, 0, 2)
			);
		}

		@SuppressWarnings("SameParameterValue")
		private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobPeachTree(Block logBlock, Block leavesBlock,
																				 int baseHeight, int heightRandA, int heightRandB,
																				 int radius) {
			return new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(logBlock),
					new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
					BlockStateProvider.simple(leavesBlock),
					new DarkOakFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0)),
					new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
			);
		}

		private TreeConfiguredFeatures() {}
	}

	public static final class VegetationFeatures {
		public static final Block CYAN_PETUNIA = ECBlocks.Plant.CYAN_PETUNIA.get();
		public static final Block MAGENTA_PETUNIA = ECBlocks.Plant.MAGENTA_PETUNIA.get();

		public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_PETUNIA_PLAINS = register(
				"flower_petunia_plains", Feature.FLOWER.configured(new RandomPatchConfiguration(
						64, 6, 2, () -> Feature.SIMPLE_BLOCK.configured(
								new SimpleBlockConfiguration(new NoiseThresholdProvider(
										2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, 0.5F, 0.75F,
										Blocks.ALLIUM.defaultBlockState(),
										List.of(
												CYAN_PETUNIA.defaultBlockState(),
												MAGENTA_PETUNIA.defaultBlockState()
										), List.of(
												Blocks.POPPY.defaultBlockState(),
												Blocks.DANDELION.defaultBlockState()
										)
								))
						).onlyWhenEmpty()
				))
		);

		public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_HIGAN_BANA = register(
				"flower_higan_bana", Feature.FLOWER.configured(new RandomPatchConfiguration(
						96, 7, 2, () -> Feature.SIMPLE_BLOCK.configured(
								new SimpleBlockConfiguration(
										BlockStateProvider.simple(ECBlocks.Plant.HIGAN_BANA.defaultBlockState())
								)
						).onlyWhenEmpty()
				))
		);

		public static final WeightedStateProvider PURPURACEUS_VEGETATION_PROVIDER = new WeightedStateProvider(
				SimpleWeightedRandomList.<BlockState>builder()
						.add(Blocks.CRIMSON_ROOTS.defaultBlockState(), 2)
						.add(Blocks.WARPED_ROOTS.defaultBlockState(), 2)
						.add(ECBlocks.Plant.PURPURACEUS_FUNGUS.defaultBlockState(), 16)
						.add(ECBlocks.Plant.PURPURACEUS_ROOTS.defaultBlockState(), 80)
		);
		public static final ConfiguredFeature<NetherForestVegetationConfig, ?> PURPURACEUS_SWAMP_VEGETATION = register(
				"purpuraceus_forest_vegetation", Feature.NETHER_FOREST_VEGETATION.configured(
						new NetherForestVegetationConfig(PURPURACEUS_VEGETATION_PROVIDER, 8, 4)
				)
		);
		public static final ConfiguredFeature<NetherForestVegetationConfig, ?> PURPURACEUS_SWAMP_VEGETATION_BONEMEAL = register(
				"purpuraceus_forest_vegetation_bonemeal", Feature.NETHER_FOREST_VEGETATION.configured(
					new NetherForestVegetationConfig(PURPURACEUS_VEGETATION_PROVIDER, 3, 1)
				)
		);
	}

	public static final class SpecialFeatures {
		public static final ConfiguredFeature<NoneFeatureConfiguration, ?> VOLCANIC_CAVES_LAVA_POOL = register(
				"volcanic_caves_lava_pool", ECFeatures.VOLCANIC_CAVES_LAVA_POOL.configured(FeatureConfiguration.NONE)
		);

		public static final ConfiguredFeature<DeltaFeatureConfiguration, ?> XANADU_DELTA = register(
				"xanadu_delta", Feature.DELTA_FEATURE.configured(new DeltaFeatureConfiguration(
						Blocks.WATER.defaultBlockState(),
						Blocks.MOSS_BLOCK.defaultBlockState(),
						UniformInt.of(3, 9),
						ConstantInt.of(0)
				))
		);

		public static final ConfiguredFeature<DeltaFeatureConfiguration, ?> KARST_DELTA = register(
				"karst_delta", Feature.DELTA_FEATURE.configured(new DeltaFeatureConfiguration(
						Blocks.WATER.defaultBlockState(),
						Blocks.GRAVEL.defaultBlockState(),
						UniformInt.of(5, 12),
						UniformInt.of(0, 2)
				))
		);

		public static final ConfiguredFeature<DeltaFeatureConfiguration, ?> PURPURACEUS_SWAMP_DELTA = register(
				"purpuraceus_swamp_delta", Feature.DELTA_FEATURE.configured(new DeltaFeatureConfiguration(
						Blocks.MAGMA_BLOCK.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(),
						UniformInt.of(2, 7),
						UniformInt.of(0, 2)
				))
		);

		public static final ConfiguredFeature<DeltaFeatureConfiguration, ?> PURPURACEUS_SWAMP_LAVA_DELTA = register(
				"purpuraceus_swamp_lava_delta", Feature.DELTA_FEATURE.configured(new DeltaFeatureConfiguration(
						Blocks.LAVA.defaultBlockState(),
						ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(),
						UniformInt.of(4, 10),
						UniformInt.of(0, 2)
				))
		);
	}
	
	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), configuredFeature);
	}
}
