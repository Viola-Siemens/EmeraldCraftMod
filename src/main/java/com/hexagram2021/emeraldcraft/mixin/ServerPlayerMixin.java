package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.BEFORE))
	public void makeHealedPiglinAngryAt(DamageSource damageSource, float value, CallbackInfoReturnable<Boolean> cir) {
		Entity entity = damageSource.getEntity();
		if(entity instanceof LivingEntity livingEntity && !(entity instanceof AbstractPiglin)) {
			ServerPlayer current = (ServerPlayer)(Object)this;
			List<AbstractPiglin> piglins = current.level.getNearbyEntities(
					AbstractPiglin.class, TargetingConditions.forNonCombat().range(64.0D), current, current.getBoundingBox().inflate(64.0D, 16.0D, 64.0D)
			);
			for(AbstractPiglin piglin: piglins) {
				PlayerHealable playerHealable = (PlayerHealable)piglin;
				if(playerHealable.isPlayerHealed() && playerHealable.getHealedPlayer() == current.getUUID()) {
					PiglinAi.maybeRetaliate(piglin, livingEntity);
				}
			}
		}
	}
}
