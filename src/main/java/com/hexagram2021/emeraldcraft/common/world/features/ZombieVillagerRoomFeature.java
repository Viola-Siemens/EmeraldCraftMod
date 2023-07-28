package com.hexagram2021.emeraldcraft.common.world.features;

import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
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

import java.util.function.Predicate;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("deprecation")
public class ZombieVillagerRoomFeature extends Feature<NoneFeatureConfiguration> {
	private static final ResourceLocation ZOMBIE_VILLAGER_ROOM_CHEST = new ResourceLocation(MODID, "chests/zombie_villager_room");

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
	private static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
	private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.defaultBlockState();

	public ZombieVillagerRoomFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
		BlockPos origin = context.origin();
		RandomSource random = context.random();
		WorldGenLevel worldgenlevel = context.level();
		int halfX = random.nextInt(2) + 3;
		int beginX = -halfX - 1;
		int endX = halfX + 1;
		int halfZ = random.nextInt(2) + 3;
		int beginZ = -halfZ - 1;
		int endZ = halfZ + 1;
		int cnt = 0;

		for(int x = beginX; x <= endX; ++x) {
			for(int y = -1; y <= 4; ++y) {
				for(int z = beginZ; z <= endZ; ++z) {
					BlockPos current = origin.offset(x, y, z);
					boolean flag = worldgenlevel.getBlockState(current).isSolid();
					if (y == -1 && !flag) {
						return false;
					}

					if (y == 4 && !flag) {
						return false;
					}

					if ((x == beginX || x == endX || z == beginZ || z == endZ) && y == 0 && worldgenlevel.isEmptyBlock(current) && worldgenlevel.isEmptyBlock(current.above())) {
						++cnt;
					}
				}
			}
		}

		if (cnt >= 1 && cnt <= 5) {
			for(int x = beginX; x <= endX; ++x) {
				for(int y = 3; y >= -1; --y) {
					for(int z = beginZ; z <= endZ; ++z) {
						BlockPos current = origin.offset(x, y, z);
						BlockState blockstate = worldgenlevel.getBlockState(current);
						if (x != beginX && y != -1 && z != beginZ && x != endX && y != 4 && z != endZ) {
							if (!blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.SPAWNER)) {
								this.safeSetBlock(worldgenlevel, current, AIR, predicate);
							}
						} else if (current.getY() >= worldgenlevel.getMinBuildHeight() && !worldgenlevel.getBlockState(current.below()).isSolid()) {
							worldgenlevel.setBlock(current, AIR, Block.UPDATE_CLIENTS);
						} else if (blockstate.isSolid() && !blockstate.is(Blocks.CHEST)) {
							if (y == -1 && random.nextInt(2) != 0) {
								this.safeSetBlock(worldgenlevel, current, MOSSY_COBBLESTONE, predicate);
							} else {
								this.safeSetBlock(worldgenlevel, current, COBBLESTONE, predicate);
							}
						}
					}
				}
			}

			for(int l3 = 0; l3 < 2; ++l3) {
				for(int j4 = 0; j4 < 3; ++j4) {
					int x = origin.getX() + random.nextInt(halfX * 2 + 1) - halfX;
					int y = origin.getY();
					int z = origin.getZ() + random.nextInt(halfZ * 2 + 1) - halfZ;
					BlockPos current = new BlockPos(x, y, z);
					if (worldgenlevel.isEmptyBlock(current)) {
						int solidCount = 0;

						for(Direction direction : Direction.Plane.HORIZONTAL) {
							if (worldgenlevel.getBlockState(current.relative(direction)).isSolid()) {
								++solidCount;
							}
						}

						if (solidCount == 1) {
							this.safeSetBlock(worldgenlevel, current, StructurePiece.reorient(worldgenlevel, current, Blocks.CHEST.defaultBlockState()), predicate);
							RandomizableContainerBlockEntity.setLootTable(worldgenlevel, random, current, ZOMBIE_VILLAGER_ROOM_CHEST);
							break;
						}
					}
				}
			}

			this.safeSetBlock(worldgenlevel, origin, Blocks.SPAWNER.defaultBlockState(), predicate);
			BlockEntity blockentity = worldgenlevel.getBlockEntity(origin);
			if (blockentity instanceof SpawnerBlockEntity) {
				((SpawnerBlockEntity)blockentity).setEntityId(EntityType.ZOMBIE_VILLAGER, random);
			} else {
				ECLogger.error("Failed to fetch mob spawner entity at ({}, {}, {})", origin.getX(), origin.getY(), origin.getZ());
			}

			return true;
		}
		return false;
	}
}