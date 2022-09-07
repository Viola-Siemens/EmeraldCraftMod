package com.hexagram2021.emeraldcraft.common.world.surface;

import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class XanaduSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
	public XanaduSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
		super(codec);
	}

	private static double sigmoid(double x) {
		return 1.0D / (1.0D + Math.exp(-x));
	}

	@Override
	public void apply(@Nonnull Random random, @Nonnull IChunk chunk, @Nonnull Biome biome, int x, int z, int startHeight, double noise,
					  @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int surfaceLevel, long seed, @Nonnull SurfaceBuilderConfig config) {
		int xInChunk = x & 15;
		int zInChunk = z & 15;
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();

		double noise_bottom = Math.abs(noise * 4.0D);

		int biomeFloor = 127 - startHeight;

		int heightSnapshot = 63 - (int)(sigmoid(startHeight - 63) * noise_bottom);

		if (startHeight > 0) {
			for (int y = biomeFloor; y < heightSnapshot; ++y) {
				blockpos$Mutable.set(xInChunk, y, zInChunk);

				chunk.setBlockState(blockpos$Mutable, Blocks.WATER.defaultBlockState(), false);
			}
			for(int y = heightSnapshot; y <= startHeight; ++y) {
				blockpos$Mutable.set(xInChunk, y, zInChunk);
				if (y <= startHeight - 3 - random.nextInt(6)) {
					chunk.setBlockState(blockpos$Mutable, config.getUnderwaterMaterial(), false);
				} else if (random.nextInt(6) * (startHeight - y) == 0) {
					chunk.setBlockState(blockpos$Mutable, config.getTopMaterial(), false);
				} else {
					chunk.setBlockState(blockpos$Mutable, config.getUnderMaterial(), false);
				}
			}
		}
	}
}
