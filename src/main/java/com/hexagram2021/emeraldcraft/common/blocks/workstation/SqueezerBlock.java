package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static net.minecraftforge.common.ToolActions.SHEARS_HARVEST;

@SuppressWarnings("deprecation")
public class SqueezerBlock extends Block {
	public static final IntegerProperty HONEY_COUNT = ECProperties.HONEY_COUNT;

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(0.6F);

	public SqueezerBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(HONEY_COUNT, 0));
	}

	@Override @NotNull
	public InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player,
								 @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
		ItemStack itemstack = player.getItemInHand(interactionHand);
		ItemStack itemstack2 = player.getItemInHand(InteractionHand.OFF_HAND);
		if (interactionHand == InteractionHand.MAIN_HAND &&
				!isSqueezable(itemstack) && !isEmptyBottle(itemstack) && !itemstack.canPerformAction(SHEARS_HARVEST) &&
				(isSqueezable(itemstack2) || isEmptyBottle(itemstack2) || itemstack2.canPerformAction(SHEARS_HARVEST))) {
			return InteractionResult.PASS;
		}

		if (isSqueezable(itemstack) && canBeCharged(blockState)) {
			charge(level, itemstack, blockPos, blockState);
			if (!player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		if (blockState.getValue(HONEY_COUNT) != 0) {
			if (isEmptyBottle(itemstack)) {
				itemstack.shrink(1);
				level.playSound(
						null,
						(double)blockPos.getX() + 0.5D,
						(double)blockPos.getY() + 0.5D,
						(double)blockPos.getZ() + 0.5D,
						SoundEvents.BOTTLE_FILL,
						SoundSource.NEUTRAL,
						1.0F, 1.0F
				);
				if (itemstack.isEmpty()) {
					player.setItemInHand(interactionHand, new ItemStack(Items.HONEY_BOTTLE));
				} else if (!player.getInventory().add(new ItemStack(Items.HONEY_BOTTLE))) {
					player.drop(new ItemStack(Items.HONEY_BOTTLE), false);
				}
				level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
				resetHoneyCount(level, blockState, blockPos);

				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			if (itemstack.canPerformAction(SHEARS_HARVEST)) {
				level.playSound(
						null,
						(double)blockPos.getX() + 0.5D,
						(double)blockPos.getY() + 0.5D,
						(double)blockPos.getZ() + 0.5D,
						SoundEvents.BEEHIVE_SHEAR,
						SoundSource.NEUTRAL,
						1.0F, 1.0F
				);
				dropHoneycomb(level, blockPos);
				itemstack.hurtAndBreak(1, player, (player2) -> player2.broadcastBreakEvent(interactionHand));
				level.gameEvent(player, GameEvent.SHEAR, blockPos);
				resetHoneyCount(level, blockState, blockPos);

				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.CONSUME;
	}

	public static void dropHoneycomb(Level level, BlockPos blockPos) {
		popResource(level, blockPos, new ItemStack(Items.HONEYCOMB));
	}

	public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
		return blockState.getValue(HONEY_COUNT);
	}

	private static boolean isSqueezable(ItemStack itemStack) {
		return itemStack.is(Items.HONEYCOMB_BLOCK) || itemStack.is(Items.HONEYCOMB);
	}

	private static boolean isEmptyBottle(ItemStack itemStack) {
		return itemStack.is(Items.GLASS_BOTTLE);
	}

	private static boolean canBeCharged(BlockState blockState) {
		return blockState.getValue(HONEY_COUNT) < 4;
	}

	public void resetHoneyCount(Level level, BlockState blockState, BlockPos blockPos) {
		level.setBlock(blockPos, blockState.setValue(HONEY_COUNT, blockState.getValue(HONEY_COUNT) - 1), Block.UPDATE_ALL);
	}

	public static void charge(Level level, ItemStack itemStack, BlockPos blockPos, BlockState blockState) {
		int chargeValue = 0;
		if(itemStack.is(Items.HONEYCOMB_BLOCK)) {
			chargeValue = 4;
		} else if(itemStack.is(Items.HONEYCOMB)) {
			chargeValue = 1;
		}
		if(chargeValue != 0) {
			level.setBlock(blockPos, blockState.setValue(HONEY_COUNT, blockState.getValue(HONEY_COUNT) + chargeValue), Block.UPDATE_ALL);
			level.playSound(
					null,
					(double) blockPos.getX() + 0.5D,
					(double) blockPos.getY() + 0.5D,
					(double) blockPos.getZ() + 0.5D,
					ECSounds.VILLAGER_WORK_BEEKEEPER, SoundSource.BLOCKS,
					1.0F, 1.0F
			);
		}
	}

	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos blockPos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
		definition.add(HONEY_COUNT);
	}
}
