package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ZombieVillagerRoomFeature extends Feature<NoFeatureConfig> {
	private static final ResourceLocation ZOMBIE_VILLAGER_ROOM_CHEST = new ResourceLocation(MODID, "chests/zombie_villager_room");

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
	private static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
	private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.defaultBlockState();

	public ZombieVillagerRoomFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(@Nonnull ISeedReader level, @Nonnull ChunkGenerator chunkGenerator, Random random,
						 @Nonnull BlockPos origin, @Nonnull NoFeatureConfig config) {
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
					BlockPos blockpos1 = origin.offset(x, y, z);
					Material material = level.getBlockState(blockpos1).getMaterial();
					boolean flag = material.isSolid();
					if (y == -1 && !flag) {
						return false;
					}

					if (y == 4 && !flag) {
						return false;
					}

					if ((x == beginX || x == endX || z == beginZ || z == endZ) && y == 0 && level.isEmptyBlock(blockpos1) && level.isEmptyBlock(blockpos1.above())) {
						++cnt;
					}
				}
			}
		}

		if (cnt >= 1 && cnt <= 5) {
			for(int x = beginX; x <= endX; ++x) {
				for(int y = 3; y >= -1; --y) {
					for(int z = beginZ; z <= endZ; ++z) {
						BlockPos blockpos2 = origin.offset(x, y, z);
						BlockState blockstate = level.getBlockState(blockpos2);
						if (x != beginX && y != -1 && z != beginZ && x != endX && y != 4 && z != endZ) {
							if (!blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.SPAWNER)) {
								level.setBlock(blockpos2, AIR, 2);
							}
						} else if (blockpos2.getY() >= 0 && !level.getBlockState(blockpos2.below()).getMaterial().isSolid()) {
							level.setBlock(blockpos2, AIR, 2);
						} else if (blockstate.getMaterial().isSolid() && !blockstate.is(Blocks.CHEST)) {
							if (y == -1 && random.nextInt(2) != 0) {
								level.setBlock(blockpos2, MOSSY_COBBLESTONE, 2);
							} else {
								level.setBlock(blockpos2, COBBLESTONE, 2);
							}
						}
					}
				}
			}

			for(int l3 = 0; l3 < 2; ++l3) {
				for(int j4 = 0; j4 < 3; ++j4) {
					int l4 = origin.getX() + random.nextInt(halfX * 2 + 1) - halfX;
					int i5 = origin.getY();
					int j5 = origin.getZ() + random.nextInt(halfZ * 2 + 1) - halfZ;
					BlockPos blockpos3 = new BlockPos(l4, i5, j5);
					if (level.isEmptyBlock(blockpos3)) {
						int j3 = 0;

						for(Direction direction : Direction.Plane.HORIZONTAL) {
							if (level.getBlockState(blockpos3.relative(direction)).getMaterial().isSolid()) {
								++j3;
							}
						}

						if (j3 == 1) {
							level.setBlock(blockpos3, StructurePiece.reorient(level, blockpos3, Blocks.CHEST.defaultBlockState()), 2);
							LockableLootTileEntity.setLootTable(level, random, blockpos3, ZOMBIE_VILLAGER_ROOM_CHEST);
							break;
						}
					}
				}
			}

			level.setBlock(origin, Blocks.SPAWNER.defaultBlockState(), 2);
			TileEntity blockentity = level.getBlockEntity(origin);
			if (blockentity instanceof MobSpawnerTileEntity) {
				((MobSpawnerTileEntity)blockentity).getSpawner().setEntityId(EntityType.ZOMBIE_VILLAGER);
			} else {
				ECLogger.error("Failed to fetch mob spawner entity at ({}, {}, {})", origin.getX(), origin.getY(), origin.getZ());
			}

			return true;
		} else {
			return false;
		}
	}
}