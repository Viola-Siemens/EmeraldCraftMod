package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;

public class ECBiomeTags {
	public static boolean hasShelter(ResourceLocation biome) {
		return Biomes.CRIMSON_FOREST.location().equals(biome);
	}

	public static boolean hasNetherWarfield(ResourceLocation biome) {
		return ECBiomeKeys.EMERY_DESERT.location().equals(biome);
	}

	public static boolean hasSwampVillage(ResourceLocation biome) {
		return	Biomes.SWAMP.location().equals(biome) ||
				ECBiomeKeys.XANADU.location().equals(biome);
	}

	public static boolean hasCampWithType(ResourceLocation biome, CampType type) {
		return type.isTargetBiome(biome);
	}
}
