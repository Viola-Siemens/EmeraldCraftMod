package com.hexagram2021.emeraldcraft.common.world.structures.shelter;

import com.hexagram2021.emeraldcraft.common.register.ECStructurePieceTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ShelterPieces {
	private static final ResourceLocation SHELTER = new ResourceLocation(MODID, "shelter/piglin_cutey_shelter");

	public static void addPieces(StructureTemplateManager structureManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieces) {
		pieces.addPiece(new ShelterPieces.ShelterPiece(structureManager, SHELTER, pos, rotation));
	}

	public static class ShelterPiece extends TemplateStructurePiece {
		public ShelterPiece(StructureTemplateManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation) {
			super(ECStructurePieceTypes.SHELTER_TYPE, 0, structureManager, location, location.toString(), makeSettings(rotation), pos);
		}

		public ShelterPiece(StructurePieceSerializationContext context, CompoundTag tag) {
			super(ECStructurePieceTypes.SHELTER_TYPE, tag, context.structureTemplateManager(), (location) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
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
		protected void handleDataMarker(@NotNull String function, @NotNull BlockPos pos, @NotNull ServerLevelAccessor level, @NotNull RandomSource random, @NotNull BoundingBox sbb) { }
	}
}
