package com.hexagram2021.emeraldcraft.api.camp;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.register.ECStructures;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

	static final List<CampType> ALL_CAMPS = Lists.newArrayList(Arrays.stream(CampTypes.values()).iterator());
	static final Map<String, Integer> ALL_CAMP_IDS = Util.make(new HashMap<>(), map -> {
		for(int i = 0; i < ALL_CAMPS.size(); ++i) {
			map.put(ALL_CAMPS.get(i).toString(), i);
		}
	});

	private static final Map<String, ResourceLocation> CUSTOM_CAMP = new HashMap<>();

	/**
	 * API for custom camp.
	 * You can add your camp at any time before tags updated.
	 * But please remember that you should only add once.
	 *
	 * @param type - Your custom type of camp.
	 * @param structure - The resource location of the template of camp structure you made.
	 * @param holder - The resource key of your camp structure.
	*/
	@SuppressWarnings("unused")
	public static void addCustomCamp(CampType type, ResourceLocation structure, ResourceKey<Structure> holder) {
		CUSTOM_CAMP.put(type.toString(), structure);
		ALL_CAMP_IDS.put(type.toString(), ALL_CAMPS.size());
		ALL_CAMPS.add(type);
		ECStructures.ALL_CAMPS.add(holder);
	}

	@Nullable
	public static ResourceLocation getCampWithType(CampType type) {
		return CUSTOM_CAMP.get(type.toString());
	}
}
