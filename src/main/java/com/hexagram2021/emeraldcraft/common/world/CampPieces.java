package com.hexagram2021.emeraldcraft.common.world;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.hexagram2021.emeraldcraft.api.camp.CampTypes;
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

	public static void addPieces(TemplateManager structureManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, CampType type) {
		if (CampTypes.BADLANDS.equals(type)) {
			pieces.add(new CampPiece(structureManager, BADLANDS_CAMP, pos, rotation));
		} else if (CampTypes.BIRCH.equals(type)) {
			pieces.add(new CampPiece(structureManager, BIRCH_CAMP, pos, rotation));
		} else if (CampTypes.DESERT.equals(type)) {
			pieces.add(new CampPiece(structureManager, DESERT_CAMP, pos, rotation));
		} else if (CampTypes.JUNGLE.equals(type)) {
			pieces.add(new CampPiece(structureManager, JUNGLE_CAMP, pos, rotation));
		} else if (CampTypes.PLAINS.equals(type)) {
			pieces.add(new CampPiece(structureManager, PLAINS_CAMP, pos, rotation));
		} else if (CampTypes.SAVANNA.equals(type)) {
			pieces.add(new CampPiece(structureManager, SAVANNA_CAMP, pos, rotation));
		} else if (CampTypes.SNOW.equals(type)) {
			pieces.add(new CampPiece(structureManager, SNOW_CAMP, pos, rotation));
		} else if (CampTypes.STONY.equals(type)) {
			pieces.add(new CampPiece(structureManager, STONY_CAMP, pos, rotation));
		} else if (CampTypes.SWAMP.equals(type)) {
			pieces.add(new CampPiece(structureManager, SWAMP_CAMP, pos, rotation));
		} else if (CampTypes.TAIGA.equals(type)) {
			pieces.add(new CampPiece(structureManager, TAIGA_CAMP, pos, rotation));
		} else {
			ResourceLocation camp = CampTypes.getCampWithType(type);
			if (camp == null) {
				throw new IllegalArgumentException("Unknown camp type: " + type);
			}
			pieces.add(new CampPiece(structureManager, camp, pos, rotation));
		}
	}

	public static class CampPiece extends TemplateStructurePiece {
		private final ResourceLocation templateLocation;
		private final Rotation rotation;
		public CampPiece(TemplateManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation) {
			super(ECStructures.CAMP_TYPE, 0);
			this.templateLocation = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.loadTemplate(structureManager);
		}

		public CampPiece(TemplateManager structureManager, CompoundNBT tag) {
			super(ECStructures.CAMP_TYPE, tag);
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
