package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECConfiguredFeatures {


	public static final class OreConfiguredFeatures {
		public static final ConfiguredFeature<?, ?> ORE_DEBRIS_EXTRA = register("ore_debris_extra",
				Feature.SCATTERED_ORE.configured(new OreConfiguration(OreFeatures.NETHER_ORE_REPLACEABLES,
						Blocks.ANCIENT_DEBRIS.defaultBlockState(), 4, 0.8F)));
		public static final ConfiguredFeature<?, ?> ORE_QUARTZ_EXTRA = register("ore_quartz_extra",
				Feature.ORE.configured(new OreConfiguration(OreFeatures.NETHERRACK,
						Blocks.NETHER_QUARTZ_ORE.defaultBlockState(), 16)));

		private OreConfiguredFeatures() {}
	}

	public static final class StructureConfiguredFeatures {
		public static ConfiguredFeature<NoneFeatureConfiguration, ?> ZOMBIE_VILLAGER_ROOM = register("zombie_villager_room",
				ECFeatures.ZOMBIE_VILLAGER_ROOM.configured(FeatureConfiguration.NONE));

		private StructureConfiguredFeatures() {}
	}

	public static final class TreeConfiguredFeatures {
		public static final Block GINKGO_LOG = ECBlocks.Plant.GINKGO_LOG.get();
		public static final Block GINKGO_LEAVES = ECBlocks.Plant.GINKGO_LEAVES.get();

		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_1 = register("ginkgo_1",
				Feature.TREE.configured(createStraightBlobGinkgoTree(GINKGO_LOG, GINKGO_LEAVES, 3, 2, 1, 2).build()));
		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_2 = register("ginkgo_2",
				Feature.TREE.configured(createStraightBlobGinkgoTree(GINKGO_LOG, GINKGO_LEAVES, 3, 2, 2, 3).build()));
		public static final ConfiguredFeature<TreeConfiguration, ?> GINKGO_3 = register("ginkgo_3",
				Feature.TREE.configured(createStraightBlobGinkgoTree(GINKGO_LOG, GINKGO_LEAVES, 4, 2, 6, 3).build()));

		public static final ConfiguredFeature<RandomFeatureConfiguration, ?> TREES_GINKGO = register("trees_ginkgo",
				Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(
						List.of(
								new WeightedPlacedFeature(TreeConfiguredFeatures.GINKGO_2.placed(), 0.3F),
								new WeightedPlacedFeature(TreeConfiguredFeatures.GINKGO_3.placed(), 0.2F)
						),
						TreeConfiguredFeatures.GINKGO_1.placed()
				)));

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

		private TreeConfiguredFeatures() {}
	}

	public static final class VegetationFeatures {
		public static final Block CYAN_PETUNIA = ECBlocks.Plant.CYAN_PETUNIA.get();
		public static final Block MAGENTA_PETUNIA = ECBlocks.Plant.MAGENTA_PETUNIA.get();

		public static final ConfiguredFeature<?, ?> FLOWER_PETUNIA_PLAINS = register(
				"flower_petunia_plains",
				Feature.FLOWER.configured(new RandomPatchConfiguration(
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
	}

	public static final class SpecialFeatures {
		public static final ConfiguredFeature<?, ?> VOLCANIC_CAVES_LAVA_POOL = register("volcanic_caves_lava_pool",
				ECFeatures.VOLCANIC_CAVES_LAVA_POOL.configured(FeatureConfiguration.NONE));
	}

	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MODID, id), configuredFeature);
	}
}
