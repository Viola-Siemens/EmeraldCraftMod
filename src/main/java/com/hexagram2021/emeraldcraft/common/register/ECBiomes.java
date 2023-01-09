package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECBiomes {
	private static final DeferredRegister<Biome> REGISTER = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);

	public static final RegistryObject<Biome> DEAD_CRIMSON_OCEAN = REGISTER.register("dead_crimson_ocean", () -> DeadCrimsonOcean(false));
	public static final RegistryObject<Biome> DEAD_WARPED_OCEAN = REGISTER.register("dead_warped_ocean", () -> DeadWarpedOcean(false));
	public static final RegistryObject<Biome> DEEP_DEAD_CRIMSON_OCEAN = REGISTER.register("deep_dead_crimson_ocean", () -> DeadCrimsonOcean(true));
	public static final RegistryObject<Biome> DEEP_DEAD_WARPED_OCEAN = REGISTER.register("deep_dead_warped_ocean", () -> DeadWarpedOcean(true));
	public static final RegistryObject<Biome> XANADU = REGISTER.register("xanadu", ECBiomes::Xanadu);
	public static final RegistryObject<Biome> GINKGO_FOREST = REGISTER.register("ginkgo_forest", ECBiomes::GinkgoForest);
	public static final RegistryObject<Biome> KARST_HILLS = REGISTER.register("karst_hills", ECBiomes::KarstHills);
	public static final RegistryObject<Biome> PETUNIA_PLAINS = REGISTER.register("petunia_plains", ECBiomes::PetuniaPlains);
	public static final RegistryObject<Biome> GOLDEN_BEACH = REGISTER.register("golden_beach", ECBiomes::GoldenBeach);
	public static final RegistryObject<Biome> PALM_BEACH = REGISTER.register("palm_beach", ECBiomes::PalmBeach);
	public static final RegistryObject<Biome> AZURE_DESERT = REGISTER.register("azure_desert", ECBiomes::AzureDesertBiome);
	public static final RegistryObject<Biome> JADEITE_DESERT = REGISTER.register("jadeite_desert", ECBiomes::JadeiteDesertBiome);
	public static final RegistryObject<Biome> VOLCANIC_CAVES = REGISTER.register("volcanic_caves", ECBiomes::VolcanicCaves);
	public static final RegistryObject<Biome> EMERY_DESERT = REGISTER.register("emery_desert", ECBiomes::EmeryDesertBiome);
	public static final RegistryObject<Biome> QUARTZ_DESERT = REGISTER.register("quartz_desert", ECBiomes::QuartzDesertBiome);
	public static final RegistryObject<Biome> PURPURACEUS_SWAMP = REGISTER.register("purpuraceus_swamp", ECBiomes::PurpuraceusSwamp);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
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
		builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
	}

	private static Biome baseOcean(MobSpawnSettings.Builder mobSpawnSetting, int waterColor, int waterFogColor, int fogColor, BiomeGenerationSettings.Builder biomeGenerationSetting) {
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(waterColor).waterFogColor(waterFogColor)
						.fogColor(fogColor).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobSpawnSetting.build())
				.generationSettings(biomeGenerationSetting.build()).build();
	}

	private static BiomeGenerationSettings.Builder baseOceanGeneration() {
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addWaterTrees(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultFlowers(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenSettingsBuilder);
		return biomeGenSettingsBuilder;
	}

	public static Biome DeadCrimsonOcean(boolean isDeep) {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		if (isDeep) {
			oceanSpawns(mobSpawnSettingsBuilder, 8, 4, 8);
		} else {
			oceanSpawns(mobSpawnSettingsBuilder, 10, 2, 15);
		}

		mobSpawnSettingsBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ECEntities.PURPLE_SPOTTED_BIGEYE, 25, 8, 8));
		mobSpawnSettingsBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 1, 1, 2));
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = baseOceanGeneration();
		biomeGenSettingsBuilder.addFeature(
				GenerationStep.Decoration.VEGETAL_DECORATION,
				isDeep ? AquaticPlacements.SEAGRASS_DEEP_WARM : AquaticPlacements.SEAGRASS_WARM
		);
		if (isDeep) {
			BiomeDefaultFeatures.addDefaultSeagrass(biomeGenSettingsBuilder);
		}

		BiomeDefaultFeatures.addLukeWarmKelp(biomeGenSettingsBuilder);
		return baseOcean(mobSpawnSettingsBuilder, 0x804cd6, 0x370537, 0xd86064, biomeGenSettingsBuilder);
	}

	public static Biome DeadWarpedOcean(boolean isDeep) {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		oceanSpawns(mobSpawnSettingsBuilder, 3, 4, 15);
		mobSpawnSettingsBuilder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(ECEntities.HERRING, 15, 1, 5));
		mobSpawnSettingsBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2));
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = baseOceanGeneration();
		biomeGenSettingsBuilder.addFeature(
				GenerationStep.Decoration.VEGETAL_DECORATION,
				isDeep ? AquaticPlacements.SEAGRASS_DEEP_COLD : AquaticPlacements.SEAGRASS_COLD
		);
		BiomeDefaultFeatures.addDefaultSeagrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addColdOceanExtraVegetation(biomeGenSettingsBuilder);
		return baseOcean(mobSpawnSettingsBuilder, 0x1a9ed6, 0x052d41, 0x60d2d8, biomeGenSettingsBuilder);
	}

	private static Biome Xanadu() {
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 4, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 4, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 7, 2, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 1, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 4, 2, 4))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 1, 1, 2));
		BiomeDefaultFeatures.caveSpawns(mobSpawnSettingsBuilder);
		globalOverworldGeneration(biomeGenSettingsBuilder);
		addXanaduDeltas(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addPlainGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		addXanaduVegetation(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addInfestedStone(biomeGenSettingsBuilder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(1.0F).downfall(0.9F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	private static Biome GinkgoForest() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.farmAnimals(mobSpawnSettingsBuilder);
		mobSpawnSettingsBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addFerns(biomeGenSettingsBuilder);
		addGinkgoTrees(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultFlowers(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addCommonBerryBushes(biomeGenSettingsBuilder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(0.5F).downfall(0.9F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533).grassColorOverride(0xfadc50).foliageColorOverride(0xfadc50)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	private static Biome KarstHills() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.oceanSpawns(mobSpawnSettingsBuilder, 8, 4, 8);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		addKarstDeltas(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addExtraEmeralds(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addInfestedStone(biomeGenSettingsBuilder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_STONY_PEAKS);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(1.0F).downfall(0.3F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4445678).waterFogColor(270131)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(1.0F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	private static Biome PetuniaPlains() {
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = (new MobSpawnSettings.Builder())
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 2))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 2, 6))
				.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 2, 2, 4));
		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		globalOverworldGeneration(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addPlainGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		addPetuniaPlainsVegetation(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addExtraEmeralds(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addInfestedStone(biomeGenSettingsBuilder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(0.5F).downfall(0.8F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x2260e1).waterFogColor(0x050533)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	public static Biome GoldenBeach() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();

		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultFlowers(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenSettingsBuilder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(0.8F).downfall(0.4F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	public static Biome PalmBeach() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();

		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		addPalmTrees(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultFlowers(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenSettingsBuilder);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(0.8F).downfall(0.6F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011)
						.fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.8F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(null).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	private static Biome AzureDesertBiome() {
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();

		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);

		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomeGenSettingsBuilder);

		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		addExtraLapis(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);

		BiomeDefaultFeatures.addBadlandGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	private static Biome JadeiteDesertBiome() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();

		BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenSettingsBuilder);
		addZombieVillagerRoom(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSprings(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addSurfaceFreezing(biomeGenSettingsBuilder);

		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);

		BiomeDefaultFeatures.addBadlandGrass(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);

		BiomeDefaultFeatures.addExtraEmeralds(biomeGenSettingsBuilder);
		addExtraEmerald(biomeGenSettingsBuilder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(calculateSkyColor(2.0F))
						.foliageColorOverride(10387789).grassColorOverride(9470285)
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
	}

	public static Biome VolcanicCaves() {
		MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
		mobSpawnSettingsBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 10, 3, 8));
		BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder);
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = new BiomeGenerationSettings.Builder();
		globalOverworldGeneration(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultOres(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenSettingsBuilder);
		BiomeDefaultFeatures.addDefaultMushrooms(biomeGenSettingsBuilder);
		addVolcanicCavesFeatures(biomeGenSettingsBuilder);
		Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES);

		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.RAIN)
				.temperature(0.5F).downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(0xc0d8ff).skyColor(calculateSkyColor(0.5F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.LAVA, 0.00625F))
						.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build())
				.mobSpawnSettings(mobSpawnSettingsBuilder.build())
				.generationSettings(biomeGenSettingsBuilder.build()).build();
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
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = (new BiomeGenerationSettings.Builder())
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

		BiomeDefaultFeatures.addNetherDefaultOres(biomeGenSettingsBuilder);
		addExtraAncientDebris(biomeGenSettingsBuilder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(1787717).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomeGenSettingsBuilder.build()).build();
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
		BiomeGenerationSettings.Builder biomeGenSettingsBuilder = (new BiomeGenerationSettings.Builder())
				.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
				.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_CRIMSON_ROOTS)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
				.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);
		BiomeDefaultFeatures.addNetherDefaultOres(biomeGenSettingsBuilder);
		addExtraQuartz(biomeGenSettingsBuilder);
		return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE)
				.temperature(2.0F).downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(4159204).waterFogColor(329011).fogColor(12169636).skyColor(calculateSkyColor(2.0F))
						.ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.00625F))
						.ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
						.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
						.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
						.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)).build())
				.mobSpawnSettings(mobspawnsettings)
				.generationSettings(biomeGenSettingsBuilder.build()).build();
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
}
