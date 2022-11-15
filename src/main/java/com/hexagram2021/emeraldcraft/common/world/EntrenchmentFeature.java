package com.hexagram2021.emeraldcraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntrenchmentFeature extends StructureFeature<NoneFeatureConfiguration> {
	public EntrenchmentFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec, PieceGeneratorSupplier.simple(EntrenchmentFeature::checkLocation, EntrenchmentFeature::generatePieces));
	}

	private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		return context.validBiomeOnTop(Heightmap.Types.OCEAN_FLOOR_WG);
	}

	@Override @NotNull
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.STRONGHOLDS;
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
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
}
