package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class ContinuousMinerBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
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

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of()
			.requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.METAL);

	public ContinuousMinerBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.registerDefaultState(this.stateDefinition.any().setValue(TRIGGERED, Boolean.FALSE));
	}

	@Override @Nullable
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ContinuousMinerBlockEntity(blockPos, blockState);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return MINER_MAIN.get(blockState.getValue(FACING));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return Shapes.or(MINER_MAIN.get(blockState.getValue(FACING)), ROCK_BREAKER.get(blockState.getValue(FACING)));
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(blockState.getMenuProvider(level, blockPos));
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createContinuousMinerTicker(level, type, ECBlockEntity.CONTINUOUS_MINER.get());
	}

	@Override
	public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState neighbor, boolean v) {
		level.setBlock(blockPos, blockState.setValue(TRIGGERED, level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())), Block.UPDATE_CLIENTS);
	}

	@Override
	public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighbor, boolean v) {
		level.setBlock(blockPos, blockState.setValue(TRIGGERED, level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above())), Block.UPDATE_CLIENTS);
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean b) {
		if (!blockState.is(newBlockState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof ContinuousMinerBlockEntity continuousMinerBlockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					Containers.dropContents(serverLevel, blockPos, continuousMinerBlockEntity);
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}
		}

		super.onRemove(blockState, level, blockPos, newBlockState, b);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror rotation) {
		return blockState.rotate(rotation.getRotation(blockState.getValue(FACING)));
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TRIGGERED);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createContinuousMinerTicker(Level level, BlockEntityType<T> type, BlockEntityType<? extends ContinuousMinerBlockEntity> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(type, blockEntityType, ContinuousMinerBlockEntity::serverTick);
	}
}
