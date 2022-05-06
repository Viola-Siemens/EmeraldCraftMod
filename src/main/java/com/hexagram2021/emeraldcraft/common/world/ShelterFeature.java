package com.hexagram2021.emeraldcraft.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class ShelterFeature extends StructureFeature<NoneFeatureConfiguration> {

	public ShelterFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource,
									 long seed, WorldgenRandom chunkRandom, ChunkPos chunk,
									 Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration config, LevelHeightAccessor level) {
		BlockPos centerOfChunk = new BlockPos((chunk.x << 4) + 7, 0, (chunk.z << 4) + 7);
		int landHeight = chunkGenerator.getBaseHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, level);

		NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), level);
		BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));

		return topBlock.getFluidState().isEmpty();
	}

	@Override
	public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return ShelterFeature.FeatureStart::new;
	}

	public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
		public FeatureStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos chunkPos, int references, long seed) {
			super(feature, chunkPos, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager,
								   ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration config, LevelHeightAccessor level) {
			BlockPos blockpos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
			Rotation rotation = Rotation.getRandom(this.random);
			ShelterPieces.addPieces(structureManager, blockpos, rotation, this);
		}
	}
}
