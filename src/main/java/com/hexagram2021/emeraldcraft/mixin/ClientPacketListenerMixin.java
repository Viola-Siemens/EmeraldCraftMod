package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.crafting.menu.PiglinCuteyMerchantMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {
	protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
		super(minecraft, connection, cookie);
	}

	@SuppressWarnings("DataFlowIssue")
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
