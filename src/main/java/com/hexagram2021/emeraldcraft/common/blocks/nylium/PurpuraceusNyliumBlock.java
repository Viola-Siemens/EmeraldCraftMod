package com.hexagram2021.emeraldcraft.common.blocks.nylium;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatureKeys.VegetationConfiguredFeatures.PURPURACEUS_SWAMP_VEGETATION_BONEMEAL;

public class PurpuraceusNyliumBlock extends NyliumBlock {
	public PurpuraceusNyliumBlock(Properties props) {
		super(props);
	}

	@Override
	public void performBonemeal(ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		BlockState current = level.getBlockState(blockPos);
		BlockPos above = blockPos.above();
		ChunkGenerator chunkgenerator = level.getChunkSource().getGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
		if (current.is(ECBlocks.Plant.PURPURACEUS_NYLIUM.get())) {
			place(registry, PURPURACEUS_SWAMP_VEGETATION_BONEMEAL, level, chunkgenerator, random, above);
		}

		for(BlockPos mutable : BlockPos.betweenClosed(blockPos.offset(-1, -1, -1), blockPos.offset(1, 1, 1))) {
			BlockState blockstate = level.getBlockState(mutable);
			if (blockstate.is(Blocks.NETHERRACK) && random.nextBoolean()) {
				level.setBlock(mutable, ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(), Block.UPDATE_ALL);
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private static void place(Registry<ConfiguredFeature<?, ?>> registry, ResourceKey<ConfiguredFeature<?, ?>> feature,
							  ServerLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos blockPos) {
		registry.getHolder(feature).ifPresent(
				holder -> holder.value().place(level, chunkGenerator, random, blockPos)
		);
	}
}
