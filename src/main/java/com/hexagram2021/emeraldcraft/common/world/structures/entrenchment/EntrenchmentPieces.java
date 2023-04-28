package com.hexagram2021.emeraldcraft.common.world.structures.entrenchment;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.register.ECStructurePieceTypes.EntrenchmentPieceTypes.*;

public class EntrenchmentPieces {
	static class PieceWeight {
		public final Class<? extends EntrenchmentPiece> pieceClass;
		private final Function<Integer, Integer> weight;
		public int placeCount;
		public final int maxPlaceCount;

		public PieceWeight(Class<? extends EntrenchmentPiece> clazz, Function<Integer, Integer> weight, int maxCount) {
			this.pieceClass = clazz;
			this.weight = weight;
			this.maxPlaceCount = maxCount;
		}

		public PieceWeight(Class<? extends EntrenchmentPiece> clazz, int weight, int maxCount) {
			this.pieceClass = clazz;
			this.weight = count -> weight;
			this.maxPlaceCount = maxCount;
		}

		public int getWeight() {
			return this.weight.apply(this.placeCount);
		}

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean isValid() {
			return this.maxPlaceCount <= 0 || this.placeCount < this.maxPlaceCount;
		}
	}

	private static sealed abstract class EntrenchmentPiece extends StructurePiece permits
			HallPiece, CrossingPiece, StraightHallwayPiece, TurnPiece, MonsterRoomPiece,
			LaboratoryPiece, BalconyPiece, PortalRoomPiece, WallPiece, KitchenPiece {
		protected DoorType entryDoor = DoorType.OPENING;

		protected EntrenchmentPiece(StructurePieceType type, int depth, BoundingBox bbox) {
			super(type, depth, bbox);
		}
		protected EntrenchmentPiece(StructurePieceType type, CompoundTag nbt) {
			super(type, nbt);
			this.entryDoor = DoorType.valueOf(nbt.getString("EntryDoor"));
		}

		private static EntrenchmentPiece findAndCreateEntrenchmentPieceFactory(Class<? extends EntrenchmentPiece> clazz, StructurePieceAccessor pieces,
																			   Random random, int x, int y, int z, @Nullable Direction direction, int depth) {
			EntrenchmentPiece ret = null;
			if(clazz == HallPiece.class) {
				ret = HallPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == CrossingPiece.class) {
				ret = CrossingPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == TBridgePiece.class) {
				ret = TBridgePiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == LongHallwayPiece.class) {
				ret = LongHallwayPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == ShortHallwayPiece.class) {
				ret = ShortHallwayPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == ChestHallwayPiece.class) {
				ret = ChestHallwayPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == ShortBridgeHallwayPiece.class) {
				ret = ShortBridgeHallwayPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == LeftTurnPiece.class) {
				ret = LeftTurnPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == RightTurnPiece.class) {
				ret = RightTurnPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == MonsterRoomPiece.class) {
				ret = MonsterRoomPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == LaboratoryPiece.class) {
				ret = LaboratoryPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == BalconyPiece.class) {
				ret = BalconyPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == PortalRoomPiece.class) {
				ret = PortalRoomPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == WallPiece.class) {
				ret = WallPiece.createPiece(pieces, random, x, y, z, direction, depth);
			} else if(clazz == KitchenPiece.class) {
				ret = KitchenPiece.createPiece(pieces, random, x, y, z, direction, depth);
			}
			return ret;
		}

		@Override
		protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, @NotNull CompoundTag nbt) {
			nbt.putString("EntryDoor", this.entryDoor.name());
		}

		private int updatePieceWeight(List<PieceWeight> weights) {
			boolean flag = false;
			int i = 0;

			for(PieceWeight pieceWeight : weights) {
				if (pieceWeight.maxPlaceCount > 0 && pieceWeight.placeCount < pieceWeight.maxPlaceCount) {
					flag = true;
				}

				i += pieceWeight.getWeight();
			}

			return flag ? i : -1;
		}

