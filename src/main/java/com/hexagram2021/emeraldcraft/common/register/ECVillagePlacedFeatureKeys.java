package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECVillagePlacedFeatureKeys {

	public static final ResourceKey<PlacedFeature> DARK_OAK_VILLAGE = createKey("dark_oak");

	public static final ResourceKey<PlacedFeature> FLOWER_SWAMP_VILLAGE = createKey("flower_swamp");

	public static void init() {}

	public static ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MODID, name));
	}
}
