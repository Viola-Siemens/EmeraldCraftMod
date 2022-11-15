package com.hexagram2021.emeraldcraft.api.camp;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum CampTypes implements CampType {
	BADLANDS,
	BIRCH,
	DESERT,
	JUNGLE,
	PLAINS,
	SAVANNA,
	SNOW,
	STONY,
	SWAMP,
	TAIGA;

	private static final Map<String, ResourceLocation> CUSTOM_CAMP = new HashMap<>();

	//API for custom camp.
	@SuppressWarnings("unused")
	public static void addCustomCamp(CampType type, ResourceLocation structure) {
		CUSTOM_CAMP.put(type.toString(), structure);
	}

	public static ResourceLocation getCampWithType(CampType type) {
		return CUSTOM_CAMP.get(type.toString());
	}
}
