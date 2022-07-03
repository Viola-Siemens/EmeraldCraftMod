package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.NetherWarfieldFeature;
import com.hexagram2021.emeraldcraft.common.world.ShelterFeature;
import com.hexagram2021.emeraldcraft.common.world.ShelterPieces;
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

	public static final ShelterFeature SHELTER = new ShelterFeature(NoneFeatureConfiguration.CODEC);
	public static final StructureFeature<JigsawConfiguration> NETHER_WARFIELD = new NetherWarfieldFeature(JigsawConfiguration.CODEC);

	public static void init(RegistryEvent.Register<StructureFeature<?>> event) {
		SHELTER.setRegistryName(new ResourceLocation(MODID, "shelter"));
		NETHER_WARFIELD.setRegistryName(new ResourceLocation(MODID, "nether_warfield"));
		event.getRegistry().register(SHELTER);
		event.getRegistry().register(NETHER_WARFIELD);
	}
}
