package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.mixin.MultiNoiseBiomeSourceAccess;
import com.hexagram2021.emeraldcraft.mixin.NetherBiomesAccess;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.NetherBiomeProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomeKeys {
//Overworld
	//Ocean
	public static final RegistryKey<Biome> DEAD_CRIMSON_OCEAN = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "dead_crimson_ocean"));
	public static final RegistryKey<Biome> DEAD_WARPED_OCEAN = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "dead_warped_ocean"));

	public static final RegistryKey<Biome> DEEP_DEAD_CRIMSON_OCEAN = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "deep_dead_crimson_ocean"));
	public static final RegistryKey<Biome> DEEP_DEAD_WARPED_OCEAN = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "deep_dead_warped_ocean"));

	//Island
	public static final RegistryKey<Biome> XANADU = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "xanadu"));

	//Forest
	public static final RegistryKey<Biome> GINKGO_FOREST = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "ginkgo_forest"));

	//Hills
	public static final RegistryKey<Biome> KARST_HILLS = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "karst_hills"));

	//Plain
	public static final RegistryKey<Biome> PETUNIA_PLAINS = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "petunia_plains"));

	//Beach
	public static final RegistryKey<Biome> GOLDEN_BEACH = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "golden_beach"));
	public static final RegistryKey<Biome> PALM_BEACH = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "palm_beach"));

	//Desert
	public static final RegistryKey<Biome> AZURE_DESERT = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "azure_desert"));
	public static final RegistryKey<Biome> JADEITE_DESERT = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "jadeite_desert"));

	//Underground
	//public static final RegistryKey<Biome> VOLCANIC_CAVES = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "volcanic_caves"));

//Nether
	public static final RegistryKey<Biome> EMERY_DESERT = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "emery_desert"));
	public static final RegistryKey<Biome> QUARTZ_DESERT = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "quartz_desert"));

//Test Only
	public static final RegistryKey<Biome> NETHER_GARDEN = RegistryKey.create(ForgeRegistries.Keys.BIOMES, new ResourceLocation(MODID, "nether_garden"));

	public static void registerBiomes() {
		addBiome(DEAD_CRIMSON_OCEAN, BiomeManager.BiomeType.WARM, 5,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.OVERWORLD);
		addBiome(DEAD_WARPED_OCEAN, BiomeManager.BiomeType.COOL, 5,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.OVERWORLD);
		addBiome(DEEP_DEAD_CRIMSON_OCEAN, BiomeManager.BiomeType.WARM, 5,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.OVERWORLD);
		addBiome(DEEP_DEAD_WARPED_OCEAN, BiomeManager.BiomeType.COOL, 5,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.OVERWORLD);
		addBiome(XANADU, BiomeManager.BiomeType.WARM, 1,
				BiomeDictionary.Type.WET, BiomeDictionary.Type.RARE, BiomeDictionary.Type.OVERWORLD);
		addBiome(GINKGO_FOREST, BiomeManager.BiomeType.COOL, 10,
				BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.OVERWORLD);
		addBiome(KARST_HILLS, BiomeManager.BiomeType.COOL, 10,
				BiomeDictionary.Type.HILLS, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.OVERWORLD);
		addBiome(PETUNIA_PLAINS, BiomeManager.BiomeType.COOL, 10,
				BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.OVERWORLD);
		addBiome(GOLDEN_BEACH, BiomeManager.BiomeType.WARM, 5,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.OVERWORLD);
		addBiome(PALM_BEACH, BiomeManager.BiomeType.COOL, 10,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.OVERWORLD);
		addBiome(AZURE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addBiome(JADEITE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addNetherBiome(EMERY_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);
		addNetherBiome(QUARTZ_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);

		addNetherBiome(NETHER_GARDEN,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.NETHER);

		NetherBiomesAccess.setNETHER(new NetherBiomeProvider.Preset(
				new ResourceLocation("nether"),
				(preset, registry, v) -> MultiNoiseBiomeSourceAccess.construct(v, ImmutableList.of(
						Pair.of(new Biome.Attributes(0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
								() -> registry.getOrThrow(Biomes.NETHER_WASTES)),
						Pair.of(new Biome.Attributes(0.0F, -0.5F, 0.0F, 0.0F, 0.0F),
								() -> registry.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
						Pair.of(new Biome.Attributes(0.4F, 0.0F, 0.0F, 0.0F, 0.0F),
								() -> registry.getOrThrow(Biomes.CRIMSON_FOREST)),
						Pair.of(new Biome.Attributes(0.0F, 0.5F, 0.0F, 0.0F, 0.375F),
								() -> registry.getOrThrow(Biomes.WARPED_FOREST)),
						Pair.of(new Biome.Attributes(-0.5F, 0.0F, 0.0F, 0.0F, 0.175F),
								() -> registry.getOrThrow(Biomes.BASALT_DELTAS)),
						Pair.of(new Biome.Attributes(-0.8F, -0.8F, 0.0F, 0.0F, 0.175F),
								() -> registry.getOrThrow(EMERY_DESERT)),
						Pair.of(new Biome.Attributes(0.75F, 0.7F, 0.0F, 0.0F, 0.175F),
								() -> registry.getOrThrow(QUARTZ_DESERT))
				), Optional.of(Pair.of(registry, preset)))
		));
	}

	private static void addBiome(RegistryKey<Biome> biomeKey, BiomeManager.BiomeType type, int weight, BiomeDictionary.Type... types) {
		BiomeDictionary.addTypes(biomeKey, types);
		BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(biomeKey, weight));
	}

	private static void addNetherBiome(RegistryKey<Biome> biomeKey, BiomeDictionary.Type... types) {
		BiomeDictionary.addTypes(biomeKey, types);
	}
}
