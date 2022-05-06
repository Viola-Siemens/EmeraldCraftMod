package com.hexagram2021.emeraldcraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class NetherWarfieldFeature extends JigsawFeature {
	public NetherWarfieldFeature(Codec<JigsawConfiguration> codec) {
		super(codec, 60, true, false);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource,
									 long seed, WorldgenRandom chunkRandom, ChunkPos chunk,
									 Biome biome, ChunkPos chunkPos, JigsawConfiguration config, LevelHeightAccessor level) {
		BlockPos centerOfChunk = new BlockPos((chunk.x << 4) + 7, 0, (chunk.z << 4) + 7);
		int landHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);

		NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), level);
		BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));

		return topBlock.getFluidState().isEmpty();
	}
}
