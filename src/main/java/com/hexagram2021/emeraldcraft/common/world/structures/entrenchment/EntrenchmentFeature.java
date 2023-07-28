package com.hexagram2021.emeraldcraft.common.world.structures.entrenchment;

import com.hexagram2021.emeraldcraft.common.register.ECStructureTypes;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

public class EntrenchmentFeature extends Structure {
	public static final Codec<EntrenchmentFeature> CODEC = simpleCodec(EntrenchmentFeature::new);

	public EntrenchmentFeature(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		return Optional.of(new Structure.GenerationStub(context.chunkPos().getWorldPosition(), (builder) -> generatePieces(builder, context)));
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.STRONGHOLDS;
	}

	private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		EntrenchmentPieces.StartPiece startPiece = new EntrenchmentPieces.StartPiece(
				context.random(), context.chunkPos().getBlockX(2), context.chunkPos().getBlockZ(2)
		);
		builder.addPiece(startPiece);
		startPiece.addChildren(startPiece, builder, context.random());
		List<StructurePiece> list = startPiece.pendingChildren;

		while(!list.isEmpty()) {
			int rank = context.random().nextInt(list.size());
			StructurePiece piece = list.remove(rank);
			piece.addChildren(startPiece, builder, context.random());
		}
	}

	@Override
	public StructureType<?> type() {
		return ECStructureTypes.ENTRENCHMENT.get();
	}
}
