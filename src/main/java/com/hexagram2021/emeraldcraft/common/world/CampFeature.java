package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CampFeature extends StructureFeature<NoneFeatureConfiguration> {

	public CampFeature(Codec<NoneFeatureConfiguration> codec, CampType type) {
		super(codec, PieceGeneratorSupplier.simple(CampFeature::checkLocation, (builder, context) -> CampFeature.generatePieces(builder, context, type)));
	}

	@Override @NotNull
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		if(!context.validBiomeOnTop(Heightmap.Types.OCEAN_FLOOR_WG)) {
			return false;
		}
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getMinBlockX() + 7, 0, context.chunkPos().getMinBlockZ() + 7);
		int landHeight = context.chunkGenerator().getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

		NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
		BlockState topBlock = columnOfBlocks.getBlock(landHeight);

		return topBlock.getFluidState().isEmpty();
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context, CampType type) {
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getMinBlockX() + 7, 0, context.chunkPos().getMinBlockZ() + 7);
		BlockPos blockpos = new BlockPos(
				context.chunkPos().getMinBlockX(),
				context.chunkGenerator().getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor()),
				context.chunkPos().getMinBlockZ()
		);
		Rotation rotation = Rotation.getRandom(context.random());
		CampPieces.addPieces(context.structureManager(), blockpos, rotation, builder, type);
	}
}
