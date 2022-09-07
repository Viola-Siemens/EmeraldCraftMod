package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.ZombieVillagerRoomFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECFeatures {
	public static final Feature<NoFeatureConfig> ZOMBIE_VILLAGER_ROOM = new ZombieVillagerRoomFeature(NoFeatureConfig.CODEC);

	public static void init(RegistryEvent.Register<Feature<?>> event) {
		ZOMBIE_VILLAGER_ROOM.setRegistryName(MODID, "zombie_villager_room");
		event.getRegistry().register(ZOMBIE_VILLAGER_ROOM);
	}
}
