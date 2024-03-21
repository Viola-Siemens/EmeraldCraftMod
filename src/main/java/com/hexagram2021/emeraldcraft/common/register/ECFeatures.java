package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.world.features.RawBerylFeature;
import com.hexagram2021.emeraldcraft.common.world.features.VineGrowthFeature;
import com.hexagram2021.emeraldcraft.common.world.features.VolcanicCavesLavaPoolFeature;
import com.hexagram2021.emeraldcraft.common.world.features.ZombieVillagerRoomFeature;
import com.hexagram2021.emeraldcraft.common.world.features.configuration.VineGrowthConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECFeatures {
	public static final Feature<NoneFeatureConfiguration> ZOMBIE_VILLAGER_ROOM = new ZombieVillagerRoomFeature(NoneFeatureConfiguration.CODEC);
	public static final Feature<NoneFeatureConfiguration> VOLCANIC_CAVES_LAVA_POOL = new VolcanicCavesLavaPoolFeature(NoneFeatureConfiguration.CODEC);
	public static final Feature<VineGrowthConfiguration> VINE_GROWTH = new VineGrowthFeature(VineGrowthConfiguration.CODEC);
	public static final Feature<NoneFeatureConfiguration> RAW_BERYL = new RawBerylFeature(NoneFeatureConfiguration.CODEC);

	public static void init(ECContent.RegisterConsumer<Feature<?>> register) {
		register.accept(new ResourceLocation(MODID, "zombie_villager_room"), ZOMBIE_VILLAGER_ROOM);
		register.accept(new ResourceLocation(MODID, "volcanic_caves_lava_pool"), VOLCANIC_CAVES_LAVA_POOL);
		register.accept(new ResourceLocation(MODID, "vine_growth"), VINE_GROWTH);
		register.accept(new ResourceLocation(MODID, "raw_beryl"), RAW_BERYL);
	}
}
