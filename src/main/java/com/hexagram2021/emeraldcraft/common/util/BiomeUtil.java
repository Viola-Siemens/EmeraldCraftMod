package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.register.ECBiomeKeys;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;

public class BiomeUtil {
	@SafeVarargs
	public static ResourceKey<Biome> biomeOrFallback(Registry<Biome> biomeRegistry, ECBiomeKeys.BiomeKey key, ResourceKey<Biome>... biomes) {
		if (isKeyRegistered(biomeRegistry, key)) {
			return key.key();
		}
		for(ResourceKey<Biome> biome: biomes)  {
			if(biome != null) {
				return biome;
			}
		}
		throw new RuntimeException("Failed to find fallback for biome!");
	}

	public static boolean isKeyRegistered(Registry<Biome> registry, @Nullable ECBiomeKeys.BiomeKey key) {
		return key != null && key.generate() && registry.get(key.key()) != null;
	}
}
