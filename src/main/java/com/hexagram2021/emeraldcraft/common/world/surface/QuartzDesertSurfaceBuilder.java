package com.hexagram2021.emeraldcraft.common.world.surface;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.ValleySurfaceBuilder;

import javax.annotation.Nonnull;

public class QuartzDesertSurfaceBuilder extends ValleySurfaceBuilder {
	private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
	private static final BlockState VITRIFIED_SAND = ECBlocks.Decoration.VITRIFIED_SAND.defaultBlockState();
	private static final BlockState QUARTZ_SAND = ECBlocks.Decoration.QUARTZ_SAND.defaultBlockState();
	private static final BlockState QUARTZ_SANDSTONE = ECBlocks.Decoration.QUARTZ_SANDSTONE.defaultBlockState();
	private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES = ImmutableList.of(QUARTZ_SAND, QUARTZ_SANDSTONE);
	private static final ImmutableList<BlockState> CEILING_BLOCK_STATES = ImmutableList.of(NETHERRACK, VITRIFIED_SAND);

	public QuartzDesertSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
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
		return VITRIFIED_SAND;
	}
}
