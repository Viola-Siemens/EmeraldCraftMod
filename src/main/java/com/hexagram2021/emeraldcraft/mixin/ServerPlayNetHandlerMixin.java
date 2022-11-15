package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerRideableFlying;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions.*;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetHandlerMixin {
	@Shadow
	public ServerPlayer player;

	@Inject(method = "handlePlayerCommand", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/level/ServerPlayer;resetLastActionTime()V"), cancellable = true)
	public void handleMantaCommand(ServerboundPlayerCommandPacket packet, CallbackInfo ci) {
		if(packet.getAction() == RIDING_FLY) {
			if (this.player.getVehicle() instanceof PlayerRideableFlying flyable) {
				flyable.fly(packet.getData());
			}
			ci.cancel();
		}
	}
}
