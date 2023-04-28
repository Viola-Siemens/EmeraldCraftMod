package com.hexagram2021.emeraldcraft.common.blocks.nylium;

import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PurpuraceusNyliumBlock extends NyliumBlock {
	public PurpuraceusNyliumBlock(Properties props) {
		super(props);
	}

	@Override
	public void performBonemeal(ServerLevel level, @NotNull Random random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		BlockState current = level.getBlockState(blockPos);
		BlockPos above = blockPos.above();
		ChunkGenerator chunkgenerator = level.getChunkSource().getGenerator();
		if (current.is(ECBlocks.Plant.PURPURACEUS_NYLIUM.get())) {
			ECConfiguredFeatures.VegetationFeatures.PURPURACEUS_SWAMP_VEGETATION_BONEMEAL.value().place(level, chunkgenerator, random, above);
		}

		for(BlockPos mutable : BlockPos.betweenClosed(blockPos.offset(-1, -1, -1), blockPos.offset(1, 1, 1))) {
			BlockState blockstate = level.getBlockState(mutable);
			if (blockstate.is(Blocks.NETHERRACK) && random.nextBoolean()) {
				level.setBlock(mutable, ECBlocks.Plant.PURPURACEUS_NYLIUM.defaultBlockState(), Block.UPDATE_ALL);
			}
		}
	}
}
