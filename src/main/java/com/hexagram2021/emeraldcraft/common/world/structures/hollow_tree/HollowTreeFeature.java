package com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
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

public class HollowTreeFeature extends StructureFeature<NoneFeatureConfiguration> {
	public HollowTreeFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec, PieceGeneratorSupplier.simple(HollowTreeFeature::checkLocation, HollowTreeFeature::generatePieces));
	}

	@Override @NotNull
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
		if(!context.validBiomeOnTop(Heightmap.Types.OCEAN_FLOOR_WG)) {
			return false;
		}
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getBlockX(5), 0, context.chunkPos().getBlockZ(5));
		int landHeight = context.chunkGenerator().getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

		NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
		BlockState topBlock = columnOfBlocks.getBlock(landHeight);

		return topBlock.getFluidState().isEmpty();
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getBlockX(5), 0, context.chunkPos().getBlockZ(5));
		BlockPos blockpos = new BlockPos(
				centerOfChunk.getX(),
				context.chunkGenerator().getBaseHeight(
						centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor()
				),
				centerOfChunk.getZ()
		);
		Rotation rotation = Rotation.getRandom(context.random());
		HollowTreePieces.addPieces(context.structureManager(), blockpos, rotation, builder);
	}
}
