package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.VolcanicCavesLavaPoolFeature;
import com.hexagram2021.emeraldcraft.common.world.ZombieVillagerRoomFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECFeatures {
	public static final Feature<NoneFeatureConfiguration> ZOMBIE_VILLAGER_ROOM = new ZombieVillagerRoomFeature(NoneFeatureConfiguration.CODEC);
	public static final Feature<NoneFeatureConfiguration> VOLCANIC_CAVES_LAVA_POOL = new VolcanicCavesLavaPoolFeature(NoneFeatureConfiguration.CODEC);

	public static void init(RegistryEvent.Register<Feature<?>> event) {
		ZOMBIE_VILLAGER_ROOM.setRegistryName(MODID, "zombie_villager_room");
		VOLCANIC_CAVES_LAVA_POOL.setRegistryName(MODID, "volcanic_caves_lava_pool");
		event.getRegistry().register(ZOMBIE_VILLAGER_ROOM);
		event.getRegistry().register(VOLCANIC_CAVES_LAVA_POOL);
	}
}
