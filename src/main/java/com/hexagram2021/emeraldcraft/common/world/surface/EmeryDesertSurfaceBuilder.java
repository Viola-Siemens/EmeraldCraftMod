package com.hexagram2021.emeraldcraft.common.world.surface;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

public class EmeryDesertSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
	private static final BlockState EMERY_SAND = ECBlocks.Decoration.EMERY_SAND.defaultBlockState();
	private static final BlockState EMERY_SANDSTONE = ECBlocks.Decoration.EMERY_SANDSTONE.defaultBlockState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();

	public static final SurfaceBuilderBaseConfiguration CONFIG = new SurfaceBuilderBaseConfiguration(EMERY_SAND, EMERY_SANDSTONE, GRAVEL);

	public EmeryDesertSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
		super(codec);
	}

	@Override
	public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int startHeight, double noise,
					  BlockState defaultBlock, BlockState defaultFluid, int surfaceLevel, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
		SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seaLevel, seed, CONFIG);
	}
}
