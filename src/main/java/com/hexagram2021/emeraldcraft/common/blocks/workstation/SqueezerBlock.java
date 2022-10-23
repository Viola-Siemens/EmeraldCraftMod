package com.hexagram2021.emeraldcraft.common.blocks.workstation;

import com.hexagram2021.emeraldcraft.common.register.ECProperties;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SqueezerBlock extends Block {
	public static final IntegerProperty HONEY_COUNT = ECProperties.HONEY_COUNT;

	public static final Supplier<Properties> PROPERTIES = () -> Block.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(0.6F);

	public SqueezerBlock(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(HONEY_COUNT, 0));
	}

	@Override @Nonnull
	public ActionResultType use(@Nonnull BlockState blockState, @Nonnull World level, @Nonnull BlockPos blockPos, PlayerEntity player,
								@Nonnull Hand interactionHand, @Nonnull BlockRayTraceResult blockHitResult) {
		ItemStack itemstack = player.getItemInHand(interactionHand);
		ItemStack itemstack2 = player.getItemInHand(Hand.OFF_HAND);
		if (interactionHand == Hand.MAIN_HAND &&
				!isSqueezable(itemstack) && !isEmptyBottle(itemstack) && !isShears(itemstack) &&
				(isSqueezable(itemstack2) || isEmptyBottle(itemstack2) || isShears(itemstack2))) {
			return ActionResultType.PASS;
		}

		if (isSqueezable(itemstack) && canBeCharged(blockState)) {
			charge(level, blockPos, blockState);
			if (!player.abilities.instabuild) {
				itemstack.shrink(1);
			}

			return ActionResultType.sidedSuccess(level.isClientSide);
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
						SoundCategory.NEUTRAL,
						1.0F, 1.0F
				);
				if (itemstack.isEmpty()) {
					player.setItemInHand(interactionHand, new ItemStack(Items.HONEY_BOTTLE));
				} else if (!player.inventory.add(new ItemStack(Items.HONEY_BOTTLE))) {
					player.drop(new ItemStack(Items.HONEY_BOTTLE), false);
				}
				resetHoneyCount(level, blockState, blockPos);

				return ActionResultType.sidedSuccess(level.isClientSide);
			}
			if (isShears(itemstack)) {
				level.playSound(
						null,
						(double)blockPos.getX() + 0.5D,
						(double)blockPos.getY() + 0.5D,
						(double)blockPos.getZ() + 0.5D,
						SoundEvents.BEEHIVE_SHEAR,
						SoundCategory.NEUTRAL,
						1.0F, 1.0F
				);
				dropHoneycomb(level, blockPos);
				itemstack.hurtAndBreak(1, player, (player2) -> {
					player2.broadcastBreakEvent(interactionHand);
				});
				resetHoneyCount(level, blockState, blockPos);

				return ActionResultType.sidedSuccess(level.isClientSide);
			}
		}
		return ActionResultType.CONSUME;
	}

	public static void dropHoneycomb(World level, BlockPos blockPos) {
		popResource(level, blockPos, new ItemStack(Items.HONEYCOMB));
	}

	public boolean hasAnalogOutputSignal(@Nonnull BlockState blockState) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState blockState, @Nonnull World level, @Nonnull BlockPos blockPos) {
		return blockState.getValue(HONEY_COUNT);
	}

	private static boolean isSqueezable(ItemStack itemStack) {
		return itemStack.getItem() == Items.HONEYCOMB_BLOCK;
	}

	private static boolean isEmptyBottle(ItemStack itemStack) {
		return itemStack.getItem() == Items.GLASS_BOTTLE;
	}

	private static boolean isShears(ItemStack itemStack) {
		return itemStack.getItem() == Items.SHEARS;
	}

	private static boolean canBeCharged(BlockState blockState) {
		return blockState.getValue(HONEY_COUNT) < 4;
	}

	public void resetHoneyCount(World level, BlockState blockState, BlockPos blockPos) {
		level.setBlock(blockPos, blockState.setValue(HONEY_COUNT, blockState.getValue(HONEY_COUNT) - 1), 3);
	}

	public static void charge(World level, BlockPos blockPos, BlockState blockState) {
		level.setBlock(blockPos, blockState.setValue(HONEY_COUNT, blockState.getValue(HONEY_COUNT) + 4), 3);
		level.playSound(
				null,
				(double)blockPos.getX() + 0.5D,
				(double)blockPos.getY() + 0.5D,
				(double)blockPos.getZ() + 0.5D,
				ECSounds.VILLAGER_WORK_BEEKEEPER, SoundCategory.BLOCKS,
				1.0F, 1.0F
		);
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader getter, @Nonnull BlockPos blockPos, @Nonnull PathType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> definition) {
		definition.add(HONEY_COUNT);
	}
}
