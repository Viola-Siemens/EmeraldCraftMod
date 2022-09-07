package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.common.register.ECStructures;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ShelterPieces {
	private static final ResourceLocation SHELTER = new ResourceLocation(MODID, "shelter/piglin_cutey_shelter");

	public static void addPieces(TemplateManager structureManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces) {
		pieces.add(new ShelterPieces.ShelterPiece(structureManager, SHELTER, pos, rotation));
	}

	public static class ShelterPiece extends TemplateStructurePiece {
		private final ResourceLocation templateLocation;
		private final Rotation rotation;
		public ShelterPiece(TemplateManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation) {
			super(ECStructures.SHELTER_TYPE, 0);
			this.templateLocation = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.loadTemplate(structureManager);
		}

		public ShelterPiece(TemplateManager structureManager, CompoundNBT tag) {
			super(ECStructures.SHELTER_TYPE, tag);
			this.templateLocation = new ResourceLocation(tag.getString("Template"));
			this.rotation = Rotation.valueOf(tag.getString("Rot"));
			this.loadTemplate(structureManager);
		}

		private void loadTemplate(TemplateManager manager) {
			Template template = manager.getOrCreate(this.templateLocation);
			PlacementSettings placementsettings = (new PlacementSettings())
					.setRotation(this.rotation)
					.setMirror(Mirror.LEFT_RIGHT)
					.setRotationPivot(new BlockPos(5, 1, 5))
					.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			this.setup(template, this.templatePosition, placementsettings);
		}


		@Override
		protected void addAdditionalSaveData(@Nonnull CompoundNBT tag) {
			super.addAdditionalSaveData(tag);
			tag.putString("Template", this.templateLocation.toString());
			tag.putString("Rot", this.placeSettings.getRotation().name());
		}

		@Override
		protected void handleDataMarker(@Nonnull String function, @Nonnull BlockPos pos, @Nonnull IServerWorld level,
										@Nonnull Random random, @Nonnull MutableBoundingBox sbb) { }
	}
}
