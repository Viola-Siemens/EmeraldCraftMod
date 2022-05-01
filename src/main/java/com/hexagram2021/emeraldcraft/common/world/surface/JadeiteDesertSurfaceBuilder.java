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

public class JadeiteDesertSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
	private static final BlockState JADEITE_SAND = ECBlocks.Decoration.JADEITE_SAND.defaultBlockState();
	private static final BlockState JADEITE_SANDSTONE = ECBlocks.Decoration.JADEITE_SANDSTONE.defaultBlockState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();

	public static final SurfaceBuilderBaseConfiguration CONFIG = new SurfaceBuilderBaseConfiguration(JADEITE_SAND, JADEITE_SANDSTONE, GRAVEL);

	public JadeiteDesertSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
		super(codec);
	}

	@Override
	public void apply(Random random, ChunkAccess chunk, Biome biome, int x, int z, int startHeight, double noise,
					  BlockState defaultBlock, BlockState defaultFluid, int surfaceLevel, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
		SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seaLevel, seed, CONFIG);
	}
}
