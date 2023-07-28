package com.hexagram2021.emeraldcraft.common.world.structures.hollow_tree;

import com.hexagram2021.emeraldcraft.common.register.ECStructureTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class HollowTreeFeature extends Structure {
	public static final Codec<HollowTreeFeature> CODEC = simpleCodec(HollowTreeFeature::new);

	public HollowTreeFeature(StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> this.generatePieces(builder, context));
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
		BlockPos centerOfChunk = new BlockPos(context.chunkPos().getBlockX(5), 0, context.chunkPos().getBlockZ(5));
		BlockPos blockpos = new BlockPos(
				centerOfChunk.getX(),
				context.chunkGenerator().getBaseHeight(
						centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
				),
				centerOfChunk.getZ()
		);
		Rotation rotation = Rotation.getRandom(context.random());
		HollowTreePieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder);
	}

	@Override
	public StructureType<?> type() {
		return ECStructureTypes.HOLLOW_TREE.get();
	}
}
