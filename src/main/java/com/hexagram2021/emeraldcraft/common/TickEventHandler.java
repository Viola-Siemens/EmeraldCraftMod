package com.hexagram2021.emeraldcraft.common;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MODID)
public class TickEventHandler {
	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.side.isServer() && !event.player.getAbilities().instabuild && event.player.isOnFire()) {
			for(EquipmentSlot slot : EquipmentSlot.values()) {
				if (slot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack itemstack = event.player.getItemBySlot(slot);
					if (ECItems.WOODEN_ARMOR.values().stream().anyMatch(armor -> itemstack.is(armor.get()))) {
						if(event.player.tickCount % 10 == 0) {
							itemstack.hurtAndBreak(1, event.player, player -> player.broadcastBreakEvent(slot));
							if(event.player.getRandom().nextInt(4) == 0) {
								event.player.setRemainingFireTicks(event.player.getRemainingFireTicks() + 4);
							}
						}
					}
				}
			}
		}
	}
}
