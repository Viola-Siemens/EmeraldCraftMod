package com.hexagram2021.emeraldcraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class NetherWarfieldFeature extends JigsawStructure {
	public NetherWarfieldFeature(Codec<VillageConfig> codec) {
		super(codec, 60, true, false);
	}

	@Override @Nonnull
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override @Nonnull
	public String getFeatureName() {
		return new ResourceLocation(MODID, "nether_warfield").toString();
	}

	@Override
	protected boolean isFeatureChunk(@Nonnull ChunkGenerator chunkGenerator, @Nonnull BiomeProvider biomeSource,
									 long seed, @Nonnull SharedSeedRandom chunkRandom, int x, int z,
									 @Nonnull Biome biome, @Nonnull ChunkPos chunkPos, @Nonnull VillageConfig config) {
		BlockPos centerOfChunk = new BlockPos((x << 4) + 7, 0, (z << 4) + 7);
		int landHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
		BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));

		return topBlock.getFluidState().isEmpty();
	}
}
