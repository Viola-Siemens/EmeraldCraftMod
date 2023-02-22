package com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree;

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

public class HollowTreeFeature extends Structure {
	public static final Codec<HollowTreeFeature> CODEC = simpleCodec(HollowTreeFeature::new);

	public HollowTreeFeature(StructureSettings settings) {
		super(settings);
	}

	@Override @NotNull
	public Optional<GenerationStub> findGenerationPoint(@NotNull Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (builder) -> this.generatePieces(builder, context));
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getMinBlockX() + 7, 0, context.chunkPos().getMinBlockZ() + 7);
		BlockPos blockpos = new BlockPos(
				context.chunkPos().getBlockX(5),
				context.chunkGenerator().getBaseHeight(
						centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
				),
				context.chunkPos().getBlockZ(5)
		);
		Rotation rotation = Rotation.getRandom(context.random());
		HollowTreePieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder);
	}

	@Override @NotNull
	public StructureType<?> type() {
		return ECStructureTypes.HOLLOW_TREE;
	}
}
