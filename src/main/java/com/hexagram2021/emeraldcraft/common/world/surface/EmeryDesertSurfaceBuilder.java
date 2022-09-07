package com.hexagram2021.emeraldcraft.common.world.surface;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.ValleySurfaceBuilder;

import javax.annotation.Nonnull;

public class EmeryDesertSurfaceBuilder extends ValleySurfaceBuilder {
	private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
	private static final BlockState EMERY_SAND = ECBlocks.Decoration.EMERY_SAND.defaultBlockState();
	private static final BlockState EMERY_SANDSTONE = ECBlocks.Decoration.EMERY_SANDSTONE.defaultBlockState();
	private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES = ImmutableList.of(EMERY_SAND, EMERY_SANDSTONE);
	private static final ImmutableList<BlockState> CEILING_BLOCK_STATES = ImmutableList.of(BLACKSTONE);

	public EmeryDesertSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
		super(codec);
	}

	@Override @Nonnull
	protected ImmutableList<BlockState> getFloorBlockStates() {
		return FLOOR_BLOCK_STATES;
	}

	@Override @Nonnull
	protected ImmutableList<BlockState> getCeilingBlockStates() {
		return CEILING_BLOCK_STATES;
	}

	@Override @Nonnull
	protected BlockState getPatchBlockState() {
		return BLACKSTONE;
	}
}
