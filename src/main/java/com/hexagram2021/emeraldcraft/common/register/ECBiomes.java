package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.*;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECBiomes {
	public static final Biome DEAD_CRIMSON_OCEAN = DeadCrimsonOcean(false);
	public static final Biome DEAD_WARPED_OCEAN = DeadWarpedOcean(false);
	public static final Biome DEEP_DEAD_CRIMSON_OCEAN = DeadCrimsonOcean(true);
	public static final Biome DEEP_DEAD_WARPED_OCEAN = DeadWarpedOcean(true);
	public static final Biome XANADU = Xanadu();
	public static final Biome GINKGO_FOREST = GinkgoForest();
	public static final Biome KARST_HILLS = KarstHills();
	public static final Biome PETUNIA_PLAINS = PetuniaPlains();
	public static final Biome GOLDEN_BEACH = GoldenBeach();
	public static final Biome PALM_BEACH = PalmBeach();
	public static final Biome AZURE_DESERT = AzureDesertBiome();
	public static final Biome JADEITE_DESERT = JadeiteDesertBiome();
	public static final Biome VOLCANIC_CAVES = VolcanicCaves();
	public static final Biome EMERY_DESERT = EmeryDesertBiome();
	public static final Biome QUARTZ_DESERT = QuartzDesertBiome();
	public static final Biome PURPURACEUS_SWAMP = PurpuraceusSwamp();

	public static void init(RegistryEvent.Register<Biome> event) {
		DEAD_CRIMSON_OCEAN.setRegistryName(MODID, "dead_crimson_ocean");
		DEAD_WARPED_OCEAN.setRegistryName(MODID, "dead_warped_ocean");
		DEEP_DEAD_CRIMSON_OCEAN.setRegistryName(MODID, "deep_dead_crimson_ocean");
		DEEP_DEAD_WARPED_OCEAN.setRegistryName(MODID, "deep_dead_warped_ocean");
		XANADU.setRegistryName(MODID, "xanadu");
		GINKGO_FOREST.setRegistryName(MODID, "ginkgo_forest");
		KARST_HILLS.setRegistryName(MODID, "karst_hills");
		PETUNIA_PLAINS.setRegistryName(MODID, "petunia_plains");
		GOLDEN_BEACH.setRegistryName(MODID, "golden_beach");
		PALM_BEACH.setRegistryName(MODID, "palm_beach");
		AZURE_DESERT.setRegistryName(MODID, "azure_desert");
		JADEITE_DESERT.setRegistryName(MODID, "jadeite_desert");
		VOLCANIC_CAVES.setRegistryName(MODID, "volcanic_caves");
		EMERY_DESERT.setRegistryName(MODID, "emery_desert");
		QUARTZ_DESERT.setRegistryName(MODID, "quartz_desert");
		PURPURACEUS_SWAMP.setRegistryName(MODID, "purpuraceus_swamp");

		event.getRegistry().register(DEAD_CRIMSON_OCEAN);
		event.getRegistry().register(DEAD_WARPED_OCEAN);
		event.getRegistry().register(DEEP_DEAD_CRIMSON_OCEAN);
		event.getRegistry().register(DEEP_DEAD_WARPED_OCEAN);
		event.getRegistry().register(XANADU);
		event.getRegistry().register(GINKGO_FOREST);
		event.getRegistry().register(KARST_HILLS);
		event.getRegistry().register(PETUNIA_PLAINS);
		event.getRegistry().register(GOLDEN_BEACH);
		event.getRegistry().register(PALM_BEACH);
		event.getRegistry().register(AZURE_DESERT);
		event.getRegistry().register(JADEITE_DESERT);
		event.getRegistry().register(VOLCANIC_CAVES);
		event.getRegistry().register(EMERY_DESERT);
		event.getRegistry().register(QUARTZ_DESERT);
		event.getRegistry().register(PURPURACEUS_SWAMP);

		registerBiomesToDictionary();
	}

	private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
		BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
		BiomeDefaultFeatures.addDefaultSprings(builder);
		BiomeDefaultFeatures.addSurfaceFreezing(builder);
	}

	public static void oceanSpawns(MobSpawnSettings.Builder builder, int squidWeight, int minCount, int maxCount) {
		builder.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, squidWeight, 1, minCount));
		builder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, maxCount, 3, 6));
		BiomeDefaultFeatures.caveSpawns(builder);
	}

	private static Biome baseOcean(MobSpawnSettings.Builder mobSpawnSetting, int waterColor, int waterFogColor, int fogColor, BiomeGenerationSettings.Builder biomeGenerationSetting) {
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.OCEAN)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(waterColor).waterFogColor(waterFogColor)
						.fogColor(fogColor).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobSpawnSetting.build())
				.generationSettings(biomeGenerationSetting.build()).build();
	}

	private static BiomeGenerationSettings.Builder baseOceanGeneration() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addWaterTrees(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
		return biomegenerationsettings$builder;
	}

	public static Biome DeadCrimsonOcean(boolean isDeep) {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		if (isDeep) {
			oceanSpawns(mobspawnsettings$builder, 8, 4, 8);
		} else {
			oceanSpawns(mobspawnsettings$builder, 10, 2, 15);
		}

		mobspawnsettings$builder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ECEntities.PURPLE_SPOTTED_BIGEYE.get(), 25, 8, 8));
		mobspawnsettings$builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1, 2));
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = baseOceanGeneration();
		biomegenerationsettings$builder.addFeature(
				GenerationStep.Decoration.VEGETAL_DECORATION,
				isDeep ? AquaticPlacements.SEAGRASS_DEEP_WARM : AquaticPlacements.SEAGRASS_WARM
		);
		if (isDeep) {
			BiomeDefaultFeatures.addDefaultSeagrass(biomegenerationsettings$builder);
		}

		BiomeDefaultFeatures.addLukeWarmKelp(biomegenerationsettings$builder);
		return baseOcean(mobspawnsettings$builder, 0x804cd6, 0x370537, 0xd86064, biomegenerationsettings$builder);
	}

	public static Biome DeadWarpedOcean(boolean isDeep) {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		oceanSpawns(mobspawnsettings$builder, 3, 4, 15);
		mobspawnsettings$builder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ECEntities.HERRING.get(), 15, 1, 5));
		mobspawnsettings$builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2));
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = baseOceanGeneration();
		biomegenerationsettings$builder.addFeature(
				GenerationStep.Decoration.VEGETAL_DECORATION,
				isDeep ? AquaticPlacements.SEAGRASS_DEEP_COLD : AquaticPlacements.SEAGRASS_COLD
		);
		BiomeDefaultFeatures.addDefaultSeagrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addColdOceanExtraVegetation(biomegenerationsettings$builder);
		return baseOcean(mobspawnsettings$builder, 0x1a9ed6, 0x052d41, 0x60d2d8, biomegenerationsettings$builder);
	}

	private static Biome Xanadu() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		MobSpawnSettings.Builder mobspawnsettings$builder = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 4, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 4, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 7, 2, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 1, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 4, 2, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 1, 1, 2));
		BiomeDefaultFeatures.caveSpawns(mobspawnsettings$builder);
		globalOverworldGeneration(biomegenerationsettings$builder);
		addXanaduDeltas(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addPlainGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		addXanaduVegetation(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addInfestedStone(biomegenerationsettings$builder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.MOUNTAIN)
				.temperature(1.0F).downfall(0.9F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome GinkgoForest() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.farmAnimals(mobspawnsettings$builder);
		mobspawnsettings$builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addFerns(biomegenerationsettings$builder);
		addGinkgoTrees(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addCommonBerryBushes(biomegenerationsettings$builder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.FOREST)
				.temperature(0.5F).downfall(0.9F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533).grassColorOverride(0xfadc50).foliageColorOverride(0xfadc50)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome KarstHills() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.oceanSpawns(mobspawnsettings$builder, 8, 4, 8);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		addKarstDeltas(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addInfestedStone(biomegenerationsettings$builder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_STONY_PEAKS);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.FOREST)
				.temperature(1.0F).downfall(0.3F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4445678).waterFogColor(270131)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome PetuniaPlains() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		MobSpawnSettings.Builder mobspawnsettings$builder = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 2, 6))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 2, 4));
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		globalOverworldGeneration(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addPlainGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		addPetuniaPlainsVegetation(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addInfestedStone(biomegenerationsettings$builder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.MOUNTAIN)
				.temperature(0.5F).downfall(0.8F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	public static Biome GoldenBeach() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();

		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.BEACH)
				.temperature(0.8F).downfall(0.4F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	public static Biome PalmBeach() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();

		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		addPalmTrees(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.BEACH)
				.temperature(0.8F).downfall(0.6F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome AzureDesertBiome() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();

		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);

		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		addExtraLapis(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addBadlandGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.MESA)
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
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();

		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomegenerationsettings$builder);
		addZombieVillagerRoom(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSprings(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addBadlandGrass(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);

		BiomeDefaultFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		addExtraEmerald(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.MESA)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	public static Biome VolcanicCaves() {
		MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
		mobspawnsettings$builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 10, 3, 8));
		BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		addVolcanicCavesFeatures(biomegenerationsettings$builder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.biomeCategory(Biome.BiomeCategory.UNDERGROUND)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.LAVA, 0.00625F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
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
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);

		BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addExtraAncientDebris(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NETHER)
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
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);
		BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addExtraQuartz(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NETHER)
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

	private static Biome PurpuraceusSwamp() {
		MobSpawnSettings mobspawnsettings = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 5, 4, 4))
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 15, 4, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
				.build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder())
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA)
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.PURPURACEUS_FUNGI)
				.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.PURPURACEUS_SWAMP_VEGETATION)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);

		BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addPurpuraceusDeltas(biomegenerationsettings$builder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NETHER)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(0x973e97).skyColor(calculateSkyColor(2.0F))
						.ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
						.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
						.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}


	private static int calculateSkyColor(float temp) {
		float f = temp / 3.0F;
		f = Mth.clamp(f, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

	public static void addXanaduVegetation(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.TREES_PEACH);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.FLOWER_PETUNIA_PLAINS);
	}

	public static void addPetuniaPlainsVegetation(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.FLOWER_PETUNIA_PLAINS);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_MEADOW);
	}

	private static void addExtraLapis(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECPlacedFeatures.ORE_LAPIS_EXTRA);
	}

	private static void addExtraEmerald(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ECPlacedFeatures.ORE_EMERALD_EXTRA);
	}

	private static void addExtraAncientDebris(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ECPlacedFeatures.ORE_DEBRIS_EXTRA);
	}

	private static void addExtraQuartz(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ECPlacedFeatures.ORE_QUARTZ_EXTRA);
	}

	private static void addZombieVillagerRoom(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ECPlacedFeatures.ZOMBIE_VILLAGER_ROOM);
	}

	private static void addVolcanicCavesFeatures(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.VOLCANIC_CAVES_LAVA_POOL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
	}

	private static void addXanaduDeltas(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, ECPlacedFeatures.XANADU_DELTA);
	}

	private static void addKarstDeltas(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, ECPlacedFeatures.KARST_DELTA);
	}

	private static void addGinkgoTrees(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.TREES_GINKGO);
	}

	private static void addPalmTrees(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ECPlacedFeatures.TREES_PALM);
	}

	private static void addPurpuraceusDeltas(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, ECPlacedFeatures.PURPURACEUS_SWAMP_DELTA);
		builder.addFeature(GenerationStep.Decoration.RAW_GENERATION, ECPlacedFeatures.PURPURACEUS_SWAMP_LAVA_DELTA);
	}

	public static void registerBiomesToDictionary() {
		addBiome(ECBiomeKeys.DEAD_CRIMSON_OCEAN,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.DEAD_WARPED_OCEAN,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.DEEP_DEAD_CRIMSON_OCEAN,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.DEEP_DEAD_WARPED_OCEAN,
				BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.XANADU,
				BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.RARE, BiomeDictionary.Type.WET, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.GINKGO_FOREST,
				BiomeDictionary.Type.FOREST, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.KARST_HILLS,
				BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.PEAK, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.PETUNIA_PLAINS,
				BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.GOLDEN_BEACH,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.PALM_BEACH,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.VOLCANIC_CAVES,
				BiomeDictionary.Type.UNDERGROUND, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.AZURE_DESERT,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.JADEITE_DESERT,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.EMERY_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.RARE, BiomeDictionary.Type.NETHER);
		addBiome(ECBiomeKeys.QUARTZ_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);
		addBiome(ECBiomeKeys.PURPURACEUS_SWAMP,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.RARE, BiomeDictionary.Type.NETHER);
	}

	private static void addBiome(ResourceKey<Biome> biomeKey, BiomeDictionary.Type... types) {
		BiomeDictionary.addTypes(biomeKey, types);
	}
}
