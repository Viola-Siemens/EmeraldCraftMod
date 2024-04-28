package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.MeatGrinderBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class MeatGrinderBlock extends BaseEntityBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public static final Supplier<Properties> PROPERTIES = () -> Properties.of()
			.requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F);

	public MeatGrinderBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
								 InteractionHand hand, BlockHitResult blockHitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		ItemStack handItem = player.getItemInHand(hand);
		if(blockEntity instanceof MeatGrinderBlockEntity meatGrinderBlockEntity) {
			ItemStack input = meatGrinderBlockEntity.getItem(MeatGrinderBlockEntity.SLOT_INPUT);
			ItemStack result = meatGrinderBlockEntity.getItem(MeatGrinderBlockEntity.SLOT_RESULT);
			if(handItem.isEmpty()) {
				if(result.isEmpty()) {
					if(input.isEmpty()) {
						return InteractionResult.PASS;
					}
					player.setItemInHand(hand, input);
					meatGrinderBlockEntity.setItem(MeatGrinderBlockEntity.SLOT_INPUT, ItemStack.EMPTY);
				} else {
					player.setItemInHand(hand, result);
					meatGrinderBlockEntity.setItem(MeatGrinderBlockEntity.SLOT_RESULT, ItemStack.EMPTY);
				}
				meatGrinderBlockEntity.setChanged();
				return InteractionResult.CONSUME;
			}
			if(ItemStack.isSameItemSameTags(result, handItem)) {
				int grow = Math.min(handItem.getMaxStackSize() - handItem.getCount(), result.getCount());
				handItem.grow(grow);
				result.shrink(grow);
				if(result.isEmpty()) {
					meatGrinderBlockEntity.setItem(MeatGrinderBlockEntity.SLOT_RESULT, ItemStack.EMPTY);
				}
				meatGrinderBlockEntity.setChanged();
				return InteractionResult.CONSUME;
			}
			if (input.isEmpty() && meatGrinderBlockEntity.canPlaceItem(MeatGrinderBlockEntity.SLOT_INPUT, handItem)) {
				meatGrinderBlockEntity.setItem(MeatGrinderBlockEntity.SLOT_INPUT, handItem.split(1));
				meatGrinderBlockEntity.setChanged();
				return InteractionResult.CONSUME;
			}
			if (ItemStack.isSameItemSameTags(input, handItem)) {
				input.grow(1);
				handItem.shrink(1);
				meatGrinderBlockEntity.setChanged();
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
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
	public boolean useShapeForLightOcclusion(BlockState blockState) {
		return true;
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
		definition.add(FACING);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos blockPos, PathComputationType type) {
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new MeatGrinderBlockEntity(blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean b) {
		if (!blockState.is(newBlockState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof MeatGrinderBlockEntity iceMakerBlockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					Containers.dropContents(serverLevel, blockPos, iceMakerBlockEntity);
				}

				level.updateNeighbourForOutputSignal(blockPos, this);
			}

			super.onRemove(blockState, level, blockPos, newBlockState, b);
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ECBlockEntity.MEAT_GRINDER.get(), level.isClientSide ? MeatGrinderBlockEntity::animationTick : MeatGrinderBlockEntity::serverTick);
	}
}
