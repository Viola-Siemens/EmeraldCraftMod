package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CookstoveBlock extends BaseEntityBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D);
	public static final Supplier<Properties> PROPERTIES = () -> Properties.of()
			.requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F);

	public CookstoveBlock(Properties properties) {
		super(properties);
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
			if(diff.length() < 0.05D) {
				index = -1;
			} else {
				index = (int)(Math.atan2(diff.y(), diff.x()) * 4 / Math.PI) + 4;
				if(index >= 8) {
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
