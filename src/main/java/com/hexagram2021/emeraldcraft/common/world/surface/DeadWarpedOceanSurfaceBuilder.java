package com.hexagram2021.emeraldcraft.common.world.surface;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class DeadWarpedOceanSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public DeadWarpedOceanSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
		super(codec);
	}

	@Override
	public void apply(@Nonnull Random random, @Nonnull IChunk chunk, @Nonnull Biome biome, int x, int z, int startHeight, double noise,
					  @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int surfaceLevel, long seed, @Nonnull SurfaceBuilderConfig config) {
		double noise2 = Biome.BIOME_INFO_NOISE.getValue((double)x * 0.25D, (double)z * 0.25D, false);
		if(noise > -0.025D && noise < 0.025D) {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, ECSurfaceBuildersConfigs.CONFIG_WARPED_OCEAN_BASALT);
		} else if(noise2 > -0.05D && noise2 < 0.05D) {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, ECSurfaceBuildersConfigs.CONFIG_WARPED_OCEAN_GRAVEL);
		} else {
			SurfaceBuilder.DEFAULT.apply(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, surfaceLevel, seed, ECSurfaceBuildersConfigs.CONFIG_WARPED_OCEAN);
		}
	}
}
