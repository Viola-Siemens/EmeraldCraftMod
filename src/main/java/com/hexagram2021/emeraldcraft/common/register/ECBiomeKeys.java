package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeKeys {
//Overworld
	//Ocean
	public static final BiomeKey DEAD_CRIMSON_OCEAN = registerKey(ECCommonConfig.GENERATE_DEAD_CRIMSON_OCEAN.get(), "dead_crimson_ocean");
	public static final BiomeKey DEAD_WARPED_OCEAN = registerKey(ECCommonConfig.GENERATE_DEAD_WARPED_OCEAN.get(), "dead_warped_ocean");

	public static final BiomeKey DEEP_DEAD_CRIMSON_OCEAN = registerKey(ECCommonConfig.GENERATE_DEAD_CRIMSON_OCEAN.get(), "deep_dead_crimson_ocean");
	public static final BiomeKey DEEP_DEAD_WARPED_OCEAN = registerKey(ECCommonConfig.GENERATE_DEAD_WARPED_OCEAN.get(), "deep_dead_warped_ocean");

	//Island
	public static final BiomeKey XANADU = registerKey(ECCommonConfig.GENERATE_XANADU.get(), "xanadu");

	//Forest
	public static final BiomeKey GINKGO_FOREST = registerKey(ECCommonConfig.GENERATE_GINKGO_FOREST.get(), "ginkgo_forest");

	//Hills
	public static final BiomeKey KARST_HILLS = registerKey(ECCommonConfig.GENERATE_KARST_HILLS.get(), "karst_hills");

	//Plain
	public static final BiomeKey PETUNIA_PLAINS = registerKey(ECCommonConfig.GENERATE_PETUNIA_PLAINS.get(), "petunia_plains");

	//Beach
	public static final BiomeKey GOLDEN_BEACH = registerKey(ECCommonConfig.GENERATE_GOLDEN_BEACH.get(), "golden_beach");
	public static final BiomeKey PALM_BEACH = registerKey(ECCommonConfig.GENERATE_PALM_BEACH.get(), "palm_beach");

	//Desert
	public static final BiomeKey AZURE_DESERT = registerKey(ECCommonConfig.GENERATE_AZURE_DESERT.get(), "azure_desert");
	public static final BiomeKey JADEITE_DESERT = registerKey(ECCommonConfig.GENERATE_JADEITE_DESERT.get(), "jadeite_desert");

	//Underground
	public static final BiomeKey VOLCANIC_CAVES = registerKey(ECCommonConfig.GENERATE_VOLCANIC_CAVES.get(), "volcanic_caves");
	public static final BiomeKey MOSSY_CAVES = registerKey(ECCommonConfig.GENERATE_MOSSY_CAVES.get(), "mossy_caves");

//Nether
	public static final BiomeKey EMERY_DESERT = registerKey(ECCommonConfig.GENERATE_EMERY_DESERT.get(), "emery_desert");
	public static final BiomeKey QUARTZ_DESERT = registerKey(ECCommonConfig.GENERATE_QUARTZ_DESERT.get(), "quartz_desert");
	public static final BiomeKey PURPURACEUS_SWAMP = registerKey(ECCommonConfig.GENERATE_PURPURACEUS_SWAMP.get(), "purpuraceus_swamp");

	private static BiomeKey registerKey(boolean generate, String biomeName) {
		return new BiomeKey(ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, biomeName)), generate);
	}

	public record BiomeKey(ResourceKey<Biome> key, boolean generate) {}
}
