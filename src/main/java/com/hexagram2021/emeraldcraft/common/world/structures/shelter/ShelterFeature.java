package com.hexagram2021.emeraldcraft.common.world.structures.shelter;

import com.hexagram2021.emeraldcraft.common.register.ECStructureTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class ShelterFeature extends Structure {
	public static final Codec<ShelterFeature> CODEC = simpleCodec(ShelterFeature::new);

	public ShelterFeature(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (builder) -> generatePieces(builder, context));
	}

	private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
		Rotation rotation = Rotation.getRandom(context.random());
		ShelterPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder);
	}

	@Override
	public StructureType<?> type() {
		return ECStructureTypes.SHELTER.get();
	}
}
