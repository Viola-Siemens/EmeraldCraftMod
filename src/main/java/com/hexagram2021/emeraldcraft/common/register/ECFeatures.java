package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.world.features.RawBerylFeature;
import com.hexagram2021.emeraldcraft.common.world.features.VineGrowthFeature;
import com.hexagram2021.emeraldcraft.common.world.features.VolcanicCavesLavaPoolFeature;
import com.hexagram2021.emeraldcraft.common.world.features.ZombieVillagerRoomFeature;
import com.hexagram2021.emeraldcraft.common.world.features.configuration.VineGrowthConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ECFeatures {
    public static final Feature<NoneFeatureConfiguration> ZOMBIE_VILLAGER_ROOM = register("zombie_villager_room", new ZombieVillagerRoomFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> VOLCANIC_CAVES_LAVA_POOL = register("volcanic_caves_lava_pool", new VolcanicCavesLavaPoolFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<VineGrowthConfiguration> VINE_GROWTH = register("vine_growth", new VineGrowthFeature(VineGrowthConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> RAW_BERYL = register("raw_beryl", new RawBerylFeature(NoneFeatureConfiguration.CODEC));

    private static <T extends FeatureConfiguration> Feature<T> register(String name, Feature<T> feature) {
        return Registry.register(BuiltInRegistries.FEATURE, EmeraldCraft.id(name), feature);
    }

    public static void init() {
    }
}
