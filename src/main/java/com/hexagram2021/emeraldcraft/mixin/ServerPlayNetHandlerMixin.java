package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerRideableFlying;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CEntityActionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions.*;

@Mixin(ServerPlayNetHandler.class)
public class ServerPlayNetHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;
	
	
	@Inject(method = "handlePlayerCommand", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/entity/player/ServerPlayerEntity;resetLastActionTime()V"), cancellable = true)
	public void handleMantaCommand(CEntityActionPacket packet, CallbackInfo ci) {
		if(packet.getAction() == RIDING_FLY) {
			if (this.player.getVehicle() instanceof PlayerRideableFlying) {
				PlayerRideableFlying flyable = (PlayerRideableFlying)this.player.getVehicle();
				flyable.fly(packet.getData());
			}
			ci.cancel();
		}
	}
}
