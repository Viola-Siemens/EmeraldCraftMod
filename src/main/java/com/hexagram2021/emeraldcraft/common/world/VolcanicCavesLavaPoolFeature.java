package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.material.Material;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class VolcanicCavesLavaPoolFeature extends Feature<NoneFeatureConfiguration> {
	private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
	private static final BlockState CRYING_OBSIDIAN = Blocks.CRYING_OBSIDIAN.defaultBlockState();
	private static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.defaultBlockState();
	private static final BlockState SMOOTH_BASALT = Blocks.SMOOTH_BASALT.defaultBlockState();
	private static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();

	private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();

	public VolcanicCavesLavaPoolFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	private static final int MAX_DEPTH = 6;

	private void doPlace(WorldGenLevel level, Set<BlockPos> visited, BlockPos curPosition, Random random, Predicate<BlockState> predicate, int depth) {
		for(int b = 1; b <= 6; ++b) {
			int s = ((b & 4) >> 2);
			int b1 = (b & 3) + s;
			int y = (b1 & 1) ^ ((b1 & 2) >> 1) ^ 1;
			int x = (b1 & 1) ^ y;
			int z = ((b1 & 2) >> 1) ^ y;

			x *= 2 * s - 1;
			y *= 2 * s - 1;
			z *= 2 * s - 1;

			BlockPos blockpos1 = curPosition.offset(x, y, z);
			BlockState blockstate = level.getBlockState(blockpos1);

			if(visited.contains(blockpos1)) {
				continue;
			}
			visited.add(blockpos1);
			if(random.nextInt(6) == 0) {
				continue;
			}
			if(blockstate.is(Blocks.DEEPSLATE) || blockstate.is(Blocks.TUFF) ||blockstate.is(Blocks.STONE) ||
					blockstate.is(Blocks.ANDESITE) || blockstate.is(Blocks.DIORITE) || blockstate.is(Blocks.GRANITE)) {
				if(depth <= MAX_DEPTH) {
					doPlace(level, visited, blockpos1, random, predicate, depth + 1);
				}
				this.safeSetBlock(level, blockpos1, LAVA, predicate);

				for(int m = 1; m <= 5; ++m) {
					int ms = ((m & 4) >> 2);
					int m1 = (m & 3) + ms;
					int my = (m1 & 1) ^ ((m1 & 2) >> 1) ^ 1;
					int mx = (m1 & 1) ^ my;
					int mz = ((m1 & 2) >> 1) ^ my;

					mx *= 2 * ms - 1;
					my *= 2 * ms - 1;
					mz *= 2 * ms - 1;

					BlockPos blockpos2 = blockpos1.offset(mx, my, mz);
					if(level.isEmptyBlock(blockpos2) || (blockstate.is(Blocks.DEEPSLATE) && random.nextInt(3) != 0)) {
						switch (random.nextInt(15)) {
							case 0, 1, 2, 3, 4 -> this.safeSetBlock(level, blockpos1, OBSIDIAN, predicate);
							case 5, 6, 7, 8 -> this.safeSetBlock(level, blockpos1, MAGMA_BLOCK, predicate);
							case 9, 10, 11 -> this.safeSetBlock(level, blockpos1, SMOOTH_BASALT, predicate);
							case 12, 13 -> this.safeSetBlock(level, blockpos1, CRYING_OBSIDIAN, predicate);
							case 14 -> this.safeSetBlock(level, blockpos1, BLACKSTONE, predicate);
						}
					}
				}
			}
		}
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
		BlockPos blockpos = context.origin();
		Random random = context.random();
		WorldGenLevel worldgenlevel = context.level();

		final Set<BlockPos> visited = new HashSet<>();
		doPlace(worldgenlevel, visited, blockpos, random, predicate, 0);
		return true;
	}
}