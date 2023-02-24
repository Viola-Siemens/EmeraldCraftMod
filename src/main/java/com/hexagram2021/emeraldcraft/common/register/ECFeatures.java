package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.features.VineGrowthFeature;
import com.hexagram2021.emeraldcraft.common.world.features.VolcanicCavesLavaPoolFeature;
import com.hexagram2021.emeraldcraft.common.world.features.ZombieVillagerRoomFeature;
import com.hexagram2021.emeraldcraft.common.world.features.configuration.VineGrowthConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.RegisterEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECFeatures {
	public static final Feature<NoneFeatureConfiguration> ZOMBIE_VILLAGER_ROOM = new ZombieVillagerRoomFeature(NoneFeatureConfiguration.CODEC);
	public static final Feature<NoneFeatureConfiguration> VOLCANIC_CAVES_LAVA_POOL = new VolcanicCavesLavaPoolFeature(NoneFeatureConfiguration.CODEC);
	public static final Feature<VineGrowthConfiguration> VINE_GROWTH = new VineGrowthFeature(VineGrowthConfiguration.CODEC);

	public static void init(RegisterEvent event) {
		event.register(Registry.FEATURE_REGISTRY, helper -> {
			helper.register(new ResourceLocation(MODID, "zombie_villager_room"), ZOMBIE_VILLAGER_ROOM);
			helper.register(new ResourceLocation(MODID, "volcanic_caves_lava_pool"), VOLCANIC_CAVES_LAVA_POOL);
			helper.register(new ResourceLocation(MODID, "vine_growth"), VINE_GROWTH);
		});
	}
}
