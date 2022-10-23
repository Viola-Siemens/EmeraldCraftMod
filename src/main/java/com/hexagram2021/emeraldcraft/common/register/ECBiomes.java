package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
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
	public static final Biome EMERY_DESERT = EmeryDesertBiome();
	public static final Biome QUARTZ_DESERT = QuartzDesertBiome();

	public static final Biome NETHER_GARDEN = NetherGarden();

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
		EMERY_DESERT.setRegistryName(MODID, "emery_desert");
		QUARTZ_DESERT.setRegistryName(MODID, "quartz_desert");
		NETHER_GARDEN.setRegistryName(MODID, "nether_garden");

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
		event.getRegistry().register(EMERY_DESERT);
		event.getRegistry().register(QUARTZ_DESERT);
		event.getRegistry().register(NETHER_GARDEN);

		registerBiomesToDictionary();
	}

	private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
		DefaultBiomeFeatures.addDefaultCarvers(builder);
		DefaultBiomeFeatures.addDefaultLakes(builder);
		DefaultBiomeFeatures.addDefaultMonsterRoom(builder);
		DefaultBiomeFeatures.addDefaultUndergroundVariety(builder);
		DefaultBiomeFeatures.addDefaultSprings(builder);
		DefaultBiomeFeatures.addSurfaceFreezing(builder);
	}

	private static void globalOverworldOceanGeneration(BiomeGenerationSettings.Builder builder) {
		DefaultBiomeFeatures.addOceanCarvers(builder);
		DefaultBiomeFeatures.addDefaultLakes(builder);
		DefaultBiomeFeatures.addDefaultMonsterRoom(builder);
		DefaultBiomeFeatures.addDefaultUndergroundVariety(builder);
		DefaultBiomeFeatures.addDefaultSprings(builder);
		DefaultBiomeFeatures.addSurfaceFreezing(builder);
	}

	public static void oceanSpawns(MobSpawnInfo.Builder builder, int squidWeight, int minCount, int codWeight) {
		builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(EntityType.SQUID, squidWeight, 1, minCount));
		builder.addSpawn(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(EntityType.COD, codWeight, 3, 6));
		DefaultBiomeFeatures.commonSpawns(builder);
	}

	private static Biome baseOcean(MobSpawnInfo.Builder mobSpawnSetting, int waterColor, int waterFogColor, int fogColor, BiomeGenerationSettings.Builder biomeGenerationSetting, boolean isDeep) {
		return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
				.biomeCategory(Biome.Category.OCEAN)
				.depth(isDeep ? -1.8F : -1.0F).scale(0.1F)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(waterColor).waterFogColor(waterFogColor)
						.fogColor(fogColor).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobSpawnSetting.build())
				.generationSettings(biomeGenerationSetting.build()).build();
	}

	private static BiomeGenerationSettings.Builder baseOceanGeneration() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder();
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
		DefaultBiomeFeatures.addDefaultOverworldOceanStructures(biomegenerationsettings$builder);
		globalOverworldOceanGeneration(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addWaterTrees(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
		return biomegenerationsettings$builder;
	}

	public static Biome DeadCrimsonOcean(boolean isDeep) {
		MobSpawnInfo.Builder mobSpawnSettingsBuilder = new MobSpawnInfo.Builder();
		if (isDeep) {
			oceanSpawns(mobSpawnSettingsBuilder, 8, 4, 8);
		} else {
			oceanSpawns(mobSpawnSettingsBuilder, 10, 2, 15);
		}
		
		mobSpawnSettingsBuilder.addSpawn(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(ECEntities.PURPLE_SPOTTED_BIGEYE.get(), 25, 8, 8));
		mobSpawnSettingsBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.DROWNED, 1, 1, 2));
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = baseOceanGeneration().surfaceBuilder(ECConfiguredSurfaceBuilders.DEAD_CRIMSON_OCEAN_BUILDER);
		biomegenerationsettings$builder.addFeature(
				GenerationStage.Decoration.VEGETAL_DECORATION,
				isDeep ? Features.SEAGRASS_DEEP_WARM : Features.SEAGRASS_WARM
		);
		if (isDeep) {
			DefaultBiomeFeatures.addDefaultSeagrass(biomegenerationsettings$builder);
		}

		DefaultBiomeFeatures.addLukeWarmKelp(biomegenerationsettings$builder);
		return baseOcean(mobSpawnSettingsBuilder, 0x804cd6, 0x370537, 0xd86064, biomegenerationsettings$builder, isDeep);
	}

	public static Biome DeadWarpedOcean(boolean isDeep) {
		MobSpawnInfo.Builder mobSpawnSettingsBuilder = new MobSpawnInfo.Builder();
		oceanSpawns(mobSpawnSettingsBuilder, 3, 4, 15);
		mobSpawnSettingsBuilder.addSpawn(EntityClassification.WATER_AMBIENT, new MobSpawnInfo.Spawners(ECEntities.HERRING.get(), 15, 1, 5));
		mobSpawnSettingsBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 1, 1, 2));
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = baseOceanGeneration().surfaceBuilder(ECConfiguredSurfaceBuilders.DEAD_WARPED_OCEAN_BUILDER);
		biomegenerationsettings$builder.addFeature(
				GenerationStage.Decoration.VEGETAL_DECORATION,
				isDeep ? Features.SEAGRASS_DEEP_COLD : Features.SEAGRASS_COLD
		);
		DefaultBiomeFeatures.addDefaultSeagrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addColdOceanExtraVegetation(biomegenerationsettings$builder);
		return baseOcean(mobSpawnSettingsBuilder, 0x1a9ed6, 0x052d41, 0x60d2d8, biomegenerationsettings$builder, isDeep);
	}

	private static Biome Xanadu() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.XANADU_BUILDER);
		MobSpawnInfo.Builder mobspawnsettings$builder = (new MobSpawnInfo.Builder())
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.DONKEY, 4, 1, 2))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.HORSE, 4, 1, 2))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.CHICKEN, 7, 2, 4))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.WOLF, 2, 1, 4))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.SHEEP, 4, 2, 4))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.MOOSHROOM, 1, 1, 2));
		DefaultBiomeFeatures.ambientSpawns(mobspawnsettings$builder);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		globalOverworldGeneration(biomegenerationsettings$builder);
		addXanaduDeltas(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addPlainGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		addXanaduVegetation(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addInfestedStone(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
				.biomeCategory(Biome.Category.MUSHROOM)
				.depth(0.15F).scale(0.3F)
				.temperature(1.0F).downfall(0.9F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome GinkgoForest() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.farmAnimals(mobspawnsettings$builder);
		mobspawnsettings$builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.WOLF, 8, 4, 4)).addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.RABBIT, 4, 2, 3)).addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.FOX, 8, 2, 4));
		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.GINKGO_FOREST_BUILDER);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
		globalOverworldGeneration(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addFerns(biomegenerationsettings$builder);
		addGinkgoTrees(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addBerryBushes(biomegenerationsettings$builder);

		return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
				.biomeCategory(Biome.Category.FOREST)
				.depth(0.1F).scale(0.4F)
				.temperature(0.5F).downfall(0.9F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533).grassColorOverride(0xfadc50).foliageColorOverride(0xfadc50)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome KarstHills() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.oceanSpawns(mobspawnsettings$builder, 8, 4, 8);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.KARST_HILLS_BUILDER);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_MOUNTAIN);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
		globalOverworldGeneration(biomegenerationsettings$builder);
		addKarstDeltas(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addInfestedStone(biomegenerationsettings$builder);

		return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
				.biomeCategory(Biome.Category.EXTREME_HILLS)
				.temperature(1.0F).downfall(0.3F)
				.depth(1.0F).scale(1.0F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4445678).waterFogColor(270131)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome PetuniaPlains() {
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
		MobSpawnInfo.Builder mobspawnsettings$builder = (new MobSpawnInfo.Builder())
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.DONKEY, 1, 1, 2))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.RABBIT, 2, 2, 6))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.SHEEP, 2, 2, 4));
		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.VILLAGE_PLAINS);
		globalOverworldGeneration(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addPlainGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		addPetuniaPlainsVegetation(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addInfestedStone(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
				.biomeCategory(Biome.Category.PLAINS)
				.temperature(0.5F).downfall(0.8F)
				.depth(0.125F).scale(0.1F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	public static Biome GoldenBeach() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();

		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.GOLDEN_BEACH_BUILDER);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.BURIED_TREASURE);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);

		globalOverworldGeneration(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);

		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.BEACH)
				.temperature(0.8F).downfall(0.4F)
				.depth(0.025F).scale(0.025F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	public static Biome PalmBeach() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();

		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.PALM_BEACH_BUILDER);
		DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.BURIED_TREASURE);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);

		globalOverworldGeneration(biomegenerationsettings$builder);
		addPalmTrees(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);

		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.BEACH)
				.temperature(0.8F).downfall(0.6F)
				.depth(0.05F).scale(0.025F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome AzureDesertBiome() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);

		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.AZURE_DESERT_BUILDER);

		DefaultBiomeFeatures.addDefaultOverworldLandMesaStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);

		DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDesertLakes(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		addExtraLapis(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		DefaultBiomeFeatures.addBadlandGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.MESA)
				.temperature(2.0F).downfall(0.0F)
				.depth(0.125F).scale(0.05F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome JadeiteDesertBiome() {
		MobSpawnInfo.Builder mobspawnsettings$builder = new MobSpawnInfo.Builder();
		DefaultBiomeFeatures.commonSpawns(mobspawnsettings$builder);
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder().surfaceBuilder(ECConfiguredSurfaceBuilders.JADEITE_DESERT_BUILDER);

		DefaultBiomeFeatures.addDefaultOverworldLandMesaStructures(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);

		DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDesertLakes(biomegenerationsettings$builder);
		addZombieVillagerRoom(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSprings(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

		DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

		DefaultBiomeFeatures.addBadlandGrass(biomegenerationsettings$builder);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);

		DefaultBiomeFeatures.addExtraEmeralds(biomegenerationsettings$builder);
		addExtraEmerald(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.MESA)
				.temperature(2.0F).downfall(0.0F)
				.depth(0.125F).scale(0.05F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobspawnsettings$builder.build())
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome EmeryDesertBiome() {
		MobSpawnInfo mobspawnsettings = (new MobSpawnInfo.Builder())
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.WITHER_SKELETON, 1, 5, 5))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.BLAZE, 50, 4, 4))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 20, 5, 5))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.STRIDER, 60, 1, 2))
				.addMobCharge(EntityType.WITHER_SKELETON, 0.7D, 0.15D)
				.addMobCharge(EntityType.BLAZE, 0.7D, 0.15D)
				.addMobCharge(EntityType.ZOMBIFIED_PIGLIN, 0.7D, 0.15D)
				.addMobCharge(EntityType.STRIDER, 0.7D, 0.15D)
				.build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder())
				.surfaceBuilder(ECConfiguredSurfaceBuilders.EMERY_DESERT_BUILDER)
				.addCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.NETHER_CAVE)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA)
				.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND)
				.addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER)
				.addStructureStart(StructureFeatures.NETHER_FOSSIL)
				.addStructureStart(StructureFeatures.NETHER_BRIDGE);

		DefaultBiomeFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addExtraAncientDebris(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.NETHER)
				.temperature(2.0F).downfall(0.0F)
				.depth(0.1F).scale(0.2F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(1787717).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new ParticleEffectAmbience(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(BackgroundMusicTracks.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome QuartzDesertBiome() {
		MobSpawnInfo mobspawnsettings = (new MobSpawnInfo.Builder())
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.SKELETON, 20, 5, 5))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.GHAST, 50, 4, 4))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 1, 4, 4))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.STRIDER, 60, 1, 2))
				.addMobCharge(EntityType.SKELETON, 0.7D, 0.15D)
				.addMobCharge(EntityType.GHAST, 0.7D, 0.15D)
				.addMobCharge(EntityType.ENDERMAN, 0.7D, 0.15D)
				.addMobCharge(EntityType.STRIDER, 0.7D, 0.15D)
				.build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder())
				.surfaceBuilder(ECConfiguredSurfaceBuilders.QUARTZ_DESERT_BUILDER)
				.addCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.NETHER_CAVE)
				.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND)
				.addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER)
				.addStructureStart(StructureFeatures.NETHER_FOSSIL)
				.addStructureStart(StructureFeatures.NETHER_BRIDGE)
				.addStructureStart(StructureFeatures.BASTION_REMNANT);
		DefaultBiomeFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		addExtraQuartz(biomegenerationsettings$builder);
		return (new Biome.Builder()).precipitation(Biome.RainType.NONE)
				.biomeCategory(Biome.Category.NETHER)
				.temperature(2.0F).downfall(0.0F)
				.depth(0.1F).scale(0.2F)
				.specialEffects((new BiomeAmbience.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12169636).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new ParticleEffectAmbience(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(BackgroundMusicTracks.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static Biome NetherGarden() {
		MobSpawnInfo mobspawninfo = new MobSpawnInfo.Builder()
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 2, 2, 4))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.HOGLIN, 1, 3, 4))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PIGLIN, 5, 3, 4))
				.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 2, 1, 2))
				.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.STRIDER, 10, 1, 2)).build();
		BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(ECConfiguredSurfaceBuilders.NETHER_GARDEN)
				.addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER)
				.addCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.NETHER_CAVE)
				.addStructureStart(StructureFeatures.NETHER_BRIDGE)
				.addStructureStart(StructureFeatures.BASTION_REMNANT)
				//.addStructureStart(StructureFeatures.STRONGHOLD)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
		DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
		biomegenerationsettings$builder.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
				.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.WEEPING_VINES)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.TWISTING_VINES)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FUNGI)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FOREST_VEGETATION)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.WARPED_FUNGI)
				.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.WARPED_FOREST_VEGETATION);
		DefaultBiomeFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
		return new Biome.Builder().precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.NETHER)
				.depth(0.1F).scale(0.2F).temperature(2.0F).downfall(0.0F)
				.specialEffects(new BiomeAmbience.Builder()
						.waterColor(4159204).waterFogColor(329011).fogColor(12169636).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new ParticleEffectAmbience(ParticleTypes.CRIMSON_SPORE, 0.025F))
						.ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
						.ambientMoodSound(new MoodSoundAmbience(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new SoundAdditionsAmbience(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111D))
						.backgroundMusic(BackgroundMusicTracks.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)).build())
				.mobSpawnSettings(mobspawninfo).generationSettings(biomegenerationsettings$builder.build()).build();
	}

	private static int calculateSkyColor(float temp) {
		float f = temp / 3.0F;
		f = MathHelper.clamp(f, -1.0F, 1.0F);
		return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

	public static void addXanaduVegetation(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ECConfiguredFeatures.TreeConfiguredFeatures.TREES_PEACH);
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ECConfiguredFeatures.VegetationFeatures.FLOWER_PETUNIA_PLAINS);
	}

	public static void addPetuniaPlainsVegetation(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ECConfiguredFeatures.VegetationFeatures.FLOWER_PETUNIA_PLAINS);
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.TREES_MOUNTAIN);
	}

	private static void addExtraLapis(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.OreConfiguredFeatures.ORE_LAPIS_EXTRA);
	}

	private static void addExtraEmerald(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ECConfiguredFeatures.OreConfiguredFeatures.ORE_EMERALD_EXTRA);
	}

	private static void addExtraAncientDebris(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ECConfiguredFeatures.OreConfiguredFeatures.ORE_DEBRIS_EXTRA);
	}

	private static void addExtraQuartz(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, ECConfiguredFeatures.OreConfiguredFeatures.ORE_QUARTZ_EXTRA);
	}

	private static void addZombieVillagerRoom(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ECConfiguredFeatures.StructureConfiguredFeatures.ZOMBIE_VILLAGER_ROOM);
	}

	private static void addXanaduDeltas(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ECConfiguredFeatures.SpecialFeatures.XANADU_DELTA);
	}

	private static void addKarstDeltas(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ECConfiguredFeatures.SpecialFeatures.KARST_DELTA);
	}

	private static void addGinkgoTrees(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ECConfiguredFeatures.TreeConfiguredFeatures.TREES_GINKGO);
	}

	private static void addPalmTrees(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ECConfiguredFeatures.TreeConfiguredFeatures.TREES_PALM);
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
				BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.PETUNIA_PLAINS,
				BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.RARE, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.GOLDEN_BEACH,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.PALM_BEACH,
				BiomeDictionary.Type.BEACH, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.AZURE_DESERT,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);
		addBiome(ECBiomeKeys.JADEITE_DESERT,
				BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY, BiomeDictionary.Type.OVERWORLD);

		addBiome(ECBiomeKeys.EMERY_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.RARE, BiomeDictionary.Type.NETHER);
		addBiome(ECBiomeKeys.QUARTZ_DESERT,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.NETHER);

		addBiome(ECBiomeKeys.NETHER_GARDEN,
				BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.NETHER);
	}

	private static void addBiome(RegistryKey<Biome> biomeKey, BiomeDictionary.Type... types) {
		BiomeDictionary.addTypes(biomeKey, types);
	}
}
