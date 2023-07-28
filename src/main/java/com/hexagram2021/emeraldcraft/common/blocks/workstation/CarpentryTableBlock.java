package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.crafting.menu.CarpentryTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CarpentryTableBlock extends Block {
	private static final Component CONTAINER_TITLE = Component.translatable("container.carpentry");
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of().instrument(NoteBlockInstrument.BASS)
			.sound(SoundType.WOOD).strength(2.5F);

	public CarpentryTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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

	@Override @Nullable
	public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
		return new SimpleMenuProvider((id, inventory, levelAccess) ->
				new CarpentryTableMenu(id, inventory, ContainerLevelAccess.create(level, blockPos)), CONTAINER_TITLE);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState blockState) {
		return true;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
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
}
