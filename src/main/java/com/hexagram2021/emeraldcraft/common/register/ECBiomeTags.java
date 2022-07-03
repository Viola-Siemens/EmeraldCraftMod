package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeTags {
	public static final TagKey<Biome> HAS_SHELTER = create("has_structure/shelter");
	public static final TagKey<Biome> HAS_NETHER_WARFIELD = create("has_structure/nether_warfield");


	private static TagKey<Biome> create(String name) {
		return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MODID, name));
	}
}
