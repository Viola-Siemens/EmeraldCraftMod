package com.hexagram2021.emeraldcraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

public class NetherWarfieldFeature extends JigsawFeature {
	public NetherWarfieldFeature(Codec<JigsawConfiguration> codec) {
		super(codec, 60, true, false, NetherWarfieldFeature::checkLocation);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		BlockPos centerOfChunk = new BlockPos((context.chunkPos().getMinBlockX() << 4) + 7, 0, (context.chunkPos().getMinBlockZ() << 4) + 7);
		int landHeight = context.chunkGenerator().getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

		NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
		BlockState topBlock = columnOfBlocks.getBlock(landHeight);

		return topBlock.getFluidState().isEmpty();
	}
}
