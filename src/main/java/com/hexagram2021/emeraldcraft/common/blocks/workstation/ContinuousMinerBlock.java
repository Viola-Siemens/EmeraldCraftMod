package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class ContinuousMinerBlock extends ContainerBlock {
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	public static final Map<Direction, VoxelShape> MINER_MAIN = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(0, 0, 3, 16, 16, 16),
			Direction.SOUTH, Block.box(0, 0, 0, 16, 16, 13),
			Direction.EAST, Block.box(0, 0, 0, 13, 16, 16),
			Direction.WEST, Block.box(3, 0, 0, 16, 16, 16)
	));
	public static final Map<Direction, VoxelShape> ROCK_BREAKER = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(0, 2, -6, 16, 11, 3),
			Direction.SOUTH, Block.box(0, 2, 13, 16, 11, 22),
			Direction.EAST, Block.box(13, 2, 0, 22, 11, 16),
			Direction.WEST, Block.box(-6, 2, 0, 3, 11, 16)
	));

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.METAL)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.strength(3.5F);

	public ContinuousMinerBlock(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.registerDefaultState(this.stateDefinition.any().setValue(TRIGGERED, Boolean.FALSE));
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(@Nonnull IBlockReader reader) {
		return new ContinuousMinerBlockEntity();
	}

	@Override @Nonnull
	public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext context) {
		return MINER_MAIN.get(blockState.getValue(FACING));
	}

	@Override @Nonnull
	public VoxelShape getCollisionShape(BlockState blockState, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext context) {
		return VoxelShapes.or(MINER_MAIN.get(blockState.getValue(FACING)), ROCK_BREAKER.get(blockState.getValue(FACING)));
	}

	@Override @Nonnull
	public ActionResultType use(@Nonnull BlockState blockState, World level, @Nonnull BlockPos blockPos, @Nonnull PlayerEntity player,
								@Nonnull Hand interactionHand, @Nonnull BlockRayTraceResult blockHitResult) {
		if (level.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		player.openMenu(blockState.getMenuProvider(level, blockPos));
		return ActionResultType.CONSUME;
	}

	@Override
	public void onPlace(BlockState blockState, World level, @Nonnull BlockPos blockPos, @Nonnull BlockState neighbor, boolean v) {
		level.setBlock(blockPos, blockState.setValue(TRIGGERED, level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())), 2);
	}

	@Override
	public void neighborChanged(BlockState blockState, World level, @Nonnull BlockPos blockPos, @Nonnull Block block, @Nonnull BlockPos neighbor, boolean v) {
		level.setBlock(blockPos, blockState.setValue(TRIGGERED, level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())), 2);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override @Nonnull
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override @Nonnull
	public BlockState mirror(BlockState blockState, Mirror rotation) {
		return blockState.rotate(rotation.getRotation(blockState.getValue(FACING)));
	}

	@Override @Nonnull
	public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, TRIGGERED);
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader level, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}
}
