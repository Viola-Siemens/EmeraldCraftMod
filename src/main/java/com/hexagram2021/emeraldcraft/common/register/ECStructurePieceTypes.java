package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.ECContent;
import com.hexagram2021.emeraldcraft.common.world.structures.camp.CampPieces;
import com.hexagram2021.emeraldcraft.common.world.structures.entrenchment.EntrenchmentPieces;
import com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree.HollowTreePieces;
import com.hexagram2021.emeraldcraft.common.world.structures.shelter.ShelterPieces;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructurePieceTypes {
	public static final StructurePieceType SHELTER_TYPE = ShelterPieces.ShelterPiece::new;
	public static final StructurePieceType HOLLOW_TREE_TYPE = HollowTreePieces.HollowTreePiece::new;
	public static final StructurePieceType CAMP_TYPE = CampPieces.CampPiece::new;

	public static void init(ECContent.RegisterConsumer<StructurePieceType> register) {
		register.accept(new ResourceLocation(MODID, "shelter"), SHELTER_TYPE);
		register.accept(new ResourceLocation(MODID, "hollow_tree"), HOLLOW_TREE_TYPE);
		register.accept(new ResourceLocation(MODID, "camp"), CAMP_TYPE);
		EntrenchmentPieceTypes.init(register);
	}

	public static final class EntrenchmentPieceTypes {
		public static final StructurePieceType HALL_TYPE = EntrenchmentPieces.HallPiece::new;
		public static final StructurePieceType START_TYPE = EntrenchmentPieces.StartPiece::new;
		public static final StructurePieceType CROSSING_TYPE = EntrenchmentPieces.CrossingPiece::new;
		public static final StructurePieceType T_BRIDGE_TYPE = EntrenchmentPieces.TBridgePiece::new;
		public static final StructurePieceType LONG_HALLWAY_TYPE = EntrenchmentPieces.LongHallwayPiece::new;
		public static final StructurePieceType SHORT_HALLWAY_TYPE = EntrenchmentPieces.ShortHallwayPiece::new;
		public static final StructurePieceType CHEST_HALLWAY_TYPE = EntrenchmentPieces.ChestHallwayPiece::new;
		public static final StructurePieceType BRIDGE_HALLWAY_TYPE = EntrenchmentPieces.ShortBridgeHallwayPiece::new;
		public static final StructurePieceType LEFT_TURN_TYPE = EntrenchmentPieces.LeftTurnPiece::new;
		public static final StructurePieceType RIGHT_TURN_TYPE = EntrenchmentPieces.RightTurnPiece::new;
		public static final StructurePieceType MONSTER_ROOM_TYPE = EntrenchmentPieces.MonsterRoomPiece::new;
		public static final StructurePieceType LABORATORY_TYPE = EntrenchmentPieces.LaboratoryPiece::new;
		public static final StructurePieceType BALCONY_TYPE = EntrenchmentPieces.BalconyPiece::new;
		public static final StructurePieceType PORTAL_ROOM_TYPE = EntrenchmentPieces.PortalRoomPiece::new;
		public static final StructurePieceType WALL_TYPE = EntrenchmentPieces.WallPiece::new;
		public static final StructurePieceType KITCHEN_TYPE = EntrenchmentPieces.KitchenPiece::new;

		private EntrenchmentPieceTypes() {
		}

		public static void init(ECContent.RegisterConsumer<StructurePieceType> register) {
			register.accept(new ResourceLocation(MODID, "entrenchment_hall"), HALL_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_start"), START_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_crossing"), CROSSING_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_t_bridge"), T_BRIDGE_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_long_hallway"), LONG_HALLWAY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_short_hallway"), SHORT_HALLWAY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_chest_hallway"), CHEST_HALLWAY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_bridge_hallway"), BRIDGE_HALLWAY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_left_turn"), LEFT_TURN_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_right_turn"), RIGHT_TURN_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_monster_room"), MONSTER_ROOM_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_laboratory"), LABORATORY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_balcony"), BALCONY_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_portal_room"), PORTAL_ROOM_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_wall"), WALL_TYPE);
			register.accept(new ResourceLocation(MODID, "entrenchment_kitchen"), KITCHEN_TYPE);
		}
	}
}
