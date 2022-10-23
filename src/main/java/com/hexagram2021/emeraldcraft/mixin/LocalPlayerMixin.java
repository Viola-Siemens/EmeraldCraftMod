package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerRideableFlying;
import com.hexagram2021.emeraldcraft.common.register.ECEntityActionPacketActions;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class LocalPlayerMixin {
	@Shadow
	private int jumpRidingTicks;

	@Shadow
	public MovementInput input;
	
	@Shadow @Final public ClientPlayNetHandler connection;
	
	@Inject(method = "aiStep", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;aiStep()V"))
	public void handlePlayerFlyable(CallbackInfo ci) {
		ClientPlayerEntity current = (ClientPlayerEntity)(Object)this;
		Entity entity = current.getVehicle();
		if(entity instanceof PlayerRideableFlying) {
			PlayerRideableFlying flyable = (PlayerRideableFlying)entity;
			if(flyable.canFly()) {
				if (this.input.jumping) {
					if (this.jumpRidingTicks < 40) {
						++this.jumpRidingTicks;
					}
				} else {
					if (entity.isOnGround()) {
						if (this.jumpRidingTicks < 10) {
							this.jumpRidingTicks = 10;
						}
					} else if (this.jumpRidingTicks > 0) {
						--this.jumpRidingTicks;
					}
				}
				this.connection.send(new CEntityActionPacket(current, ECEntityActionPacketActions.RIDING_FLY, this.jumpRidingTicks));
			}
		}
	}
}
