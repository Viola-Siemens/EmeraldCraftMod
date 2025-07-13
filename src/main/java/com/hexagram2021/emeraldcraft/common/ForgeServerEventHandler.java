package com.hexagram2021.emeraldcraft.common;

import cn.sh1rocu.emeraldcraft.util.api.event.BlockToolModificationEvent;
import cn.sh1rocu.emeraldcraft.util.api.event.PlayerContainerEvent;
import cn.sh1rocu.emeraldcraft.util.api.event.PlayerTickEvent;
import com.hexagram2021.emeraldcraft.api.events.FarciFoodComputeNutritionEvent;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IFluidSyncMenu;
import com.hexagram2021.emeraldcraft.common.enchantments.VeinMiningEnchantment;
import com.hexagram2021.emeraldcraft.common.register.ECEnchantments;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.BlockUtil;
import com.hexagram2021.emeraldcraft.mixin.accessor.AxeItemAccess;
import com.hexagram2021.emeraldcraft.network.NetworkHandler;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;

public class ForgeServerEventHandler {
    public static void init() {
        onContainerOpened();
        onContainerClosed();
        onBuildFarciFood();
        onToolUse();
        playerTickEvent();
        blockBreakEvent();
    }

    public static void onContainerOpened() {
        PlayerContainerEvent.OPEN.register((player, container) -> {
            if (container instanceof IFluidSyncMenu fluidSyncMenu && player instanceof ServerPlayer) {
                fluidSyncMenu.addUsingPlayer(player);
                NetworkHandler.sendMessageToPlayer(fluidSyncMenu.getSyncPacket(), player);
            }
        });
    }

    public static void onContainerClosed() {
        PlayerContainerEvent.CLOSE.register((player, container) -> {
            if (container instanceof IFluidSyncMenu fluidSyncMenu && player instanceof ServerPlayer) {
                fluidSyncMenu.removeUsingPlayer(player);
            }
        });
    }

    public static void onBuildFarciFood() {
        FarciFoodComputeNutritionEvent.EVENT.register(event -> {
            if (event.getItem() == Items.POTATO && event.isCooked()) {
                event.addNutrition((Foods.BAKED_POTATO.getNutrition() + 1) / 2 - (Foods.POTATO.getNutrition() + 1) / 2);
            }
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void onToolUse() {
        BlockToolModificationEvent.AXE_STRIP.register(((context, pos, state) -> {
            Level level = context.getLevel();
            if (level.isClientSide()) {
                return;
            }
            Direction direction = context.getClickedFace();
            if (AxeItemAccess.getStrippedBlocks().containsKey(state.getBlock()) && state.is(BlockTags.LOGS)) {
                if (level.getRandom().nextInt(4) == 0) {
                    ItemEntity itemEntity = new ItemEntity(
                            level,
                            pos.getX() + 0.5D + 0.6D * direction.getStepX(),
                            pos.getY() + 0.5D + 0.6D * direction.getStepY(),
                            pos.getZ() + 0.5D + 0.6D * direction.getStepZ(),
                            new ItemStack(ECItems.BARK, level.getRandom().nextInt(2) + 1)
                    );
                    itemEntity.setDeltaMovement(
                            direction.getStepX() * 0.2D + (level.getRandom().nextDouble() - 0.5D) * 0.15D,
                            direction.getStepY() * 0.2D + (level.getRandom().nextDouble() - 0.5D) * 0.15D,
                            direction.getStepZ() * 0.2D + (level.getRandom().nextDouble() - 0.5D) * 0.15D
                    );
                    level.addFreshEntity(itemEntity);
                }
            }
        }));

    }

    public static void playerTickEvent() {
        PlayerTickEvent.END.register(player -> {
            if (player instanceof ServerPlayer && !player.getAbilities().instabuild && player.isOnFire()) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                        ItemStack itemstack = player.getItemBySlot(slot);
                        if (ECItems.WOODEN_ARMOR.values().stream().anyMatch(armor -> itemstack.is(armor.get()))) {
                            if (player.tickCount % 10 == 0) {
                                int maxDamage = itemstack.getMaxDamage();
                                if (maxDamage > 0 && player.getRandom().nextInt(maxDamage) >= itemstack.getDamageValue() * 9 / 10 - 1) {
                                    itemstack.hurtAndBreak(1, player, targetPlayer -> targetPlayer.broadcastBreakEvent(slot));
                                }
                                if (player.getRandom().nextInt(4) == 0) {
                                    player.setRemainingFireTicks(player.getRemainingFireTicks() + 4);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static void blockBreakEvent() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayer && player.isShiftKeyDown()) {
                ItemStack handItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                int lvl = EnchantmentHelper.getItemEnchantmentLevel(ECEnchantments.VEIN_MINING, handItem);
                if (VeinMiningEnchantment.canWorkWhenHolding(handItem) && lvl > 0) {
                    lvl = Mth.clamp(lvl, 1, 32);
                    int flag = lvl % 3;
                    int radius = (lvl + 2) / 3;

                    BlockState templateBlock = player.level().getBlockState(pos);
                    BlockEntity templateBlockEntity = player.level().getBlockEntity(pos);
                    if (!templateBlock.isAir() && templateBlockEntity == null) {
                        searchInRadius(radius, (x, y, z) -> {
                            if (x * x + y * y + z * z <= radius * radius + flag * flag) {
                                BlockPos current = pos.offset(x, y, z);
                                if (!current.equals(pos)) {
                                    BlockState blockState = player.level().getBlockState(current);
                                    BlockEntity currentBlockEntity = player.level().getBlockEntity(current);
                                    if (blockState.is(templateBlock.getBlock())) {
                                        BlockUtil.breakBlock(
                                                (ServerPlayer) player, blockState, currentBlockEntity, current,
                                                BlockUtil.getDestroyBlockExp((ServerPlayer) player, blockState, current)
                                        );
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private static void searchInRadius(int radius, TriConsumer<Integer, Integer, Integer> consumer) {
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    consumer.accept(x, y, z);
                }
            }
        }
    }
}
