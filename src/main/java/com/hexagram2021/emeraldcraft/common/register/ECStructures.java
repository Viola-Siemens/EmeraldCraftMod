package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.world.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.event.RegistryEvent;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructures {
	public static final StructurePieceType SHELTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "shelter", ShelterPieces.ShelterPiece::new);
	public static final StructurePieceType CAMP_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "camp", CampPieces.CampPiece::new);

	public static final ShelterFeature SHELTER = new ShelterFeature(NoneFeatureConfiguration.CODEC);
	public static final StructureFeature<JigsawConfiguration> NETHER_WARFIELD = new NetherWarfieldFeature(JigsawConfiguration.CODEC);
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

	public static void init(RegistryEvent.Register<StructureFeature<?>> event) {
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
	}
}
