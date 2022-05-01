package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomes {
	public static final Biome AZURE_DESERT = AzureDesertBiome();
	public static final Biome JADEITE_DESERT = JadeiteDesertBiome();

	public static void init(RegistryEvent.Register<Biome> event) {
		AZURE_DESERT.setRegistryName(MODID, "azure_desert");
		JADEITE_DESERT.setRegistryName(MODID, "jadeite_desert");

		event.getRegistry().register(AZURE_DESERT);
		event.getRegistry().register(JADEITE_DESERT);
	}

	private static Biome AzureDesertBiome() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		mobspawnsettings$builder.setPlayerCanSpawn();
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ECConfiguredSurfaceBuilder.AZURE_DESERT_BUILDER);
		BiomeDefaultFeatures.addDefaultOverworldLandMesaStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		BiomeDefaultFeatures.addDefaultCarvers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultLakes(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		addExtraLapis(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addBadlandGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		//BiomeDefaultFeatures.addBadlandExtraVegetation(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSprings(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.MESA)
				.depth(0.1F).scale(0.2F)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome JadeiteDesertBiome() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		mobspawnsettings$builder.setPlayerCanSpawn();
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ECConfiguredSurfaceBuilder.JADEITE_DESERT_BUILDER);
		BiomeDefaultFeatures.addDefaultOverworldLandMesaStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		BiomeDefaultFeatures.addDefaultCarvers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultLakes(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomegenerationsettings$builder);
		addZombieVillagerRoom(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		addExtraEmerald(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addBadlandGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		//BiomeDefaultFeatures.addBadlandExtraVegetation(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSprings(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.MESA)
				.depth(0.1F).scale(0.2F)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}


	private static int calculateSkyColor(float temp) {
		float f = temp / 3.0F;
		f = Mth.clamp(f, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

	private static void addExtraLapis(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.ORE_LAPIS_EXTRA);
	}

	private static void addExtraEmerald(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.ORE_EMERALD_EXTRA);
	}

	public static void addZombieVillagerRoom(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ECConfiguredFeatures.ZOMBIE_VILLAGER_ROOM);
	}

	public static void registerBiomes() {
		addBiome(ECBiomes.AZURE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomes.JADEITE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
	}

	private static void addBiome(Biome biome, BiomeManager.BiomeType type, int weight, BiomeDictionary.Type... types) {
		final ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome)));

		BiomeDictionary.addTypes(biomeKey, types);
		BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(biomeKey, weight));
	}
}
