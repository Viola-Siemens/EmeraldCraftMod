package com.hexagram2021.emeraldcraft.common.world.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class GinkgoForestSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public GinkgoForestSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
		super(codec);
	}

	@Override
	public void apply(@Nonnull Random random, @Nonnull IChunk chunk, @Nonnull Biome biome, int x, int z, int startHeight, double noise,
					  @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int surfaceLevel, long seed, @Nonnull SurfaceBuilderConfig config) {
		if (noise > 0.6D) {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, SurfaceBuilder.CONFIG_PODZOL);
		} else {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, SurfaceBuilder.CONFIG_GRASS);
		}
	}
}
