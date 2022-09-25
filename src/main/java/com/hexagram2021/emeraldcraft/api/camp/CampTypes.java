package com.hexagram2021.emeraldcraft.api.camp;

import com.hexagram2021.emeraldcraft.common.register.ECConfiguredStructures;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;

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
	public static void addCustomCamp(CampType type, ResourceLocation structure, Holder<ConfiguredStructureFeature<?, ?>> holder) {
		CUSTOM_CAMP.put(type.toString(), structure);
		ECConfiguredStructures.ALL_CAMPS.add(StructureSet.entry(holder));
	}

	public static ResourceLocation getCampWithType(CampType type) {
		return CUSTOM_CAMP.get(type.toString());
	}
}
