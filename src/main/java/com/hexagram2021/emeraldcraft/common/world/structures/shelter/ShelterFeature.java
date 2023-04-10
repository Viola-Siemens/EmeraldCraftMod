package com.hexagram2021.emeraldcraft.common.world.structures.shelter;

import com.hexagram2021.emeraldcraft.common.register.ECStructureTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShelterFeature extends Structure {
	public static final Codec<ShelterFeature> CODEC = simpleCodec(ShelterFeature::new);

	public ShelterFeature(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override @NotNull
	public Optional<GenerationStub> findGenerationPoint(@NotNull Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (builder) -> this.generatePieces(builder, context));
	}

	private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
		Rotation rotation = Rotation.getRandom(context.random());
		ShelterPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder);
	}

	@Override @NotNull
	public StructureType<?> type() {
		return ECStructureTypes.SHELTER.get();
	}
}
