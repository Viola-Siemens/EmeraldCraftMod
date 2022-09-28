package com.hexagram2021.emeraldcraft.common.blocks.sculpture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "deprecation"})
public class SculptureBlock extends Block {
	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.WOOD)
			.strength(2.0F)
			.sound(SoundType.WOOD);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected static final VoxelShape AABB = Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 15.0D);

	public SculptureBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override @NotNull
	public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return AABB;
	}

	@Override @NotNull
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override @NotNull
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.setValue(FACING, mirror.mirror(blockState.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
}
