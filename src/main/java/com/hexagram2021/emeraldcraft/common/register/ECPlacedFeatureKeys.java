package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECPlacedFeatureKeys {
	public static final ResourceKey<PlacedFeature> ORE_LAPIS_EXTRA = createKey("ore_lapis_extra");
	public static final ResourceKey<PlacedFeature> ORE_EMERALD_EXTRA = createKey("ore_emerald_extra");
	public static final ResourceKey<PlacedFeature> ORE_DEBRIS_EXTRA = createKey("ore_debris_extra");
	public static final ResourceKey<PlacedFeature> ORE_QUARTZ_EXTRA = createKey("ore_quartz_extra");
	public static final ResourceKey<PlacedFeature> ORE_MAGMA_EXTRA = createKey("ore_magma_extra");
	public static final ResourceKey<PlacedFeature> ORE_MOSSY_STONE = createKey("ore_mossy_stone");

	public static final ResourceKey<PlacedFeature> ZOMBIE_VILLAGER_ROOM = createKey("zombie_villager_room");

	public static final ResourceKey<PlacedFeature> TREES_GINKGO = createKey("trees_ginkgo");
	public static final ResourceKey<PlacedFeature> TREES_PALM = createKey("trees_palm");
	public static final ResourceKey<PlacedFeature> TREES_PEACH = createKey("trees_peach");

	public static final ResourceKey<PlacedFeature> PURPURACEUS_FUNGI = createKey("purpuraceus_fungi");

	public static final ResourceKey<PlacedFeature> FLOWER_PETUNIA_PLAINS = createKey("flower_petunia_plains");

	public static final ResourceKey<PlacedFeature> FLOWER_HIGAN_BANA = createKey("flower_higan_bana");

	public static final ResourceKey<PlacedFeature> PURPURACEUS_SWAMP_VEGETATION = createKey("purpuraceus_swamp_vegetation");

	public static final ResourceKey<PlacedFeature> VOLCANIC_CAVES_LAVA_POOL = createKey("volcanic_caves_lava_pool");

	public static final ResourceKey<PlacedFeature> VINES_EXTRA = createKey("vines_extra");

	public static final ResourceKey<PlacedFeature> XANADU_DELTA = createKey("xanadu_delta");
	public static final ResourceKey<PlacedFeature> KARST_DELTA = createKey("karst_delta");
	public static final ResourceKey<PlacedFeature> PURPURACEUS_SWAMP_DELTA = createKey("purpuraceus_swamp_delta");
	public static final ResourceKey<PlacedFeature> PURPURACEUS_SWAMP_LAVA_DELTA = createKey("purpuraceus_swamp_lava_delta");

	public static void init() {}

	public static ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MODID, name));
	}
}
