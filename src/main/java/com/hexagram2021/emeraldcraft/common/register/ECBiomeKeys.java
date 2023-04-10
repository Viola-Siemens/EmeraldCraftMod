package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeKeys {
//Overworld
	//Ocean
	public static final BiomeKey DEAD_CRIMSON_OCEAN = registerKey(
			ECCommonConfig.GENERATE_DEAD_CRIMSON_OCEAN.get(), ECCommonConfig.SUPPRESS_DEAD_CRIMSON_OCEAN.get(), "dead_crimson_ocean"
	);
	public static final BiomeKey DEAD_WARPED_OCEAN = registerKey(
			ECCommonConfig.GENERATE_DEAD_WARPED_OCEAN.get(), ECCommonConfig.SUPPRESS_DEAD_WARPED_OCEAN.get(), "dead_warped_ocean"
	);

	public static final BiomeKey DEEP_DEAD_CRIMSON_OCEAN = registerKey(
			ECCommonConfig.GENERATE_DEAD_CRIMSON_OCEAN.get(), ECCommonConfig.SUPPRESS_DEAD_CRIMSON_OCEAN.get(), "deep_dead_crimson_ocean"
	);
	public static final BiomeKey DEEP_DEAD_WARPED_OCEAN = registerKey(
			ECCommonConfig.GENERATE_DEAD_WARPED_OCEAN.get(), ECCommonConfig.SUPPRESS_DEAD_WARPED_OCEAN.get(), "deep_dead_warped_ocean"
	);

	//Island
	public static final BiomeKey XANADU = registerKey(
			ECCommonConfig.GENERATE_XANADU.get(), ECCommonConfig.SUPPRESS_XANADU.get(), "xanadu"
	);

	//Forest
	public static final BiomeKey GINKGO_FOREST = registerKey(
			ECCommonConfig.GENERATE_GINKGO_FOREST.get(), ECCommonConfig.SUPPRESS_GINKGO_FOREST.get(), "ginkgo_forest"
	);

	//Hills
	public static final BiomeKey KARST_HILLS = registerKey(
			ECCommonConfig.GENERATE_KARST_HILLS.get(), ECCommonConfig.SUPPRESS_KARST_HILLS.get(), "karst_hills"
	);

	//Plain
	public static final BiomeKey PETUNIA_PLAINS = registerKey(
			ECCommonConfig.GENERATE_PETUNIA_PLAINS.get(), ECCommonConfig.SUPPRESS_PETUNIA_PLAINS.get(), "petunia_plains"
	);

	//Beach
	public static final BiomeKey GOLDEN_BEACH = registerKey(
			ECCommonConfig.GENERATE_GOLDEN_BEACH.get(), ECCommonConfig.SUPPRESS_GOLDEN_BEACH.get(), "golden_beach"
	);
	public static final BiomeKey PALM_BEACH = registerKey(
			ECCommonConfig.GENERATE_PALM_BEACH.get(), ECCommonConfig.SUPPRESS_PALM_BEACH.get(), "palm_beach"
	);

	//Desert
	public static final BiomeKey AZURE_DESERT = registerKey(
			ECCommonConfig.GENERATE_AZURE_DESERT.get(), ECCommonConfig.SUPPRESS_AZURE_DESERT.get(), "azure_desert"
	);
	public static final BiomeKey JADEITE_DESERT = registerKey(
			ECCommonConfig.GENERATE_JADEITE_DESERT.get(), ECCommonConfig.SUPPRESS_JADEITE_DESERT.get(), "jadeite_desert"
	);

	//Underground
	public static final BiomeKey VOLCANIC_CAVES = registerKey(
			ECCommonConfig.GENERATE_VOLCANIC_CAVES.get(), ECCommonConfig.SUPPRESS_VOLCANIC_CAVES.get(), "volcanic_caves"
	);
	public static final BiomeKey MOSSY_CAVES = registerKey(
			ECCommonConfig.GENERATE_MOSSY_CAVES.get(), ECCommonConfig.SUPPRESS_MOSSY_CAVES.get(), "mossy_caves"
	);

//Nether
	public static final BiomeKey EMERY_DESERT = registerKey(
			ECCommonConfig.GENERATE_EMERY_DESERT.get(), ECCommonConfig.SUPPRESS_EMERY_DESERT.get(), "emery_desert"
	);
	public static final BiomeKey QUARTZ_DESERT = registerKey(
			ECCommonConfig.GENERATE_QUARTZ_DESERT.get(), ECCommonConfig.SUPPRESS_QUARTZ_DESERT.get(), "quartz_desert"
	);
	public static final BiomeKey PURPURACEUS_SWAMP = registerKey(
			ECCommonConfig.GENERATE_PURPURACEUS_SWAMP.get(), ECCommonConfig.SUPPRESS_PURPURACEUS_SWAMP.get(), "purpuraceus_swamp"
	);

	private static BiomeKey registerKey(boolean generate, double suppress, String biomeName) {
		return new BiomeKey(ResourceKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, biomeName)), generate, (float)suppress);
	}

	public record BiomeKey(ResourceKey<Biome> key, boolean generate, float suppress) {}

	public static final Map<ResourceKey<Biome>, BiomeKey> ALL_BIOME_KEYS = Maps.newHashMap();

	private static void putKey(BiomeKey biomeKey) {
		ALL_BIOME_KEYS.put(biomeKey.key(), biomeKey);
	}

	@Nullable
	public static BiomeKey getBiomeKey(ResourceKey<Biome> key) {
		return ALL_BIOME_KEYS.get(key);
	}

	public static float getSuppress(ResourceKey<Biome> key) {
		return getSuppress(key, 0.0F);
	}

	public static float getSuppress(ResourceKey<Biome> key, float defaultValue) {
		BiomeKey biomeKey = getBiomeKey(key);
		return biomeKey == null ? defaultValue : biomeKey.suppress();
	}

	static {
		putKey(DEAD_CRIMSON_OCEAN);
		putKey(DEAD_WARPED_OCEAN);
		putKey(DEEP_DEAD_CRIMSON_OCEAN);
		putKey(DEEP_DEAD_WARPED_OCEAN);
		putKey(XANADU);
		putKey(GINKGO_FOREST);
		putKey(KARST_HILLS);
		putKey(PETUNIA_PLAINS);
		putKey(GOLDEN_BEACH);
		putKey(PALM_BEACH);
		putKey(AZURE_DESERT);
		putKey(JADEITE_DESERT);
		putKey(VOLCANIC_CAVES);
		putKey(MOSSY_CAVES);
		putKey(EMERY_DESERT);
		putKey(QUARTZ_DESERT);
		putKey(PURPURACEUS_SWAMP);
	}
}