		@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
		@Nullable
		protected StructurePiece generateChildForward(StartPiece startPiece, StructurePieceAccessor pieces,
													  Random random, int xOffset, int yOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() + xOffset, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() - 1, direction, this.getGenDepth());
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() + xOffset, this.boundingBox.minY() + yOffset, this.boundingBox.maxZ() + 1, direction, this.getGenDepth());
					case WEST:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + xOffset, direction, this.getGenDepth());
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + xOffset, direction, this.getGenDepth());
				}
			}

			return null;
		}

		@SuppressWarnings("UnusedReturnValue")
		@Nullable
		protected StructurePiece generateChildLeft(StartPiece startPiece, StructurePieceAccessor pieces,
												   Random random, int yOffset, int zOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + zOffset, Direction.WEST, this.getGenDepth());
					case WEST:
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() + zOffset, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() - 1, Direction.NORTH, this.getGenDepth());
				}
			}

			return null;
		}

		@SuppressWarnings("UnusedReturnValue")
		@Nullable
		protected StructurePiece generateChildRight(StartPiece startPiece, StructurePieceAccessor pieces,
													Random random, int yOffset, int zOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + zOffset, Direction.EAST, this.getGenDepth());
					case WEST:
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, random, this.boundingBox.minX() + zOffset, this.boundingBox.minY() + yOffset, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.getGenDepth());
				}
			}

			return null;
		}

		@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unused"})
		@Nullable
		protected StructurePiece generateChildForward(StartPiece startPiece, StructurePieceAccessor pieces,
													  Class<? extends EntrenchmentPiece> newPieceClass, Random random, int xOffset, int yOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() + xOffset, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() - 1, direction, this.getGenDepth());
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() + xOffset, this.boundingBox.minY() + yOffset, this.boundingBox.maxZ() + 1, direction, this.getGenDepth());
					case WEST:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + xOffset, direction, this.getGenDepth());
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + xOffset, direction, this.getGenDepth());
				}
			}

			return null;
		}

		@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unused"})
		@Nullable
		protected StructurePiece generateChildLeft(StartPiece startPiece, StructurePieceAccessor pieces,
												   Class<? extends EntrenchmentPiece> newPieceClass, Random random, int yOffset, int zOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + zOffset, Direction.WEST, this.getGenDepth());
					case WEST:
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() + zOffset, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() - 1, Direction.NORTH, this.getGenDepth());
				}
			}

			return null;
		}

		@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
		@Nullable
		protected StructurePiece generateChildRight(StartPiece startPiece, StructurePieceAccessor pieces,
													Class<? extends EntrenchmentPiece> newPieceClass, Random random, int yOffset, int zOffset) {
			Direction direction = this.getOrientation();
			if (direction != null) {
				switch(direction) {
					case NORTH:
					case SOUTH:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOffset, this.boundingBox.minZ() + zOffset, Direction.EAST, this.getGenDepth());
					case WEST:
					case EAST:
						return this.generateAndAddPiece(startPiece, pieces, newPieceClass, random, this.boundingBox.minX() + zOffset, this.boundingBox.minY() + yOffset, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.getGenDepth());
				}
			}

			return null;
		}

		@Override
		public abstract void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random);

		@Nullable
		private EntrenchmentPiece generatePiece(StartPiece startPiece, List<PieceWeight> weights, StructurePieceAccessor pieces, Random random,
												int x, int y, int z, Direction direction, int genDepth) {
			int i = this.updatePieceWeight(weights);
			boolean flag = i > 0 && genDepth <= 30;
			int j = 0;

			while(j < 5 && flag) {
				++j;
				int k = random.nextInt(i);

				for(PieceWeight pieceWeight : weights) {
					k -= pieceWeight.getWeight();
					if (k < 0) {
						if (!pieceWeight.isValid() || pieceWeight == startPiece.previousPiece) {
							break;
						}

						EntrenchmentPiece piece = findAndCreateEntrenchmentPieceFactory(pieceWeight.pieceClass, pieces, random, x, y, z, direction, genDepth);
						if (piece != null) {
							++pieceWeight.placeCount;
							startPiece.previousPiece = pieceWeight;
							if (!pieceWeight.isValid()) {
								weights.remove(pieceWeight);
							}

							return piece;
						}
					}
				}
			}

			return WallPiece.createPiece(pieces, random, x, y, z, direction, genDepth);
		}

		private static final List<PieceWeight> availablePieces = Lists.newArrayList(
				new PieceWeight(HallPiece.class, count -> 25 - count * count, 5),
				new PieceWeight(CrossingPiece.class, 20, 0),
				new PieceWeight(LongHallwayPiece.class, 40, 0),
				new PieceWeight(ShortHallwayPiece.class, 50, 0),
				new PieceWeight(ChestHallwayPiece.class, count -> 30 - count * 5, 5),
				new PieceWeight(LeftTurnPiece.class, 25, 0),
				new PieceWeight(RightTurnPiece.class, 25, 0),
				new PieceWeight(MonsterRoomPiece.class, count -> 10 + count * 20, 2),
				new PieceWeight(LaboratoryPiece.class, 10, 2),
				new PieceWeight(BalconyPiece.class, 25, 0),
				new PieceWeight(PortalRoomPiece.class, 10, 0),
				new PieceWeight(WallPiece.class, count -> 5 + 5 * count, 0),
				new PieceWeight(KitchenPiece.class, 20, 1)
		);

		protected List<PieceWeight> AvailablePieces() {
			return availablePieces;
		}

		private StructurePiece generateAndAddPiece(StartPiece startPiece, StructurePieceAccessor pieces, Random random, int x, int y, int z,
												   Direction direction, int genDepth) {
			if (Math.abs(x - startPiece.getBoundingBox().minX()) <= 112 && Math.abs(z - startPiece.getBoundingBox().minZ()) <= 112 && genDepth < 50) {
				List<PieceWeight> list = this.AvailablePieces();

				EntrenchmentPiece entrenchmentPiece = this.generatePiece(startPiece, list, pieces, random, x, y, z, direction, genDepth + 1);
				if (entrenchmentPiece != null) {
					pieces.addPiece(entrenchmentPiece);
					startPiece.pendingChildren.add(entrenchmentPiece);
				}

				return entrenchmentPiece;
			}
			return null;
		}

		private StructurePiece generateAndAddPiece(StartPiece startPiece, StructurePieceAccessor pieces, Class<? extends EntrenchmentPiece> pieceClass,
												   Random random, int x, int y, int z, Direction direction, int genDepth) {
			if (Math.abs(x - startPiece.getBoundingBox().minX()) <= 112 && Math.abs(z - startPiece.getBoundingBox().minZ()) <= 112 && genDepth < 50) {
				EntrenchmentPiece newPiece = findAndCreateEntrenchmentPieceFactory(pieceClass, pieces, random, x, y, z, direction, genDepth);
				if (newPiece != null) {
					pieces.addPiece(newPiece);
					startPiece.pendingChildren.add(newPiece);
				}

				return newPiece;
			}
			return null;
		}

		protected static boolean isOkBox(BoundingBox bbox) {
			return bbox != null && bbox.minY() > 10;
		}

		protected DoorType randomSmallDoor(Random random) {
			int i = random.nextInt(8);
			return switch (i) {
				case 2, 3, 4 -> DoorType.WOOD_DOOR;
				case 5, 6 -> DoorType.GOLD_WALL;
				case 7 -> DoorType.PISTON_DOOR;
				default -> DoorType.OPENING;
			};
		}

		//3x3x2
		protected void generateSmallDoor(WorldGenLevel level, BoundingBox bbox, DoorType doorType, int x, int y, int z) {
			final BlockState PURPURACEUS_DOOR = ECBlocks.TO_DOOR.get(ECBlocks.Plant.PURPURACEUS_PLANKS.getId()).defaultBlockState();
			final BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			final BlockState GOLD_WALL = ECBlocks.TO_WALL.get(Blocks.GOLD_BLOCK.getRegistryName()).defaultBlockState();
			switch (doorType) {
				case OPENING ->
					this.generateBox(level, bbox, x, y, z, x + 3 - 1, y + 3 - 1, z + 1, CAVE_AIR, CAVE_AIR, false);
				case WOOD_DOOR -> {
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y + 1, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y + 2, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 1, y + 2, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y + 1, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y + 2, z, bbox);
					this.placeBlock(level, PURPURACEUS_DOOR, x + 1, y, z, bbox);
					this.placeBlock(level, PURPURACEUS_DOOR.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), x + 1, y + 1, z, bbox);
					this.generateBox(level, bbox, x, y, z + 1, x + 3 - 1, y + 3 - 1, z + 1, CAVE_AIR, CAVE_AIR, false);
				}
				case GOLD_WALL -> {
					this.placeBlock(level, CAVE_AIR, x + 1, y, z, bbox);
					this.placeBlock(level, CAVE_AIR, x + 1, y + 1, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.WEST_WALL, WallSide.TALL), x, y, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.WEST_WALL, WallSide.TALL), x, y + 1, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.WEST_WALL, WallSide.TALL).setValue(WallBlock.EAST_WALL, WallSide.TALL).setValue(WallBlock.UP, Boolean.FALSE), x, y + 2, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.WEST_WALL, WallSide.TALL).setValue(WallBlock.EAST_WALL, WallSide.TALL).setValue(WallBlock.UP, Boolean.FALSE), x + 1, y + 2, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.WEST_WALL, WallSide.TALL).setValue(WallBlock.EAST_WALL, WallSide.TALL).setValue(WallBlock.UP, Boolean.FALSE), x + 2, y + 2, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.EAST_WALL, WallSide.TALL), x + 2, y + 1, z, bbox);
					this.placeBlock(level, GOLD_WALL.setValue(WallBlock.EAST_WALL, WallSide.TALL), x + 2, y, z, bbox);
					this.generateBox(level, bbox, x, y, z + 1, x + 3 - 1, y + 3 - 1, z + 1, CAVE_AIR, CAVE_AIR, false);
				}
				case PISTON_DOOR -> {
					this.placeBlock(level, CAVE_AIR, x + 1, y, z, bbox);
					this.placeBlock(level, CAVE_AIR, x + 1, y + 1, z, bbox);
					this.placeBlock(level, Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, 15), x - 1, y + 2, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y + 1, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x, y + 2, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 1, y + 2, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y + 1, z, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 2, y + 2, z, bbox);
					this.placeBlock(level, Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING, Direction.SOUTH).setValue(LeverBlock.POWERED, Boolean.TRUE), x, y + 2, z - 1, bbox);
					this.placeBlock(level, Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.EAST).setValue(PistonBaseBlock.EXTENDED, Boolean.TRUE), x - 1, y, z + 1, bbox);
					this.placeBlock(level, Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, Direction.EAST).setValue(PistonBaseBlock.EXTENDED, Boolean.TRUE), x - 1, y + 1, z + 1, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x - 1, y + 2, z + 1, bbox);
					this.placeBlock(level, Blocks.PISTON_HEAD.defaultBlockState().setValue(PistonHeadBlock.FACING, Direction.EAST).setValue(PistonHeadBlock.TYPE, PistonType.STICKY), x, y, z + 1, bbox);
					this.placeBlock(level, Blocks.PISTON_HEAD.defaultBlockState().setValue(PistonHeadBlock.FACING, Direction.EAST).setValue(PistonHeadBlock.TYPE, PistonType.STICKY), x, y + 1, z + 1, bbox);
					this.placeBlock(level, CAVE_AIR, x, y + 2, z + 1, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 1, y, z + 1, bbox);
					this.placeBlock(level, PURPLE_NETHER_BRICKS, x + 1, y + 1, z + 1, bbox);
					this.placeBlock(level, CAVE_AIR, x + 1, y + 2, z + 1, bbox);
					this.placeBlock(level, CAVE_AIR, x + 2, y, z + 1, bbox);
					this.placeBlock(level, CAVE_AIR, x + 2, y + 1, z + 1, bbox);
					this.placeBlock(level, CAVE_AIR, x + 2, y + 2, z + 1, bbox);
				}
				case BLOCKED ->
					this.generateBox(level, bbox, x, y, z, x + 3 - 1, y + 3 - 1, z, PURPLE_NETHER_BRICKS, PURPLE_NETHER_BRICKS, false);
			}
		}
	}

	public enum DoorType {
		WOOD_DOOR,
		GOLD_WALL,
		PISTON_DOOR,
		OPENING,
		BLOCKED
	}

	public static sealed class HallPiece extends EntrenchmentPiece permits StartPiece {
		private static final int WIDTH = 9;
		private static final int HEIGHT = 8;
		private static final int LENGTH = 11;

		private static final int OFF_X = 4;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		private final boolean left_low;
		private final boolean left_high;
		private final boolean right_low;
		private final boolean right_high;
		private final boolean right_hole;

		public HallPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			this(HALL_TYPE, depth, random, bbox, direction);
		}

		public HallPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
			this(HALL_TYPE, context, nbt);
		}

		protected HallPiece(StructurePieceType type, int depth, Random random, BoundingBox bbox, Direction direction) {
			super(type, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);

			this.left_low = random.nextInt(4) == 0;
			this.left_high = random.nextInt(3) != 0;
			this.right_low = random.nextBoolean();
			this.right_high = random.nextInt(5) <= 2;
			this.right_hole = random.nextInt(8) != 0;
		}

		protected HallPiece(StructurePieceType type, @SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(type, nbt);

			this.left_low = nbt.getBoolean("leftLow");
			this.left_high = nbt.getBoolean("leftHigh");
			this.right_low = nbt.getBoolean("rightLow");
			this.right_high = nbt.getBoolean("rightHigh");
			this.right_hole = nbt.getBoolean("rightHole");
		}

		@Override
		protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, @NotNull CompoundTag nbt) {
			super.addAdditionalSaveData(context, nbt);
			nbt.putBoolean("leftLow", this.left_low);
			nbt.putBoolean("leftHigh", this.left_high);
			nbt.putBoolean("rightLow", this.right_low);
			nbt.putBoolean("rightHigh", this.right_high);
			nbt.putBoolean("rightHole", this.right_hole);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState PURPLE_NETHER_BRICK_STAIRS = ECBlocks.TO_STAIRS.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();
			BlockState PURPLE_NETHER_BRICK_SLAB = ECBlocks.TO_SLAB.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();
			BlockState PURPLE_NETHER_BRICK_SLAB_TOP = PURPLE_NETHER_BRICK_SLAB.setValue(SlabBlock.TYPE, SlabType.TOP);
			BlockState UP_STAIRS = PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.NORTH);
			BlockState RIGHT_STAIRS = PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.EAST);

			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);

			this.placeBlock(level, UP_STAIRS, 1, 1, 5, bbox);
			this.placeBlock(level, UP_STAIRS, 2, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 1, 6, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 2, 1, 6, bbox);
			this.placeBlock(level, UP_STAIRS, 1, 2, 6, bbox);
			this.placeBlock(level, UP_STAIRS, 2, 2, 6, bbox);
			this.generateBox(level, bbox, 1, 1, 7, 1, 2, 9, PURPLE_NETHER_BRICKS, CAVE_AIR, false);
			this.placeBlock(level, Blocks.SOUL_TORCH.defaultBlockState(), 2, 1, 8, bbox);
			this.generateBox(level, bbox, 2, 2, 7, 2, 2, 9, PURPLE_NETHER_BRICK_SLAB_TOP, CAVE_AIR, false);
			this.generateBox(level, bbox, 3, 3, 7, 3, 3, 9, PURPLE_NETHER_BRICK_SLAB, CAVE_AIR, false);
			this.generateBox(level, bbox, 4, 3, 7, 6, 3, 9, PURPLE_NETHER_BRICK_SLAB_TOP, CAVE_AIR, false);
			this.generateBox(level, bbox, 6, 4, 7, 6, 4, 9, PURPLE_NETHER_BRICK_SLAB, CAVE_AIR, false);
			this.generateBox(level, bbox, 7, 4, 7, 7, 4, 9, RIGHT_STAIRS, CAVE_AIR, false);

			this.createChest(level, bbox, random, 2, 1, 9, new ResourceLocation(MODID, "chests/entrenchment/hall"));

			this.generateBox(level, bbox, 5, 1, LENGTH - 1, 7, 3, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);

			if (this.left_low) {
				this.generateBox(level, bbox, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
			}
			if (this.left_high) {
				this.generateBox(level, bbox, 0, 3, 7, 0, 5, 9, CAVE_AIR, CAVE_AIR, false);
			}
			if (this.right_hole) {
				this.generateBox(level, bbox, WIDTH - 1, 1, 7, WIDTH - 1, 3, 9, CAVE_AIR, CAVE_AIR, false);
			}
			if (this.right_low) {
				this.generateBox(level, bbox, WIDTH - 1, 1, 1, WIDTH - 1, 3, 3, CAVE_AIR, CAVE_AIR, false);
			}
			if (this.right_high) {
				this.generateBox(level, bbox, WIDTH - 1, 5, 7, WIDTH - 1, 7, 9, CAVE_AIR, CAVE_AIR, false);
			}

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static HallPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new HallPiece(depth, random, boundingbox, direction) : null;
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildForward((StartPiece)structurePiece, pieces, random, 5, 1);
			if (this.left_low) {
				this.generateChildLeft((StartPiece)structurePiece, pieces, random, 1, 1);
			}
			if (this.left_high) {
				this.generateChildLeft((StartPiece)structurePiece, pieces, random, 3, 7);
			}
			if (this.right_hole) {
				if(this.right_high) {
					this.generateChildRight((StartPiece)structurePiece, pieces, TBridgePiece.class, random, 1, 7);
				} else {
					this.generateChildRight((StartPiece)structurePiece, pieces, random, 1, 7);
				}
			}
			if (this.right_low) {
				this.generateChildRight((StartPiece)structurePiece, pieces, random, 1, 1);
			}
			if (this.right_high) {
				this.generateChildRight((StartPiece)structurePiece, pieces, random, 5, 7);
			}
		}
	}

	public static final class StartPiece extends HallPiece {
		private static final int WIDTH = 9;
		private static final int HEIGHT = 8;
		private static final int LENGTH = 11;

		public PieceWeight previousPiece;

		public final List<StructurePiece> pendingChildren = Lists.newArrayList();

		public StartPiece(Random random, int x, int z) {
			this(random, x, z, getRandomHorizontalDirection(random));
		}

		private StartPiece(Random random, int x, int z, Direction direction) {
			super(START_TYPE, 0, random, makeBoundingBox(x, 16, z, direction, WIDTH, HEIGHT, LENGTH), direction);
			this.entryDoor = DoorType.BLOCKED;
		}

		public StartPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
			super(START_TYPE, context, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			super.postProcess(level, manager, chunk, random, bbox, chunkPos, blockPos);
			this.createChest(level, bbox, random, 4, 1, 1, new ResourceLocation(MODID, "chests/entrenchment/start"));
		}
	}

	public static sealed class CrossingPiece extends EntrenchmentPiece permits TBridgePiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 5;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public CrossingPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			this(CROSSING_TYPE, depth, random, bbox, direction);
		}

		public CrossingPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
			this(CROSSING_TYPE, context, nbt);
		}

		protected CrossingPiece(StructurePieceType type, int depth, Random random, BoundingBox bbox, Direction direction) {
			super(type, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		protected CrossingPiece(StructurePieceType type, @SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(type, nbt);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildForward((StartPiece) structurePiece, pieces, random, 1, 1);
			this.generateChildLeft((StartPiece) structurePiece, pieces, random, 1, 1);
			this.generateChildRight((StartPiece) structurePiece, pieces, random, 1, 1);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState(), CAVE_AIR, true);

			this.generateBox(level, bbox, 1, 1, LENGTH - 1, 3, 3, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);
			this.generateBox(level, bbox, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
			this.generateBox(level, bbox, WIDTH - 1, 1, 1, WIDTH - 1, 3, 3, CAVE_AIR, CAVE_AIR, false);
			this.placeBlock(level, Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, Boolean.TRUE), 2, 3, 2, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static CrossingPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new CrossingPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class TBridgePiece extends CrossingPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 4;
		private static final int LENGTH = 5;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public TBridgePiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(T_BRIDGE_TYPE, depth, random, bbox, direction);
		}

		public TBridgePiece(StructurePieceSerializationContext context, CompoundTag nbt) {
			super(T_BRIDGE_TYPE, context, nbt);
		}

		private static final List<PieceWeight> TBridgeAvailablePieces = Lists.newArrayList(
				new PieceWeight(ShortBridgeHallwayPiece.class, 35, 0),
				new PieceWeight(BalconyPiece.class, 15, 0)
		);

		@Override
		protected List<PieceWeight> AvailablePieces() {
			return TBridgeAvailablePieces;
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildLeft((StartPiece) structurePiece, pieces, random, 1, 1);
			this.generateChildRight((StartPiece) structurePiece, pieces, random, 1, 1);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, 0, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);
			this.generateBox(level, bbox, 0, 1, LENGTH - 1, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);
			this.generateBox(level, bbox, 0, 1, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 2, CAVE_AIR, CAVE_AIR, true);

			this.placeBlock(level, PURPLE_NETHER_BRICKS, 0, 1, 0, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, WIDTH - 1, 1, 0, bbox);

			this.placeBlock(level, Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, Boolean.TRUE), 2, 3, 2, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static TBridgePiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new TBridgePiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static abstract sealed class StraightHallwayPiece extends EntrenchmentPiece permits LongHallwayPiece, ShortHallwayPiece {
		protected StraightHallwayPiece(StructurePieceType type, int depth, BoundingBox bbox, Direction direction) {
			super(type, depth, bbox);
			this.setOrientation(direction);
		}

		protected StraightHallwayPiece(StructurePieceType type, CompoundTag nbt) {
			super(type, nbt);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildForward((StartPiece) structurePiece, pieces, random, 1, 1);
		}
	}

	public static final class LongHallwayPiece extends StraightHallwayPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 12;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public LongHallwayPiece(int depth, BoundingBox bbox, Direction direction) {
			super(LONG_HALLWAY_TYPE, depth, bbox, direction);
		}

		public LongHallwayPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(LONG_HALLWAY_TYPE, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState(), CAVE_AIR, true);

			this.generateBox(level, bbox, 1, 1, LENGTH - 1, 3, 3, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);
			this.placeBlock(level, Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST), 1, 3, 5, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static LongHallwayPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new LongHallwayPiece(depth, boundingbox, direction) : null;
		}
	}

	public static sealed class ShortHallwayPiece extends StraightHallwayPiece permits ChestHallwayPiece, ShortBridgeHallwayPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 7;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public ShortHallwayPiece(int depth, BoundingBox bbox, Direction direction) {
			super(SHORT_HALLWAY_TYPE, depth, bbox, direction);
		}

		public ShortHallwayPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(SHORT_HALLWAY_TYPE, nbt);
		}

		protected ShortHallwayPiece(StructurePieceType type, int depth, BoundingBox bbox, Direction direction) {
			super(type, depth, bbox, direction);
		}

		protected ShortHallwayPiece(StructurePieceType type, CompoundTag nbt) {
			super(type, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState(), CAVE_AIR, true);

			this.generateBox(level, bbox, 1, 1, LENGTH - 1, 3, 3, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);
			this.placeBlock(level, Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, Boolean.TRUE), 1, 3, 3, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static ShortHallwayPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new ShortHallwayPiece(depth, boundingbox, direction) : null;
		}
	}

	public static final class ChestHallwayPiece extends ShortHallwayPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 7;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public ChestHallwayPiece(int depth, BoundingBox bbox, Direction direction) {
			super(CHEST_HALLWAY_TYPE, depth, bbox, direction);
		}

		public ChestHallwayPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(CHEST_HALLWAY_TYPE, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			super.postProcess(level, manager, chunk, random, bbox, chunkPos, blockPos);

			final BlockState PURPLE_NETHER_STAIRS = ECBlocks.TO_STAIRS.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();
			final BlockState PURPLE_NETHER_SLAB = ECBlocks.TO_SLAB.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();

			this.placeBlock(level, PURPLE_NETHER_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.TOP), 3, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_STAIRS.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.TOP), 3, 1, 4, bbox);
			this.placeBlock(level, PURPLE_NETHER_SLAB.setValue(SlabBlock.TYPE, SlabType.DOUBLE), 3, 1, 3, bbox);
			this.placeBlock(level, PURPLE_NETHER_SLAB, 3, 2, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_SLAB, 3, 2, 4, bbox);
			this.placeBlock(level, PURPLE_NETHER_SLAB.setValue(SlabBlock.TYPE, SlabType.TOP), 3, 3, 3, bbox);
			this.createChest(level, bbox, random, 3, 2, 3, new ResourceLocation(MODID, "chests/entrenchment/chest_hallway"));
		}

		public static ChestHallwayPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new ChestHallwayPiece(depth, boundingbox, direction) : null;
		}
	}

	public static final class ShortBridgeHallwayPiece extends ShortHallwayPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 4;
		private static final int LENGTH = 7;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public ShortBridgeHallwayPiece(int depth, BoundingBox bbox, Direction direction) {
			super(BRIDGE_HALLWAY_TYPE, depth, bbox, direction);
		}

		public ShortBridgeHallwayPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(BRIDGE_HALLWAY_TYPE, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState SOUL_TORCH = Blocks.SOUL_TORCH.defaultBlockState();
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, 0, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);
			this.generateBox(level, bbox, 0, 1, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);
			this.generateBox(level, bbox, 0, 1, 0, 0, 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);
			this.generateBox(level, bbox, WIDTH - 1, 1, 0, WIDTH - 1, 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);

			this.placeBlock(level, SOUL_TORCH, 0, 2, 3, bbox);
			this.placeBlock(level, SOUL_TORCH, WIDTH - 1, 2, 3, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static ShortBridgeHallwayPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new ShortBridgeHallwayPiece(depth, boundingbox, direction) : null;
		}
	}

	public static abstract sealed class TurnPiece extends EntrenchmentPiece permits LeftTurnPiece, RightTurnPiece {
		protected TurnPiece(StructurePieceType type, int depth, Random random, BoundingBox bbox, Direction direction) {
			super(type, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		protected TurnPiece(StructurePieceType type, CompoundTag nbt) {
			super(type, nbt);
		}
	}

	public static final class LeftTurnPiece extends TurnPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 5;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public LeftTurnPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(LEFT_TURN_TYPE, depth, random, bbox, direction);
		}

		public LeftTurnPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(LEFT_TURN_TYPE, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState(), CAVE_AIR, true);

			this.generateBox(level, bbox, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildLeft((StartPiece) structurePiece, pieces, random, 1, 1);
		}

		public static LeftTurnPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new LeftTurnPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class RightTurnPiece extends TurnPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 5;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public RightTurnPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(RIGHT_TURN_TYPE, depth, random, bbox, direction);
		}

		public RightTurnPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(RIGHT_TURN_TYPE, nbt);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState(), CAVE_AIR, true);

			this.generateBox(level, bbox, WIDTH - 1, 1, 1, WIDTH - 1, 3, 3, CAVE_AIR, CAVE_AIR, false);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildRight((StartPiece) structurePiece, pieces, random, 1, 1);
		}

		public static RightTurnPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new RightTurnPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class MonsterRoomPiece extends EntrenchmentPiece {
		private static final int WIDTH = 9;
		private static final int HEIGHT = 6;
		private static final int LENGTH = 9;

		private static final int OFF_X = 3;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		private boolean hasPlacedSpawner;

		public MonsterRoomPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(MONSTER_ROOM_TYPE, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		public MonsterRoomPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(MONSTER_ROOM_TYPE, nbt);
			this.hasPlacedSpawner = nbt.getBoolean("Mob");
		}

		@Override
		protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, @NotNull CompoundTag nbt) {
			super.addAdditionalSaveData(context, nbt);
			nbt.putBoolean("Mob", this.hasPlacedSpawner);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState PURPLE_NETHER_BRICK_STAIRS = ECBlocks.TO_STAIRS.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();
			BlockState IRON_SLAB = ECBlocks.TO_SLAB.get(Blocks.IRON_BLOCK.getRegistryName()).defaultBlockState();
			BlockState IRON_BARS = Blocks.IRON_BARS.defaultBlockState();

			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, true);

			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.EAST), 3, 1, 4, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 3, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.NORTH), 4, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 5, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.WEST), 5, 1, 4, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 5, 1, 3, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH), 4, 1, 3, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 3, 1, 3, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 4, 1, 4, bbox);

			this.generateBox(level, bbox, 3, 1, 2, 5, 3, 2, IRON_BARS.setValue(IronBarsBlock.EAST, Boolean.TRUE).setValue(IronBarsBlock.WEST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 3, 1, 6, 5, 3, 6, IRON_BARS.setValue(IronBarsBlock.EAST, Boolean.TRUE).setValue(IronBarsBlock.WEST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 2, 1, 3, 2, 3, 5, IRON_BARS.setValue(IronBarsBlock.NORTH, Boolean.TRUE).setValue(IronBarsBlock.SOUTH, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 6, 1, 3, 6, 3, 5, IRON_BARS.setValue(IronBarsBlock.NORTH, Boolean.TRUE).setValue(IronBarsBlock.SOUTH, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 6, 1, 2, 6, 3, 2, IRON_BARS.setValue(IronBarsBlock.NORTH, Boolean.TRUE).setValue(IronBarsBlock.WEST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 6, 1, 6, 6, 3, 6, IRON_BARS.setValue(IronBarsBlock.SOUTH, Boolean.TRUE).setValue(IronBarsBlock.WEST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 2, 1, 6, 2, 3, 6, IRON_BARS.setValue(IronBarsBlock.SOUTH, Boolean.TRUE).setValue(IronBarsBlock.EAST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 2, 1, 2, 2, 3, 2, IRON_BARS.setValue(IronBarsBlock.NORTH, Boolean.TRUE).setValue(IronBarsBlock.EAST, Boolean.TRUE), CAVE_AIR, false);
			this.generateBox(level, bbox, 2, 4, 2, 6, 4, 6, IRON_SLAB, CAVE_AIR, false);

			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 2, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 2, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 5, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 2, 5, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 2, 5, 2, bbox);
			this.placeBlock(level, Blocks.LAVA.defaultBlockState(), 1, 5, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 7, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 6, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 6, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 7, 5, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 6, 5, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 6, 5, 2, bbox);
			this.placeBlock(level, Blocks.LAVA.defaultBlockState(), 7, 5, 1, bbox);
			this.placeBlock(level, Blocks.SOUL_LANTERN.defaultBlockState(), 1, 1, 7, bbox);
			this.placeBlock(level, Blocks.SOUL_LANTERN.defaultBlockState(), 7, 1, 7, bbox);

			if (!this.hasPlacedSpawner) {
				BlockPos blockpos = this.getWorldPos(4, 2, 4);
				if (bbox.isInside(blockpos)) {
					this.hasPlacedSpawner = true;
					level.setBlock(blockpos, Blocks.SPAWNER.defaultBlockState(), 2);
					BlockEntity blockentity = level.getBlockEntity(blockpos);
					if (blockentity instanceof SpawnerBlockEntity) {
						((SpawnerBlockEntity)blockentity).getSpawner().setEntityId(ECEntities.WRAITH.get());
					}
				}
			}

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static MonsterRoomPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new MonsterRoomPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class LaboratoryPiece extends EntrenchmentPiece {
		private static final int WIDTH = 11;
		private static final int HEIGHT = 7;
		private static final int LENGTH = 8;

		private static final int OFF_X = 2;
		private static final int OFF_Y = 2;
		private static final int OFF_Z = 0;

		private boolean spawnedPig;
		private boolean spawnedPiglin;
		private boolean spawnedZombifiedPiglin;
		private boolean spawnedPiglinBrute;

		public LaboratoryPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(LABORATORY_TYPE, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		public LaboratoryPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(LABORATORY_TYPE, nbt);
			this.spawnedPig = nbt.getBoolean("SpawnedPig");
			this.spawnedPiglin = nbt.getBoolean("SpawnedPiglin");
			this.spawnedZombifiedPiglin = nbt.getBoolean("SpawnedZombifiedPiglin");
			this.spawnedPiglinBrute = nbt.getBoolean("SpawnedPiglinBrute");
		}

		@Override
		protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, @NotNull CompoundTag nbt) {
			super.addAdditionalSaveData(context, nbt);
			nbt.putBoolean("SpawnedPig", this.spawnedPig);
			nbt.putBoolean("SpawnedPiglin", this.spawnedPiglin);
			nbt.putBoolean("SpawnedZombifiedPiglin", this.spawnedZombifiedPiglin);
			nbt.putBoolean("SpawnedPiglinBrute", this.spawnedPiglinBrute);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState PURPLE_NETHER_BRICK_STAIRS = ECBlocks.TO_STAIRS.get(ECBlocks.Decoration.PURPLE_NETHER_BRICKS.getId()).defaultBlockState();
			BlockState IRON_BARS = Blocks.IRON_BARS.defaultBlockState();
			BlockState IRON_DOOR = Blocks.IRON_DOOR.defaultBlockState();

			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, false);

			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH), 2, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH), 3, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.SOUTH), 4, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 1, 1, 1, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICK_STAIRS.setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 5, 1, 1, bbox);

			BlockState NORTH_PRISON_IRON_BARS = IRON_BARS.setValue(IronBarsBlock.WEST, Boolean.TRUE).setValue(IronBarsBlock.EAST, Boolean.TRUE);
			BlockState EAST_PRISON_IRON_BARS = IRON_BARS.setValue(IronBarsBlock.NORTH, Boolean.TRUE).setValue(IronBarsBlock.SOUTH, Boolean.TRUE);
			BlockState NORTH_PRISON_IRON_DOOR = IRON_DOOR.setValue(DoorBlock.FACING, Direction.NORTH).setValue(DoorBlock.HINGE, DoorHingeSide.RIGHT);
			BlockState EAST_PRISON_IRON_DOOR = IRON_DOOR.setValue(DoorBlock.FACING, Direction.EAST);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 1, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 1, 2, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_DOOR, 2, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_DOOR.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 2, 2, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 3, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 3, 2, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 4, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 4, 2, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 4, 1, 6, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 4, 2, 6, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 5, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 5, 2, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_DOOR, 6, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_DOOR.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 6, 2, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 7, 1, 5, bbox);
			this.placeBlock(level, NORTH_PRISON_IRON_BARS, 7, 2, 5, bbox);
			this.generateBox(level, bbox, 1, 3, 5, 7, 3, 5, PURPLE_NETHER_BRICKS, CAVE_AIR, false);
			this.generateBox(level, bbox, 8, 1, 5, 9, 3, 6, PURPLE_NETHER_BRICKS, CAVE_AIR, false);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 1, 4, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 2, 4, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_DOOR, 8, 1, 3, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_DOOR.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 8, 2, 3, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 1, 2, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 2, 2, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 1, 1, bbox);
			this.placeBlock(level, EAST_PRISON_IRON_BARS, 8, 2, 1, bbox);
			this.createChest(level, bbox, random, 9, 1, 1, new ResourceLocation(MODID, "chests/entrenchment/prison"));
			this.generateBox(level, bbox, 8, 3, 1, 8, 3, 4, PURPLE_NETHER_BRICKS, CAVE_AIR, false);

			this.spawnMobs(level, bbox);

			BlockState SOUL_LANTERN_CEILING = Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, Boolean.TRUE);
			this.placeBlock(level, SOUL_LANTERN_CEILING, 2, 5, 3, bbox);
			this.placeBlock(level, SOUL_LANTERN_CEILING, 6, 5, 3, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		private void spawnMobs(WorldGenLevel level, BoundingBox bbox) {
			if(!this.spawnedZombifiedPiglin) {
				BlockPos blockpos = this.getWorldPos(2, 1, 6);
				if(bbox.isInside(blockpos)) {
					this.spawnedZombifiedPiglin = true;
					ZombifiedPiglin mob = Objects.requireNonNull(EntityType.ZOMBIFIED_PIGLIN.create(level.getLevel()));
					mob.setPersistenceRequired();
					mob.moveTo(blockpos.getX() + 0.5F, blockpos.getY(), blockpos.getZ() + 0.5F, 0.0F, 0.0F);
					mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockpos), MobSpawnType.STRUCTURE, null, null);
					level.addFreshEntityWithPassengers(mob);
				}
			}
			if(!this.spawnedPiglin) {
				BlockPos blockpos = this.getWorldPos(6, 1, 6);
				if(bbox.isInside(blockpos)) {
					this.spawnedPiglin = true;
					Mob mob;
					if(level.getRandom().nextInt(64) == 0) {
						mob = Objects.requireNonNull(ECEntities.PIGLIN_CUTEY.get().create(level.getLevel()));
					} else {
						mob = Objects.requireNonNull(EntityType.PIGLIN.create(level.getLevel()));
					}
					mob.setPersistenceRequired();
					mob.moveTo(blockpos.getX() + 0.5F, blockpos.getY(), blockpos.getZ() + 0.5F, 0.0F, 0.0F);
					mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockpos), MobSpawnType.STRUCTURE, null, null);
					level.addFreshEntityWithPassengers(mob);
				}
			}
			if(!this.spawnedPiglinBrute) {
				BlockPos blockpos = this.getWorldPos(9, 1, 3);
				if(bbox.isInside(blockpos)) {
					this.spawnedPiglinBrute = true;
					PiglinBrute mob = Objects.requireNonNull(EntityType.PIGLIN_BRUTE.create(level.getLevel()));
					mob.setPersistenceRequired();
					mob.moveTo(blockpos.getX() + 0.5F, blockpos.getY(), blockpos.getZ() + 0.5F, 0.0F, 0.0F);
					mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockpos), MobSpawnType.STRUCTURE, null, null);
					level.addFreshEntityWithPassengers(mob);
				}
			}
			if(!this.spawnedPig) {
				BlockPos blockpos = this.getWorldPos(9, 1, 2);
				if(bbox.isInside(blockpos)) {
					this.spawnedPig = true;
					Pig mob = Objects.requireNonNull(EntityType.PIG.create(level.getLevel()));
					mob.setPersistenceRequired();
					mob.moveTo(blockpos.getX() + 0.5F, blockpos.getY(), blockpos.getZ() + 0.5F, 0.0F, 0.0F);
					mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockpos), MobSpawnType.STRUCTURE, null, null);
					level.addFreshEntityWithPassengers(mob);
				}
			}
		}

		public static LaboratoryPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new LaboratoryPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class BalconyPiece extends EntrenchmentPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 4;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public BalconyPiece(int depth, BoundingBox bbox, Direction direction) {
			super(BALCONY_TYPE, depth, bbox);
			this.entryDoor = DoorType.OPENING;
			this.setOrientation(direction);
		}

		public BalconyPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(BALCONY_TYPE, nbt);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState PURPURACEUS_FENCE = ECBlocks.TO_FENCE.get(ECBlocks.Plant.PURPURACEUS_PLANKS.getId()).defaultBlockState();

			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, 0, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, false);
			this.generateBox(level, bbox, 0, 1, 0, WIDTH - 1, HEIGHT - 2, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);
			this.generateBox(level, bbox, 0, HEIGHT - 1, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 2, PURPLE_NETHER_BRICKS, CAVE_AIR, true);
			this.generateBox(level, bbox, 0, HEIGHT - 1, LENGTH - 2, WIDTH - 1, HEIGHT - 1, LENGTH - 1, CAVE_AIR, CAVE_AIR, true);

			BlockState NORTH_SOUTH_FENCE = PURPURACEUS_FENCE.setValue(FenceBlock.NORTH, Boolean.TRUE).setValue(FenceBlock.SOUTH, Boolean.TRUE);
			BlockState WEST_EAST_FENCE = PURPURACEUS_FENCE.setValue(FenceBlock.WEST, Boolean.TRUE).setValue(FenceBlock.EAST, Boolean.TRUE);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 0, 1, 0, bbox);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 0, 1, 1, bbox);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 0, 1, 2, bbox);
			this.placeBlock(level, PURPURACEUS_FENCE.setValue(FenceBlock.EAST, Boolean.TRUE).setValue(FenceBlock.SOUTH, Boolean.TRUE), 0, 1, 3, bbox);
			this.placeBlock(level, WEST_EAST_FENCE, 1, 1, 3, bbox);
			this.placeBlock(level, WEST_EAST_FENCE, 2, 1, 3, bbox);
			this.placeBlock(level, WEST_EAST_FENCE, 3, 1, 3, bbox);
			this.placeBlock(level, PURPURACEUS_FENCE.setValue(FenceBlock.WEST, Boolean.TRUE).setValue(FenceBlock.SOUTH, Boolean.TRUE), 4, 1, 3, bbox);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 4, 1, 2, bbox);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 4, 1, 1, bbox);
			this.placeBlock(level, NORTH_SOUTH_FENCE, 4, 1, 0, bbox);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static BalconyPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new BalconyPiece(depth, boundingbox, direction) : null;
		}
	}

	public static final class PortalRoomPiece extends EntrenchmentPiece {
		private static final int WIDTH = 9;
		private static final int HEIGHT = 8;
		private static final int LENGTH = 8;

		private static final int OFF_X = 4;
		private static final int OFF_Y = 2;
		private static final int OFF_Z = 0;

		public PortalRoomPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(PORTAL_ROOM_TYPE, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		public PortalRoomPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(PORTAL_ROOM_TYPE, nbt);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState LAVA = Blocks.LAVA.defaultBlockState();
			BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
			BlockState GOLD_BLOCK = Blocks.GOLD_BLOCK.defaultBlockState();
			BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();
			BlockState CRYING_OBSIDIAN = Blocks.CRYING_OBSIDIAN.defaultBlockState();
			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, false);

			for(int i = 1; i < WIDTH - 1; ++i) {
				for(int j = 1; j < LENGTH - 1; ++j) {
					if(random.nextInt(5) <= 2) {
						switch (random.nextInt(8)) {
							case 0, 1, 2, 3, 4, 5 -> this.placeBlock(level, LAVA, i, 1, j, bbox);
							case 6 -> this.placeBlock(level, GOLD_BLOCK, i, 1, j, bbox);
						}
					} else if(random.nextInt(7) == 0) {
						this.placeBlock(level, OBSIDIAN, i, 1, j, bbox);
					} else {
						this.placeBlock(level, NETHERRACK, i, 1, j, bbox);
					}
				}
			}

			for(int i = 2; i <= 6; ++i) {
				for(int j = 2; j <= 6; ++j) {
					if(i == 2 || i == 6 || j == 2 || j == 6) {
						int a = random.nextInt(16);
						if(a < 10) {
							this.placeBlock(level, OBSIDIAN, i, j, 4, bbox);
						} else if(a < 11) {
							this.placeBlock(level, CRYING_OBSIDIAN, i, j, 4, bbox);
						}
					}
				}
			}

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static PortalRoomPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new PortalRoomPiece(depth, random, boundingbox, direction) : null;
		}
	}

	public static final class WallPiece extends EntrenchmentPiece {
		private static final int WIDTH = 5;
		private static final int HEIGHT = 5;
		private static final int LENGTH = 1;

		private static final int OFF_X = 1;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public WallPiece(int depth, BoundingBox bbox, Direction direction) {
			super(WALL_TYPE, depth, bbox);
			this.entryDoor = DoorType.BLOCKED;
			this.setOrientation(direction);
		}

		public WallPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(WALL_TYPE, nbt);
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static WallPiece createPiece(StructurePieceAccessor pieces, @SuppressWarnings("unused") Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new WallPiece(depth, boundingbox, direction) : null;
		}
	}

	public static final class KitchenPiece extends EntrenchmentPiece {
		private static final int WIDTH = 9;
		private static final int HEIGHT = 7;
		private static final int LENGTH = 8;

		private static final int OFF_X = 2;
		private static final int OFF_Y = 1;
		private static final int OFF_Z = 0;

		public KitchenPiece(int depth, Random random, BoundingBox bbox, Direction direction) {
			super(KITCHEN_TYPE, depth, bbox);
			this.entryDoor = this.randomSmallDoor(random);
			this.setOrientation(direction);
		}

		public KitchenPiece(@SuppressWarnings("unused") StructurePieceSerializationContext context, CompoundTag nbt) {
			super(KITCHEN_TYPE, nbt);
		}

		private static final List<PieceWeight> kitchenAvailablePieces = Lists.newArrayList(
				new PieceWeight(LongHallwayPiece.class, 8, 0),
				new PieceWeight(ShortHallwayPiece.class, 12, 0),
				new PieceWeight(LeftTurnPiece.class, 5, 0),
				new PieceWeight(RightTurnPiece.class, 5, 0),
				new PieceWeight(LaboratoryPiece.class, 30, 2),
				new PieceWeight(BalconyPiece.class, 40, 0)
		);

		@Override
		protected List<PieceWeight> AvailablePieces() {
			return kitchenAvailablePieces;
		}

		@Override
		public void addChildren(@NotNull StructurePiece structurePiece, @NotNull StructurePieceAccessor pieces, @NotNull Random random) {
			this.generateChildForward((StartPiece) structurePiece, pieces, random, 1, 1);
		}

		@Override
		public void postProcess(@NotNull WorldGenLevel level, @NotNull StructureFeatureManager manager, @NotNull ChunkGenerator chunk, @NotNull Random random,
								@NotNull BoundingBox bbox, @NotNull ChunkPos chunkPos, @NotNull BlockPos blockPos) {
			BlockState PURPLE_NETHER_BRICKS = ECBlocks.Decoration.PURPLE_NETHER_BRICKS.defaultBlockState();
			BlockState FIRE = Blocks.FIRE.defaultBlockState();
			BlockState CHAIN = Blocks.CHAIN.defaultBlockState().setValue(ChainBlock.AXIS, Direction.Axis.Z);
			BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
			BlockState PURPURACEUS_TRAPDOOR = ECBlocks.TO_TRAPDOOR.get(ECBlocks.Plant.PURPURACEUS_PLANKS.getId()).defaultBlockState().setValue(TrapDoorBlock.FACING, Direction.EAST).setValue(TrapDoorBlock.OPEN, Boolean.TRUE);
			BlockState HAY_BLOCK = Blocks.HAY_BLOCK.defaultBlockState();

			this.generateBox(level, bbox, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1, PURPLE_NETHER_BRICKS, CAVE_AIR, false);

			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 1, 2, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 2, 2, bbox);
			this.placeBlock(level, NETHERRACK, 1, 0, 3, bbox);
			this.placeBlock(level, NETHERRACK, 1, 0, 4, bbox);
			this.placeBlock(level, NETHERRACK, 1, 0, 5, bbox);
			this.placeBlock(level, FIRE, 1, 1, 3, bbox);
			this.placeBlock(level, FIRE, 1, 1, 4, bbox);
			this.placeBlock(level, FIRE, 1, 1, 5, bbox);
			this.placeBlock(level, CHAIN, 1, 2, 3, bbox);
			this.placeBlock(level, CHAIN, 1, 2, 4, bbox);
			this.placeBlock(level, CHAIN, 1, 2, 5, bbox);
			this.placeBlock(level, PURPURACEUS_TRAPDOOR, 2, 1, 3, bbox);
			this.placeBlock(level, PURPURACEUS_TRAPDOOR, 2, 1, 4, bbox);
			this.placeBlock(level, PURPURACEUS_TRAPDOOR, 2, 1, 5, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 1, 6, bbox);
			this.placeBlock(level, PURPLE_NETHER_BRICKS, 1, 2, 6, bbox);

			this.placeBlock(level, HAY_BLOCK, 7, 1, 2, bbox);
			this.placeBlock(level, HAY_BLOCK, 7, 1, 5, bbox);
			this.placeBlock(level, HAY_BLOCK, 7, 1, 6, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 1, 1, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 1, 3, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 1, 4, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 1, 5, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 2, 2, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 2, 5, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 7, 2, 6, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 6, 1, 2, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 6, 1, 5, bbox);
			if(random.nextBoolean()) this.placeBlock(level, HAY_BLOCK, 6, 1, 6, bbox);

			this.generateBox(level, bbox, 1, 1, LENGTH - 1, 3, 3, LENGTH - 1, CAVE_AIR, CAVE_AIR, false);

			this.generateSmallDoor(level, bbox, this.entryDoor, OFF_X, OFF_Y, OFF_Z);
		}

		public static KitchenPiece createPiece(StructurePieceAccessor pieces, Random random, int x, int y, int z, Direction direction, int depth) {
			BoundingBox boundingbox = BoundingBox.orientBox(x, y, z, -OFF_X, -OFF_Y, -OFF_Z, WIDTH, HEIGHT, LENGTH, direction);
			return isOkBox(boundingbox) && pieces.findCollisionPiece(boundingbox) == null ? new KitchenPiece(depth, random, boundingbox, direction) : null;
		}
	}
}
