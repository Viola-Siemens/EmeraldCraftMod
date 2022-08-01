package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
import com.hexagram2021.emeraldcraft.common.register.ECStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CampPieces {
	private static final ResourceLocation BADLANDS_CAMP = new ResourceLocation(MODID, "camp/badlands_camp");
	private static final ResourceLocation BIRCH_CAMP = new ResourceLocation(MODID, "camp/birch_camp");
	private static final ResourceLocation DESERT_CAMP = new ResourceLocation(MODID, "camp/desert_camp");
	private static final ResourceLocation JUNGLE_CAMP = new ResourceLocation(MODID, "camp/jungle_camp");
	private static final ResourceLocation PLAINS_CAMP = new ResourceLocation(MODID, "camp/plains_camp");
	private static final ResourceLocation SAVANNA_CAMP = new ResourceLocation(MODID, "camp/savanna_camp");
	private static final ResourceLocation SNOW_CAMP = new ResourceLocation(MODID, "camp/snow_camp");
	private static final ResourceLocation STONY_CAMP = new ResourceLocation(MODID, "camp/stony_camp");
	private static final ResourceLocation SWAMP_CAMP = new ResourceLocation(MODID, "camp/swamp_camp");
	private static final ResourceLocation TAIGA_CAMP = new ResourceLocation(MODID, "camp/taiga_camp");

	public static void addPieces(StructureManager structureManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieces, CampType type) {
		if (CampTypes.BADLANDS.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, BADLANDS_CAMP, pos, rotation));
		} else if (CampTypes.BIRCH.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, BIRCH_CAMP, pos, rotation));
		} else if (CampTypes.DESERT.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, DESERT_CAMP, pos, rotation));
		} else if (CampTypes.JUNGLE.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, JUNGLE_CAMP, pos, rotation));
		} else if (CampTypes.PLAINS.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, PLAINS_CAMP, pos, rotation));
		} else if (CampTypes.SAVANNA.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, SAVANNA_CAMP, pos, rotation));
		} else if (CampTypes.SNOW.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, SNOW_CAMP, pos, rotation));
		} else if (CampTypes.STONY.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, STONY_CAMP, pos, rotation));
		} else if (CampTypes.SWAMP.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, SWAMP_CAMP, pos, rotation));
		} else if (CampTypes.TAIGA.equals(type)) {
			pieces.addPiece(new CampPiece(structureManager, TAIGA_CAMP, pos, rotation));
		} else {
			ResourceLocation camp = CampTypes.getCampWithType(type);
			if (camp == null) {
				throw new IllegalArgumentException("Unknown camp type: " + type.toString());
			}
			pieces.addPiece(new CampPiece(structureManager, camp, pos, rotation));
		}
	}

	public static class CampPiece extends TemplateStructurePiece {
		public CampPiece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation) {
			super(ECStructures.CAMP_TYPE, 0, structureManager, location, location.toString(), makeSettings(rotation), pos);
		}

		public CampPiece(StructurePieceSerializationContext context, CompoundTag tag) {
			super(ECStructures.CAMP_TYPE, tag, context.structureManager(), (location) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
		}

		private static StructurePlaceSettings makeSettings(Rotation rotation) {
			return (new StructurePlaceSettings())
					.setRotation(rotation)
					.setMirror(Mirror.LEFT_RIGHT)
					.setRotationPivot(new BlockPos(5, 1, 5))
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		}


		@Override
		protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext context, @NotNull CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putString("Rot", this.placeSettings.getRotation().name());
		}

		@Override
		protected void handleDataMarker(@NotNull String function, @NotNull BlockPos pos, @NotNull ServerLevelAccessor level, @NotNull Random random, @NotNull BoundingBox sbb) { }
	}
}
