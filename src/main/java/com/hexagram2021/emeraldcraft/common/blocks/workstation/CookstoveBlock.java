package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CookstoveBlock extends BaseEntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	public static final Supplier<Properties> PROPERTIES = () -> Properties.of()
			.requiresCorrectToolForDrops().sound(SoundType.METAL).noOcclusion().strength(5.0F, 6.0F);

	public CookstoveBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new CookstoveBlockEntity(blockPos, blockState);
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand hand, BlockHitResult blockHitResult) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if(blockEntity instanceof CookstoveBlockEntity cookstove) {
			if (level.isClientSide) {
				return InteractionResult.SUCCESS;
			}

			Vec3 diff = blockHitResult.getLocation().subtract(blockPos.getCenter()).multiply(1.0D, 0.0D, 1.0D);
			int index;

			if(blockHitResult.getLocation().y() < blockPos.getCenter().y() - 0.2D) {
				if(blockHitResult.getDirection().equals(blockState.getValue(FACING))) {
					index = CookstoveBlockEntity.ADD_FUEL_INDEX;
				} else {
					index = CookstoveBlockEntity.DONT_ADD_INGREDIENT_INDEX;
				}
			} else if(diff.length() < 0.05D) {
				index = CookstoveBlockEntity.DONT_ADD_INGREDIENT_INDEX;
			} else {
				index = Mth.floor(Math.atan2(diff.z(), diff.x()) * 4.0D / Math.PI) + 4;
				if(index < 0) {
					index += 8;
				} else if(index >= 8) {
					index -= 8;
				}
			}
			ItemStack item = player.getItemInHand(hand);
			if(cookstove.interact(player, item, index)) {
				if(!item.isEmpty()) {
					item.shrink(1);
				}
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean piston) {
		if (!blockState.is(newBlockState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof CookstoveBlockEntity cookstoveBlockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					Containers.dropContents(serverLevel, blockPos, cookstoveBlockEntity);
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}

			super.onRemove(blockState, level, blockPos, newBlockState, piston);
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ECBlockEntity.COOKSTOVE.get(), CookstoveBlockEntity::tick);
	}
}
