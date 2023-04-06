package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECConfiguredFeatureKeys {
	public static final class OreConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DEBRIS_EXTRA = createKey("ore_debris_extra");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ_EXTRA = createKey("ore_quartz_extra");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MAGMA_EXTRA = createKey("ore_magma_extra");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOSSY_STONE = createKey("ore_mossy_stone");

		private OreConfiguredFeatures() {}

		public static void init() {}
	}

	public static final class StructureConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> ZOMBIE_VILLAGER_ROOM = createKey("zombie_villager_room");

		private StructureConfiguredFeatures() {}

		public static void init() {}
	}

	public static final class TreeConfiguredFeatures {

		public static final ResourceKey<ConfiguredFeature<?, ?>> GINKGO = createKey("ginkgo");
		public static final ResourceKey<ConfiguredFeature<?, ?>> GINKGO_TALL = createKey("ginkgo_tall");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PALM = createKey("palm");
		public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TALL = createKey("palm_tall");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PEACH = createKey("peach");
		public static final ResourceKey<ConfiguredFeature<?, ?>> PEACH_BEES = createKey("peach_bees");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_FUNGUS = createKey("purpuraceus_fungus");
		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_FUNGUS_PLANTED = createKey("purpuraceus_fungus_planted");


		public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_GINKGO = createKey("trees_ginkgo");
		public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PALM = createKey("trees_palm");
		public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PEACH = createKey("trees_peach");

		private TreeConfiguredFeatures() {}

		public static void init() {}
	}

	public static final class VegetationConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PETUNIA_PLAINS = createKey("flower_petunia_plains");

		public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_HIGAN_BANA = createKey("flower_higan_bana");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_SWAMP_VEGETATION = createKey("purpuraceus_forest_vegetation");
		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_SWAMP_VEGETATION_BONEMEAL = createKey("purpuraceus_forest_vegetation_bonemeal");

		private VegetationConfiguredFeatures() {}

		public static void init() {}
	}

	public static final class SpecialConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANIC_CAVES_LAVA_POOL = createKey("volcanic_caves_lava_pool");

		public static final ResourceKey<ConfiguredFeature<?, ?>> VINES_EXTRA = createKey("vines_extra");

		public static final ResourceKey<ConfiguredFeature<?, ?>> XANADU_DELTA = createKey("xanadu_delta");

		public static final ResourceKey<ConfiguredFeature<?, ?>> KARST_DELTA = createKey("karst_delta");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_SWAMP_DELTA = createKey("purpuraceus_swamp_delta");

		public static final ResourceKey<ConfiguredFeature<?, ?>> PURPURACEUS_SWAMP_LAVA_DELTA = createKey("purpuraceus_swamp_lava_delta");

		private SpecialConfiguredFeatures() {}

		public static void init() {}
	}

	public static void init() {
		OreConfiguredFeatures.init();
		StructureConfiguredFeatures.init();
		TreeConfiguredFeatures.init();
		VegetationConfiguredFeatures.init();
		SpecialConfiguredFeatures.init();
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MODID, name));
	}
}
