package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeTags {
	public static final TagKey<Biome> HAS_SHELTER = create("has_structure/shelter");
	public static final TagKey<Biome> HAS_NETHER_WARFIELD = create("has_structure/nether_warfield");
	public static final TagKey<Biome> HAS_ENTRENCHMENT = create("has_structure/entrenchment");
	public static final TagKey<Biome> HAS_VILLAGE_SWAMP = create("has_structure/village_swamp");
	public static final TagKey<Biome> HAS_HOLLOW_TREE = create("has_structure/hollow_tree");

	public static final TagKey<Biome> HAS_CAMP_BADLANDS = create("has_structure/camp_badlands");
	public static final TagKey<Biome> HAS_CAMP_BIRCH = create("has_structure/camp_birch");
	public static final TagKey<Biome> HAS_CAMP_DESERT = create("has_structure/camp_desert");
	public static final TagKey<Biome> HAS_CAMP_JUNGLE = create("has_structure/camp_jungle");
	public static final TagKey<Biome> HAS_CAMP_PLAINS = create("has_structure/camp_plains");
	public static final TagKey<Biome> HAS_CAMP_SAVANNA = create("has_structure/camp_savanna");
	public static final TagKey<Biome> HAS_CAMP_SNOW = create("has_structure/camp_snow");
	public static final TagKey<Biome> HAS_CAMP_STONY = create("has_structure/camp_stony");
	public static final TagKey<Biome> HAS_CAMP_SWAMP = create("has_structure/camp_swamp");
	public static final TagKey<Biome> HAS_CAMP_TAIGA = create("has_structure/camp_taiga");

	public static final TagKey<Biome> DEAD_CRIMSON_OCEAN = create("dead_crimson_ocean");
	public static final TagKey<Biome> DEAD_WARPED_OCEAN = create("dead_warped_ocean");


	private static TagKey<Biome> create(String name) {
		return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MODID, name));
	}
}
