package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerRideableFlying;
import com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
	@Shadow
	private int jumpRidingTicks;

	@Shadow
	public Input input;

	@Shadow @Final
	public ClientPacketListener connection;

	@Inject(method = "aiStep", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/player/AbstractClientPlayer;aiStep()V"))
	public void handlePlayerFlyable(CallbackInfo ci) {
		LocalPlayer current = (LocalPlayer)(Object)this;
		if(current.isPassenger() && current.getVehicle() instanceof PlayerRideableFlying flyable && flyable.canFly()) {
			if(this.input.jumping) {
				if(this.jumpRidingTicks < 40) {
					++this.jumpRidingTicks;
				}
			} else {
				if(current.getVehicle().isOnGround()) {
					if(this.jumpRidingTicks < 10) {
						this.jumpRidingTicks = 10;
					}
				} else if(this.jumpRidingTicks > 0) {
					--this.jumpRidingTicks;
				}
			}
			this.connection.send(new ServerboundPlayerCommandPacket(current, ECEntityActionPacketActions.RIDING_FLY, this.jumpRidingTicks));
		}
	}
}
