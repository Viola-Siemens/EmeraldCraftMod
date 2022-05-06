package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.mixin.MultiNoiseBiomeSourceAccess;
import com.hexagram2021.emeraldcraft.mixin.NetherBiomesAccess;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomes {
	public static final Biome AZURE_DESERT = AzureDesertBiome();
	public static final Biome JADEITE_DESERT = JadeiteDesertBiome();
	public static final Biome EMERY_DESERT = EmeryDesertBiome();
	public static final Biome QUARTZ_DESERT = QuartzDesertBiome();

	public static void init(RegistryEvent.Register<Biome> event) {
		AZURE_DESERT.setRegistryName(MODID, "azure_desert");
		JADEITE_DESERT.setRegistryName(MODID, "jadeite_desert");
		EMERY_DESERT.setRegistryName(MODID, "emery_desert");
		QUARTZ_DESERT.setRegistryName(MODID, "quartz_desert");

		event.getRegistry().register(AZURE_DESERT);
		event.getRegistry().register(JADEITE_DESERT);
		event.getRegistry().register(EMERY_DESERT);
		event.getRegistry().register(QUARTZ_DESERT);
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

	private static Biome EmeryDesertBiome() {
		MobSpawnSettings mobspawnsettings = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 1, 5, 5))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 50, 4, 4))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 20, 5, 5))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
				.addMobCharge(EntityType.WITHER_SKELETON, 0.7D, 0.15D)
				.addMobCharge(EntityType.BLAZE, 0.7D, 0.15D)
				.addMobCharge(EntityType.ZOMBIFIED_PIGLIN, 0.7D, 0.15D)
				.addMobCharge(EntityType.STRIDER, 0.7D, 0.15D)
				.build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder())
				.surfaceBuilder(ECConfiguredSurfaceBuilder.EMERY_DESERT_BUILDER)
				.addStructureStart(StructureFeatures.NETHER_BRIDGE)
				.addStructureStart(StructureFeatures.NETHER_FOSSIL)
				.addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER)
				.addStructureStart(StructureFeatures.BASTION_REMNANT)
				.addStructureStart(ECConfiguredStructures.NETHER_WARFIELD)
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA)
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND);
		BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addExtraAncientDebris(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NETHER)
				.depth(0.125F).scale(0.05F)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(1787717).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome QuartzDesertBiome() {
		MobSpawnSettings mobspawnsettings = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 5, 5))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
				.addMobCharge(EntityType.SKELETON, 0.7D, 0.15D)
				.addMobCharge(EntityType.GHAST, 0.7D, 0.15D)
				.addMobCharge(EntityType.ENDERMAN, 0.7D, 0.15D)
				.addMobCharge(EntityType.STRIDER, 0.7D, 0.15D)
				.build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder())
				.surfaceBuilder(ECConfiguredSurfaceBuilder.QUARTZ_DESERT_BUILDER)
				.addStructureStart(StructureFeatures.NETHER_BRIDGE)
				.addStructureStart(StructureFeatures.NETHER_FOSSIL)
				.addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER)
				.addStructureStart(StructureFeatures.BASTION_REMNANT)
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND);
		addExtraQuartz(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NETHER)
				.depth(0.125F).scale(0.05F)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12169636).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
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

	private static void addExtraAncientDebris(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.ORE_DEBRIS_EXTRA);
	}

	private static void addExtraQuartz(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.ORE_QUARTZ_EXTRA);
	}

	public static void addZombieVillagerRoom(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ECConfiguredFeatures.ZOMBIE_VILLAGER_ROOM);
	}

	public static void registerBiomes() {
		addBiome(ECBiomes.AZURE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomes.JADEITE_DESERT, BiomeManager.BiomeType.DESERT, 5,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		ResourceKey<Biome> EMERY_DESERT = addNetherBiome(ECBiomes.EMERY_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);
		ResourceKey<Biome> QUARTZ_DESERT = addNetherBiome(ECBiomes.QUARTZ_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);

		NetherBiomesAccess.setNETHER(new MultiNoiseBiomeSource.Preset(
				new ResourceLocation("nether"),
				(p_48524_, p_48525_, p_48526_) -> MultiNoiseBiomeSourceAccess.construct(p_48526_, ImmutableList.of(
						Pair.of(new Biome.ClimateParameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
								() -> p_48525_.getOrThrow(Biomes.NETHER_WASTES)),
						Pair.of(new Biome.ClimateParameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F),
								() -> p_48525_.getOrThrow(Biomes.SOUL_SAND_VALLEY)),
						Pair.of(new Biome.ClimateParameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F),
								() -> p_48525_.getOrThrow(Biomes.CRIMSON_FOREST)),
						Pair.of(new Biome.ClimateParameters(0.0F, 0.5F, 0.0F, 0.0F, 0.375F),
								() -> p_48525_.getOrThrow(Biomes.WARPED_FOREST)),
						Pair.of(new Biome.ClimateParameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.175F),
								() -> p_48525_.getOrThrow(Biomes.BASALT_DELTAS)),
						Pair.of(new Biome.ClimateParameters(-0.8F, -0.8F, 0.0F, 0.0F, 0.175F),
								() -> p_48525_.getOrThrow(EMERY_DESERT)),
						Pair.of(new Biome.ClimateParameters(0.75F, 0.7F, 0.0F, 0.0F, 0.175F),
								() -> p_48525_.getOrThrow(QUARTZ_DESERT))
				), Optional.of(Pair.of(p_48525_, p_48524_)))
		));
	}

	private static void addBiome(Biome biome, BiomeManager.BiomeType type, int weight, BiomeDictionary.Type... types) {
		final ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome)));

		BiomeDictionary.addTypes(biomeKey, types);
		BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(biomeKey, weight));
	}

	private static ResourceKey<Biome> addNetherBiome(Biome biome, BiomeDictionary.Type... types) {
		final ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome)));
		BiomeDictionary.addTypes(biomeKey, types);

		return biomeKey;
	}
}
