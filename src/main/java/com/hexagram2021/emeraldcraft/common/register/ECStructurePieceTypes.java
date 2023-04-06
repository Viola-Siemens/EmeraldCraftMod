package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampPieces;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentPieces;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreePieces;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterPieces;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructurePieceTypes {
	public static final StructurePieceType SHELTER_TYPE = register("shelter", ShelterPieces.ShelterPiece::new);
	public static final StructurePieceType HOLLOW_TREE_TYPE = register("hollow_tree", HollowTreePieces.HollowTreePiece::new);
	public static final StructurePieceType CAMP_TYPE = register("camp", CampPieces.CampPiece::new);

	public static final class EntrenchmentPieceTypes {
		public static final StructurePieceType HALL_TYPE = register("entrenchment_hall", EntrenchmentPieces.HallPiece::new);
		public static final StructurePieceType START_TYPE = register("entrenchment_start", EntrenchmentPieces.StartPiece::new);
		public static final StructurePieceType CROSSING_TYPE = register("entrenchment_crossing", EntrenchmentPieces.CrossingPiece::new);
		public static final StructurePieceType T_BRIDGE_TYPE = register("entrenchment_t_bridge", EntrenchmentPieces.TBridgePiece::new);
		public static final StructurePieceType LONG_HALLWAY_TYPE = register("entrenchment_long_hallway", EntrenchmentPieces.LongHallwayPiece::new);
		public static final StructurePieceType SHORT_HALLWAY_TYPE = register("entrenchment_short_hallway", EntrenchmentPieces.ShortHallwayPiece::new);
		public static final StructurePieceType CHEST_HALLWAY_TYPE = register("entrenchment_chest_hallway", EntrenchmentPieces.ChestHallwayPiece::new);
		public static final StructurePieceType BRIDGE_HALLWAY_TYPE = register("entrenchment_bridge_hallway", EntrenchmentPieces.ShortBridgeHallwayPiece::new);
		public static final StructurePieceType LEFT_TURN_TYPE = register("entrenchment_left_turn", EntrenchmentPieces.LeftTurnPiece::new);
		public static final StructurePieceType RIGHT_TURN_TYPE = register("entrenchment_right_turn", EntrenchmentPieces.RightTurnPiece::new);
		public static final StructurePieceType MONSTER_ROOM_TYPE = register("entrenchment_monster_room", EntrenchmentPieces.MonsterRoomPiece::new);
		public static final StructurePieceType LABORATORY_TYPE = register("entrenchment_laboratory", EntrenchmentPieces.LaboratoryPiece::new);
		public static final StructurePieceType BALCONY_TYPE = register("entrenchment_balcony", EntrenchmentPieces.BalconyPiece::new);
		public static final StructurePieceType PORTAL_ROOM_TYPE = register("entrenchment_portal_room", EntrenchmentPieces.PortalRoomPiece::new);
		public static final StructurePieceType WALL_TYPE = register("entrenchment_wall", EntrenchmentPieces.WallPiece::new);
		public static final StructurePieceType KITCHEN_TYPE = register("entrenchment_kitchen", EntrenchmentPieces.KitchenPiece::new);

		private EntrenchmentPieceTypes() {}

		public static void init() {}
	}

	public static void init() {
		EntrenchmentPieceTypes.init();
	}

	private static StructurePieceType register(String name, StructurePieceType type) {
		return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, new ResourceLocation(MODID, name), type);
	}
}
