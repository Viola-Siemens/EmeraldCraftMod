package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.world.*;
import com.hexagram2021.emeraldcraft.mixin.StructureFeatureAccess;
import com.hexagram2021.emeraldcraft.mixin.StructureSettingsAccess;
import com.hexagram2021.emeraldcraft.mixin.StructureSettingsConfigAccess;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructures {
	public static final StructurePieceType SHELTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "shelter", ShelterPieces.ShelterPiece::new);
	public static final StructurePieceType CAMP_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "camp", CampPieces.CampPiece::new);

	public static final class EntrenchmentPieceTypes {
		public static final StructurePieceType HALL_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_hall", EntrenchmentPieces.HallPiece::new);
		public static final StructurePieceType START_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_start", EntrenchmentPieces.StartPiece::new);
		public static final StructurePieceType CROSSING_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_crossing", EntrenchmentPieces.CrossingPiece::new);
		public static final StructurePieceType T_BRIDGE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_t_bridge", EntrenchmentPieces.TBridgePiece::new);
		public static final StructurePieceType LONG_HALLWAY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_long_hallway", EntrenchmentPieces.LongHallwayPiece::new);
		public static final StructurePieceType SHORT_HALLWAY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_short_hallway", EntrenchmentPieces.ShortHallwayPiece::new);
		public static final StructurePieceType CHEST_HALLWAY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_chest_hallway", EntrenchmentPieces.ChestHallwayPiece::new);
		public static final StructurePieceType BRIDGE_HALLWAY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_bridge_hallway", EntrenchmentPieces.ShortBridgeHallwayPiece::new);
		public static final StructurePieceType LEFT_TURN_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_left_turn", EntrenchmentPieces.LeftTurnPiece::new);
		public static final StructurePieceType RIGHT_TURN_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_right_turn", EntrenchmentPieces.RightTurnPiece::new);
		public static final StructurePieceType MONSTER_ROOM_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_monster_room", EntrenchmentPieces.MonsterRoomPiece::new);
		public static final StructurePieceType LABORATORY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_laboratory", EntrenchmentPieces.LaboratoryPiece::new);
		public static final StructurePieceType BALCONY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_balcony", EntrenchmentPieces.BalconyPiece::new);
		public static final StructurePieceType PORTAL_ROOM_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_portal_room", EntrenchmentPieces.PortalRoomPiece::new);
		public static final StructurePieceType WALL_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_wall", EntrenchmentPieces.WallPiece::new);
		public static final StructurePieceType KITCHEN_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "entrenchment_kitchen", EntrenchmentPieces.KitchenPiece::new);

		private EntrenchmentPieceTypes() {}

		public static void init() {}
	}

	public static final ShelterFeature SHELTER = new ShelterFeature(NoneFeatureConfiguration.CODEC);
	public static final StructureFeature<JigsawConfiguration> NETHER_WARFIELD = new NetherWarfieldFeature(JigsawConfiguration.CODEC);
	public static final EntrenchmentFeature ENTRENCHMENT = new EntrenchmentFeature(NoneFeatureConfiguration.CODEC);
	public static final CampFeature BADLANDS_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.BADLANDS);
	public static final CampFeature BIRCH_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.BIRCH);
	public static final CampFeature DESERT_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.DESERT);
	public static final CampFeature JUNGLE_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.JUNGLE);
	public static final CampFeature PLAINS_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.PLAINS);
	public static final CampFeature SAVANNA_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SAVANNA);
	public static final CampFeature SNOW_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SNOW);
	public static final CampFeature STONY_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.STONY);
	public static final CampFeature SWAMP_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.SWAMP);
	public static final CampFeature TAIGA_CAMP = new CampFeature(NoneFeatureConfiguration.CODEC, CampTypes.TAIGA);

	@SuppressWarnings("ConstantConditions")
	public static void init(RegistryEvent.Register<StructureFeature<?>> event) {
		EntrenchmentPieceTypes.init();

		SHELTER.setRegistryName(new ResourceLocation(MODID, "shelter"));
		NETHER_WARFIELD.setRegistryName(new ResourceLocation(MODID, "nether_warfield"));
		ENTRENCHMENT.setRegistryName(new ResourceLocation(MODID, "entrenchment"));

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
		event.getRegistry().register(ENTRENCHMENT);

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
		
		StructureFeatureConfiguration shelterConfig = new StructureFeatureConfiguration(64, 32, 19260817);
		StructureFeatureConfiguration netherWarfieldConfig = new StructureFeatureConfiguration(32, 8, 10387312);
		StructureFeatureConfiguration entrenchmentConfig = new StructureFeatureConfiguration(40, 24, 23456);
		StructureFeatureConfiguration campConfig = new StructureFeatureConfiguration(50, 20, 200013907);
		
		StructureFeature.STRUCTURES_REGISTRY.put(SHELTER.getRegistryName().toString().toLowerCase(Locale.ROOT), SHELTER);
		StructureFeature.STRUCTURES_REGISTRY.put(NETHER_WARFIELD.getRegistryName().toString().toLowerCase(Locale.ROOT), NETHER_WARFIELD);
		StructureFeature.STRUCTURES_REGISTRY.put(ENTRENCHMENT.getRegistryName().toString().toLowerCase(Locale.ROOT), ENTRENCHMENT);
		StructureFeature.STRUCTURES_REGISTRY.put(BADLANDS_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), BADLANDS_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(BIRCH_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), BIRCH_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(DESERT_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), DESERT_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(JUNGLE_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), JUNGLE_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(PLAINS_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), PLAINS_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(SAVANNA_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SAVANNA_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(SNOW_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SNOW_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(STONY_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), STONY_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(SWAMP_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), SWAMP_CAMP);
		StructureFeature.STRUCTURES_REGISTRY.put(TAIGA_CAMP.getRegistryName().toString().toLowerCase(Locale.ROOT), TAIGA_CAMP);
		StructureFeatureAccess.setNOISE_AFFECTING_FEATURES(ImmutableList.<StructureFeature<?>>builder()
				.addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
				.add(SHELTER)
				.add(NETHER_WARFIELD)
				.add(ENTRENCHMENT)
				.add(BADLANDS_CAMP).add(BIRCH_CAMP).add(DESERT_CAMP).add(JUNGLE_CAMP).add(PLAINS_CAMP)
				.add(SAVANNA_CAMP).add(SNOW_CAMP).add(STONY_CAMP).add(SWAMP_CAMP).add(TAIGA_CAMP)
				.build()
		);
		ImmutableMap.Builder<StructureFeature<?>, StructureFeatureConfiguration> defaultsBuilder =
				ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
						.putAll(StructureSettings.DEFAULTS)
						.put(SHELTER, shelterConfig)
						.put(NETHER_WARFIELD, netherWarfieldConfig)
						.put(ENTRENCHMENT, entrenchmentConfig)
						.put(BADLANDS_CAMP, campConfig).put(BIRCH_CAMP, campConfig)
						.put(DESERT_CAMP, campConfig).put(JUNGLE_CAMP, campConfig)
						.put(PLAINS_CAMP, campConfig).put(SAVANNA_CAMP, campConfig)
						.put(SNOW_CAMP, campConfig).put(STONY_CAMP, campConfig)
						.put(SWAMP_CAMP, campConfig).put(TAIGA_CAMP, campConfig);
		StructureSettingsAccess.setDEFAULTS(defaultsBuilder.build());
		
		
		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();
			
			if(structureMap instanceof ImmutableMap){
				Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
				tempMap.put(SHELTER, shelterConfig);
				tempMap.put(NETHER_WARFIELD, netherWarfieldConfig);
				tempMap.put(ENTRENCHMENT, entrenchmentConfig);
				tempMap.put(BADLANDS_CAMP, campConfig); tempMap.put(BIRCH_CAMP, campConfig);
				tempMap.put(DESERT_CAMP, campConfig); tempMap.put(JUNGLE_CAMP, campConfig);
				tempMap.put(PLAINS_CAMP, campConfig); tempMap.put(SAVANNA_CAMP, campConfig);
				tempMap.put(SNOW_CAMP, campConfig); tempMap.put(STONY_CAMP, campConfig);
				tempMap.put(SWAMP_CAMP, campConfig); tempMap.put(TAIGA_CAMP, campConfig);
				((StructureSettingsConfigAccess)settings.getValue().structureSettings()).setStructureConfig(tempMap);
			}
			else{
				structureMap.put(SHELTER, shelterConfig);
				structureMap.put(NETHER_WARFIELD, netherWarfieldConfig);
				structureMap.put(ENTRENCHMENT, entrenchmentConfig);
				structureMap.put(BADLANDS_CAMP, campConfig); structureMap.put(BIRCH_CAMP, campConfig);
				structureMap.put(DESERT_CAMP, campConfig); structureMap.put(JUNGLE_CAMP, campConfig);
				structureMap.put(PLAINS_CAMP, campConfig); structureMap.put(SAVANNA_CAMP, campConfig);
				structureMap.put(SNOW_CAMP, campConfig); structureMap.put(STONY_CAMP, campConfig);
				structureMap.put(SWAMP_CAMP, campConfig); structureMap.put(TAIGA_CAMP, campConfig);
			}
		});
	}
}
