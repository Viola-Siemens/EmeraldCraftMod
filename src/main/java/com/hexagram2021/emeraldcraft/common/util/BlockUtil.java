package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class BlockUtil {
	@SuppressWarnings("UnusedReturnValue")
	public static boolean breakBlock(ServerPlayer player, BlockState blockState, @Nullable BlockEntity blockEntity, BlockPos pos, int exp) {
		Block block = blockState.getBlock();
		if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
			player.level.sendBlockUpdated(pos, blockState, blockState, 3);
			return false;
		}
		if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
			return false;
		}
		if (player.blockActionRestricted(player.level, pos, player.gameMode.getGameModeForPlayer())) {
			return false;
		}
		if (player.isCreative()) {
			removeBlock(player.getLevel(), player, pos, false);
		} else {
			ItemStack itemstack = player.getMainHandItem();
			ItemStack itemstack1 = itemstack.copy();
			boolean flag1 = blockState.canHarvestBlock(player.getLevel(), pos, player);
			itemstack.mineBlock(player.getLevel(), blockState, pos, player);
			if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
			}
			boolean flag = removeBlock(player.getLevel(), player, pos, flag1);

			if (flag && flag1) {
				block.playerDestroy(player.getLevel(), player, pos, blockState, blockEntity, itemstack1);
			}

			if (flag && exp > 0) {
				block.popExperience(player.getLevel(), pos, exp);
			}
		}
		return true;
	}

	private static boolean removeBlock(Level level, ServerPlayer player, BlockPos blockPos, boolean canHarvest) {
		BlockState state = level.getBlockState(blockPos);
		boolean removed = state.onDestroyedByPlayer(level, blockPos, player, canHarvest, player.level.getFluidState(blockPos));
		if (removed) {
			state.getBlock().destroy(level, blockPos, state);
		}
		return removed;
	}

	public static int getDestroyBlockExp(ServerPlayer player, BlockState blockState, BlockPos blockPos) {
		if (blockState == null || !ForgeHooks.isCorrectToolForDrops(blockState, player)) {
			return 0;
		}
		int fortuneLevel = player.getMainHandItem().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
		int silkTouchLevel = player.getMainHandItem().getEnchantmentLevel(Enchantments.SILK_TOUCH);
		return blockState.getExpDrop(player.level, player.level.random, blockPos, fortuneLevel, silkTouchLevel);
	}
}
