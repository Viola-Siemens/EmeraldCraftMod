package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeKeys {
//Overworld
	//Ocean
	public static final ResourceKey<Biome> DEAD_CRIMSON_OCEAN = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "dead_crimson_ocean"));
	public static final ResourceKey<Biome> DEAD_WARPED_OCEAN = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "dead_warped_ocean"));

	public static final ResourceKey<Biome> DEEP_DEAD_CRIMSON_OCEAN = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "deep_dead_crimson_ocean"));
	public static final ResourceKey<Biome> DEEP_DEAD_WARPED_OCEAN = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "deep_dead_warped_ocean"));

	//Island
	public static final ResourceKey<Biome> XANADU = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "xanadu"));

	//Forest
	public static final ResourceKey<Biome> GINKGO_FOREST = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "ginkgo_forest"));

	//Hills
	public static final ResourceKey<Biome> KARST_HILLS = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "karst_hills"));

	//Plain
	public static final ResourceKey<Biome> PETUNIA_PLAINS = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "petunia_plains"));

	//Beach
	public static final ResourceKey<Biome> GOLDEN_BEACH = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "golden_beach"));
	public static final ResourceKey<Biome> PALM_BEACH = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "palm_beach"));

	//Desert
	public static final ResourceKey<Biome> AZURE_DESERT = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "azure_desert"));
	public static final ResourceKey<Biome> JADEITE_DESERT = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "jadeite_desert"));

	//Underground
	public static final ResourceKey<Biome> VOLCANIC_CAVES = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "volcanic_caves"));

//Nether
	public static final ResourceKey<Biome> EMERY_DESERT = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "emery_desert"));
	public static final ResourceKey<Biome> QUARTZ_DESERT = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "quartz_desert"));
	public static final ResourceKey<Biome> PURPURACEUS_SWAMP = ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "purpuraceus_swamp"));
}
