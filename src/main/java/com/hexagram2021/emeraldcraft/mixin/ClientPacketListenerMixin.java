package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.crafting.menu.PiglinCuteyMerchantMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Shadow @Final
	private Minecraft minecraft;

	@Inject(method = "handleMerchantOffers", at = @At(value = "TAIL"))
	public void handlePiglinCuteyMerchantOffers(ClientboundMerchantOffersPacket packet, CallbackInfo ci) {
		AbstractContainerMenu menu = this.minecraft.player.containerMenu;
		if (packet.getContainerId() == menu.containerId && menu instanceof PiglinCuteyMerchantMenu merchantMenu) {
			merchantMenu.setOffers(new MerchantOffers(packet.getOffers().createTag()));
			merchantMenu.setXp(packet.getVillagerXp());
			merchantMenu.setMerchantLevel(packet.getVillagerLevel());
			merchantMenu.setShowProgressBar(packet.showProgress());
			merchantMenu.setCanRestock(packet.canRestock());
		}
	}
}
