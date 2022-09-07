package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.world.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructures {
	public static final IStructurePieceType SHELTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "shelter", ShelterPieces.ShelterPiece::new);
	public static final IStructurePieceType CAMP_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "camp", CampPieces.CampPiece::new);

	public static final ShelterFeature SHELTER = new ShelterFeature(NoFeatureConfig.CODEC);
	public static final Structure<VillageConfig> NETHER_WARFIELD = new NetherWarfieldFeature(VillageConfig.CODEC);
	public static final CampFeature BADLANDS_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.BADLANDS);
	public static final CampFeature BIRCH_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.BIRCH);
	public static final CampFeature DESERT_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.DESERT);
	public static final CampFeature JUNGLE_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.JUNGLE);
	public static final CampFeature PLAINS_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.PLAINS);
	public static final CampFeature SAVANNA_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.SAVANNA);
	public static final CampFeature SNOW_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.SNOW);
	public static final CampFeature STONY_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.STONY);
	public static final CampFeature SWAMP_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.SWAMP);
	public static final CampFeature TAIGA_CAMP = new CampFeature(NoFeatureConfig.CODEC, CampTypes.TAIGA);

	public static void init(RegistryEvent.Register<Structure<?>> event) {
		SHELTER.setRegistryName(new ResourceLocation(MODID, "shelter"));
		NETHER_WARFIELD.setRegistryName(new ResourceLocation(MODID, "nether_warfield"));

		BADLANDS_CAMP.setRegistryName(new ResourceLocation(MODID, "badlands_camp"));
		BIRCH_CAMP.setRegistryName(new ResourceLocation(MODID, "birch_camp"));
		DESERT_CAMP.setRegistryName(new ResourceLocation(MODID, "desert_camp"));
		JUNGLE_CAMP.setRegistryName(new ResourceLocation(MODID, "jungle_camp"));
		PLAINS_CAMP.setRegistryName(new ResourceLocation(MODID, "plains_camp"));
		SAVANNA_CAMP.setRegistryName(new ResourceLocation(MODID, "savanna_camp"));
		SNOW_CAMP.setRegistryName(new ResourceLocation(MODID, "snow_camp"));
		STONY_CAMP.setRegistryName(new ResourceLocation(MODID, "stony_camp"));
		SWAMP_CAMP.setRegistryName(new ResourceLocation(MODID, "swamp_camp"));
		TAIGA_CAMP.setRegistryName(new ResourceLocation(MODID, "taiga_camp"));

		event.getRegistry().register(SHELTER);
		event.getRegistry().register(NETHER_WARFIELD);

		event.getRegistry().register(BADLANDS_CAMP);
		event.getRegistry().register(BIRCH_CAMP);
		event.getRegistry().register(DESERT_CAMP);
		event.getRegistry().register(JUNGLE_CAMP);
		event.getRegistry().register(PLAINS_CAMP);
		event.getRegistry().register(SAVANNA_CAMP);
		event.getRegistry().register(SNOW_CAMP);
		event.getRegistry().register(STONY_CAMP);
		event.getRegistry().register(SWAMP_CAMP);
		event.getRegistry().register(TAIGA_CAMP);

		StructureSeparationSettings shelterConfig = new StructureSeparationSettings(64, 32, 1926081709);
		StructureSeparationSettings netherWarfieldConfig = new StructureSeparationSettings(32, 8, 20387312);
		StructureSeparationSettings campConfig = new StructureSeparationSettings(50, 20, 200013907);

		Structure.STRUCTURES_REGISTRY.put(SHELTER.getRegistryName().toString().toLowerCase(Locale.ROOT), SHELTER);
		Structure.STRUCTURES_REGISTRY.put(NETHER_WARFIELD.getRegistryName().toString().toLowerCase(Locale.ROOT), NETHER_WARFIELD);
		Structure.STRUCTURES_REGISTRY.put(BADLANDS_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), BADLANDS_CAMP);
		Structure.STRUCTURES_REGISTRY.put(BIRCH_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), BIRCH_CAMP);
		Structure.STRUCTURES_REGISTRY.put(DESERT_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), DESERT_CAMP);
		Structure.STRUCTURES_REGISTRY.put(JUNGLE_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), JUNGLE_CAMP);
		Structure.STRUCTURES_REGISTRY.put(PLAINS_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), PLAINS_CAMP);
		Structure.STRUCTURES_REGISTRY.put(SAVANNA_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SAVANNA_CAMP);
		Structure.STRUCTURES_REGISTRY.put(SNOW_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SNOW_CAMP);
		Structure.STRUCTURES_REGISTRY.put(STONY_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), STONY_CAMP);
		Structure.STRUCTURES_REGISTRY.put(SWAMP_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SWAMP_CAMP);
		Structure.STRUCTURES_REGISTRY.put(TAIGA_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), TAIGA_CAMP);
		Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
				.addAll(Structure.NOISE_AFFECTING_FEATURES)
				.add(SHELTER)
				.add(NETHER_WARFIELD)
				.add(BADLANDS_CAMP).add(BIRCH_CAMP).add(DESERT_CAMP).add(JUNGLE_CAMP).add(PLAINS_CAMP)
				.add(SAVANNA_CAMP).add(SNOW_CAMP).add(STONY_CAMP).add(SWAMP_CAMP).add(TAIGA_CAMP)
				.addAll(CampTypes.getCustomCamps())
				.build();

		ImmutableMap.Builder<Structure<?>, StructureSeparationSettings> defaultsBuilder =
				ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
				.putAll(DimensionStructuresSettings.DEFAULTS)
				.put(SHELTER, shelterConfig)
				.put(NETHER_WARFIELD, netherWarfieldConfig)
				.put(BADLANDS_CAMP, campConfig).put(BIRCH_CAMP, campConfig)
				.put(DESERT_CAMP, campConfig).put(JUNGLE_CAMP, campConfig)
				.put(PLAINS_CAMP, campConfig).put(SAVANNA_CAMP, campConfig)
				.put(SNOW_CAMP, campConfig).put(STONY_CAMP, campConfig)
				.put(SWAMP_CAMP, campConfig).put(TAIGA_CAMP, campConfig);
		CampTypes.getCustomCamps().forEach(campFeature -> defaultsBuilder.put(campFeature, campConfig));
		DimensionStructuresSettings.DEFAULTS = defaultsBuilder.build();


		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

			if(structureMap instanceof ImmutableMap){
				Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
				tempMap.put(SHELTER, shelterConfig);
				tempMap.put(NETHER_WARFIELD, netherWarfieldConfig);
				tempMap.put(BADLANDS_CAMP, campConfig); tempMap.put(BIRCH_CAMP, campConfig);
				tempMap.put(DESERT_CAMP, campConfig); tempMap.put(JUNGLE_CAMP, campConfig);
				tempMap.put(PLAINS_CAMP, campConfig); tempMap.put(SAVANNA_CAMP, campConfig);
				tempMap.put(SNOW_CAMP, campConfig); tempMap.put(STONY_CAMP, campConfig);
				tempMap.put(SWAMP_CAMP, campConfig); tempMap.put(TAIGA_CAMP, campConfig);
				CampTypes.getCustomCamps().forEach(campFeature -> tempMap.put(campFeature, campConfig));
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else {
				structureMap.put(SHELTER, shelterConfig);
				structureMap.put(NETHER_WARFIELD, netherWarfieldConfig);
				structureMap.put(BADLANDS_CAMP, campConfig); structureMap.put(BIRCH_CAMP, campConfig);
				structureMap.put(DESERT_CAMP, campConfig); structureMap.put(JUNGLE_CAMP, campConfig);
				structureMap.put(PLAINS_CAMP, campConfig); structureMap.put(SAVANNA_CAMP, campConfig);
				structureMap.put(SNOW_CAMP, campConfig); structureMap.put(STONY_CAMP, campConfig);
				structureMap.put(SWAMP_CAMP, campConfig); structureMap.put(TAIGA_CAMP, campConfig);
				CampTypes.getCustomCamps().forEach(campFeature -> structureMap.put(campFeature, campConfig));
			}
		});
	}
}
