package com.hexagram2021.emeraldcraft.common.world.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class GoldenBeachSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public GoldenBeachSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
		super(codec);
	}

	@Override
	public void apply(@Nonnull Random random, @Nonnull IChunk chunk, @Nonnull Biome biome, int x, int z, int startHeight, double noise,
					  @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int surfaceLevel, long seed, @Nonnull SurfaceBuilderConfig config) {
		if (noise > 0.8D) {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, ECSurfaceBuildersConfigs.CONFIG_RED_BEACH);
		} else if (noise > 0.4D) {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, SurfaceBuilder.CONFIG_DESERT);
		} else {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, ECSurfaceBuildersConfigs.CONFIG_GOLDEN_BEACH);
		}
	}
}
