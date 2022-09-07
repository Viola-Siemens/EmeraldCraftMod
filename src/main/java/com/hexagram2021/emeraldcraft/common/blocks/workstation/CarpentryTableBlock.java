package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableMenu;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CarpentryTableBlock extends Block {
	private static final TextComponent CONTAINER_TITLE = new TranslationTextComponent("container.carpentry");
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.WOOD)
			.sound(SoundType.WOOD)
			.strength(2.5F);

	public CarpentryTableBlock(AbstractBlock.Properties properties) {
		super(properties);
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
	@Nullable
	public INamedContainerProvider getMenuProvider(@Nonnull BlockState blockState, @Nonnull World level, @Nonnull BlockPos blockPos) {
		return new SimpleNamedContainerProvider((id, inventory, levelAccess) ->
				new CarpentryTableMenu(id, inventory, IWorldPosCallable.create(level, blockPos)), CONTAINER_TITLE);
	}

	@Override @Nonnull
	public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader blockGetter, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean useShapeForLightOcclusion(@Nonnull BlockState blockState) {
		return true;
	}

	@Override @Nonnull
	public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Override @Nonnull
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override @Nonnull
	public BlockState mirror(BlockState blockState, Mirror rotation) {
		return blockState.rotate(rotation.getRotation(blockState.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> definition) {
		definition.add(FACING);
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader getter, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}
}
