package com.hexagram2021.emeraldcraft.common.world.structures.camp;

import com.hexagram2021.emeraldcraft.api.camp.CampType;
import com.hexagram2021.emeraldcraft.common.register.ECStructureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CampFeature extends Structure {
	public static final Codec<CampFeature> CODEC = RecordCodecBuilder.create(
			builder -> builder.group(
				settingsCodec(builder), CampType.CODEC.fieldOf("camp_type").forGetter(structure -> structure.type)
			).apply(builder, CampFeature::new));

	private final CampType type;

	public CampFeature(Structure.StructureSettings settings, CampType type) {
		super(settings);
		this.type = type;
	}

	@Override @NotNull
	public Optional<GenerationStub> findGenerationPoint(@NotNull Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> this.generatePieces(builder, context, this.type));
	}

	private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, CampType type) {
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getMinBlockX() + 7, 0, context.chunkPos().getMinBlockZ() + 7);
		BlockPos blockpos = new BlockPos(
				context.chunkPos().getMinBlockX(),
				context.chunkGenerator().getBaseHeight(
						centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
				),
				context.chunkPos().getMinBlockZ()
		);
		Rotation rotation = Rotation.getRandom(context.random());
		CampPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder, type);
	}

	@Override @NotNull
	public StructureType<?> type() {
		return ECStructureTypes.CAMP.get();
	}
}
