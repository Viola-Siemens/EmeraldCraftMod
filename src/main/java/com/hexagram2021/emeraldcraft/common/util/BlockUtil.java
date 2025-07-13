package com.hexagram2021.emeraldcraft.common.util;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class BlockUtil {
    @SuppressWarnings("UnusedReturnValue")
    public static boolean breakBlock(ServerPlayer player, BlockState blockState, @Nullable BlockEntity blockEntity, BlockPos pos, int exp) {
        Block block = blockState.getBlock();
        if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
            player.level().sendBlockUpdated(pos, blockState, blockState, 3);
            return false;
        }
/*        if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
            return false;
        }*/
        if (player.blockActionRestricted(player.level(), pos, player.gameMode.getGameModeForPlayer())) {
            return false;
        }
        if (player.isCreative()) {
            removeBlock(player.serverLevel(), player, pos, false);
        } else {
            ItemStack itemstack = player.getMainHandItem();
            ItemStack itemstack1 = itemstack.copy();
            /*boolean flag1 = blockState.canHarvestBlock(player.serverLevel(), pos, player);*/
            boolean flag1 = player.hasCorrectToolForDrops(blockState);
            itemstack.mineBlock(player.serverLevel(), blockState, pos, player);
            if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
                PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(player.level(), player, pos, blockState, blockEntity);
            }
            boolean flag = removeBlock(player.serverLevel(), player, pos, flag1);

            if (flag && flag1) {
                block.playerDestroy(player.serverLevel(), player, pos, blockState, blockEntity, itemstack1);
            }

            if (flag && exp > 0) {
                block.popExperience(player.serverLevel(), pos, exp);
            }
        }
        return true;
    }

    private static boolean removeBlock(Level level, ServerPlayer player, BlockPos blockPos, boolean canHarvest) {
        BlockState state = level.getBlockState(blockPos);
        /*boolean removed = state.onDestroyedByPlayer(level, blockPos, player, canHarvest, player.level().getFluidState(blockPos));*/
        boolean removed = onDestroyedByPlayer(state, level, blockPos, player, canHarvest, player.level().getFluidState(blockPos));
        if (removed) {
            state.getBlock().destroy(level, blockPos, state);
        }
        return removed;
    }

    //from forge
    public static boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos blockPos, ServerPlayer player, boolean canHarvest, FluidState fluid) {
        state.getBlock().playerWillDestroy(level, blockPos, state, player);
        return level.setBlock(blockPos, player.level().getFluidState(blockPos).createLegacyBlock(), level.isClientSide ? 11 : 3);
    }

    public static int getDestroyBlockExp(ServerPlayer player, BlockState blockState, BlockPos blockPos) {
        /*if (!ForgeHooks.isCorrectToolForDrops(blockState, player)) {*/
        if (!player.hasCorrectToolForDrops(blockState)) {
            return 0;
        }
        int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getMainHandItem());
        int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
        return getExpDrop(blockState, player.level(), player.level().random, blockPos, fortuneLevel, silkTouchLevel);
    }

    public static int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        Block block = state.getBlock();
        if (block instanceof DropExperienceBlock)
            return silkTouchLevel == 0 ? ((DropExperienceBlock) block).xpRange.sample(randomSource) : 0;
        if (block instanceof RedStoneOreBlock)
            return silkTouchLevel == 0 ? 1 + randomSource.nextInt(5) : 0;
        if (block instanceof SculkCatalystBlock)
            return silkTouchLevel == 0 ? ((SculkCatalystBlock) block).xpRange.sample(randomSource) : 0;
        if (block instanceof SculkSensorBlock)
            return silkTouchLevel == 0 ? 5 : 0;
        if (block instanceof SculkShriekerBlock)
            return silkTouchLevel == 0 ? 5 : 0;
        if (block instanceof SpawnerBlock)
            return 15 + randomSource.nextInt(15) + randomSource.nextInt(15);
        return 0;
    }
}
