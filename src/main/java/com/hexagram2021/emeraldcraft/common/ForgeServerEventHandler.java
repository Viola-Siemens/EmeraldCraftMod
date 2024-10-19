package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IFluidSyncMenu;
import com.hexagram2021.emeraldcraft.common.enchantments.VeinMiningEnchantment;
import com.hexagram2021.emeraldcraft.common.items.capabilities.ItemStackFoodHandler;
import com.hexagram2021.emeraldcraft.common.items.foods.FarciFoodItem;
import com.hexagram2021.emeraldcraft.common.register.ECCapabilities;
import com.hexagram2021.emeraldcraft.common.register.ECEnchantments;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.util.TriConsumer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MODID)
public class ForgeServerEventHandler {
	@SubscribeEvent
	public static void onContainerOpened(PlayerContainerEvent.Open event) {
		if(event.getContainer() instanceof IFluidSyncMenu fluidSyncMenu && event.getEntity() instanceof ServerPlayer serverPlayer) {
			fluidSyncMenu.addUsingPlayer(serverPlayer);
			EmeraldCraft.sendMessageToPlayer(fluidSyncMenu.getSyncPacket(), serverPlayer.connection.getConnection());
		}
	}
	@SubscribeEvent
	public static void onContainerClosed(PlayerContainerEvent.Close event) {
		if(event.getContainer() instanceof IFluidSyncMenu fluidSyncMenu && event.getEntity() instanceof ServerPlayer serverPlayer) {
			fluidSyncMenu.removeUsingPlayer(serverPlayer);
		}
	}

	@SubscribeEvent
	public static void onAttackItemStackCapability(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() instanceof FarciFoodItem farciFoodItem) {
			event.addCapability(ECCapabilities.FOOD_CAPABILITY_ID, new ItemStackFoodHandler(event.getObject(), farciFoodItem));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onToolUse(BlockEvent.BlockToolModificationEvent event) {
		if(event.isSimulated() || event.getLevel().isClientSide()) {
			return;
		}
		if (event.getToolAction().equals(ToolActions.AXE_STRIP)) {
			Level level = event.getContext().getLevel();
			BlockPos pos = event.getPos();
			Direction direction = event.getContext().getClickedFace();
			BlockState original = level.getBlockState(pos);
			if(AxeItem.STRIPPABLES.containsKey(original.getBlock()) && original.is(BlockTags.LOGS)) {
				if(level.getRandom().nextInt(4) == 0) {
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
		}
	}

	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.side.isServer() && !event.player.getAbilities().instabuild && event.player.isOnFire()) {
			for(EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack itemstack = event.player.getItemBySlot(slot);
					if (ECItems.WOODEN_ARMOR.values().stream().anyMatch(armor -> itemstack.is(armor.get()))) {
						if(event.player.tickCount % 10 == 0) {
							int maxDamage = itemstack.getMaxDamage();
							if(maxDamage > 0 && event.player.getRandom().nextInt(maxDamage) >= itemstack.getDamageValue() * 9 / 10 - 1) {
								itemstack.hurtAndBreak(1, event.player, player -> player.broadcastBreakEvent(slot));
							}
							if(event.player.getRandom().nextInt(4) == 0) {
								event.player.setRemainingFireTicks(event.player.getRemainingFireTicks() + 4);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void blockBreakEvent(final BlockEvent.BreakEvent event) {
		if (event.getPlayer() instanceof ServerPlayer player && player.isShiftKeyDown()) {
			ItemStack handItem = player.getItemInHand(InteractionHand.MAIN_HAND);
			int lvl = handItem.getEnchantmentLevel(ECEnchantments.VEIN_MINING.get());
			if(VeinMiningEnchantment.canWorkWhenHolding(handItem) && lvl > 0) {
				lvl = Mth.clamp(lvl, 1, 32);
				int flag = lvl % 3;
				int radius = (lvl + 2) / 3;

				BlockPos pos = event.getPos();
				BlockState templateBlock = player.level().getBlockState(pos);
				BlockEntity templateBlockEntity = player.level().getBlockEntity(pos);
				if(!templateBlock.isAir() && templateBlockEntity == null) {
					searchInRadius(radius, (x, y, z) -> {
						if (x * x + y * y + z * z <= radius * radius + flag * flag) {
							BlockPos current = pos.offset(x, y, z);
							if (!current.equals(pos)) {
								BlockState blockState = player.level().getBlockState(current);
								BlockEntity blockEntity = player.level().getBlockEntity(current);
								if (blockState.is(templateBlock.getBlock())) {
									BlockUtil.breakBlock(
											player, blockState, blockEntity, current,
											BlockUtil.getDestroyBlockExp(player, blockState, current)
									);
								}
							}
						}
					});
				}
			}
		}
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
