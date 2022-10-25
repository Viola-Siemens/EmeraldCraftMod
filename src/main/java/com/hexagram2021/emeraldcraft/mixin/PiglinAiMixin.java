package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {
	@Inject(method = "setAngerTarget", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreIfPlayerHealed(AbstractPiglin piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}

	@Redirect(method = "findNearestValidAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensor;isEntityAttackableIgnoringLineOfSight(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z", ordinal = 0))
	private static boolean checkAngryIfPlayerHealed(LivingEntity piglin, LivingEntity livingEntity) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, Optional.empty());
				return false;
			}
		}
		return Sensor.isEntityAttackableIgnoringLineOfSight(piglin, livingEntity);
	}

	@Redirect(method = "findNearestValidAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/Sensor;isEntityAttackable(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z", ordinal = 0))
	private static boolean checkNotWearingGoldIfPlayerHealed(LivingEntity piglin, LivingEntity livingEntity) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				piglin.getBrain().setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.empty());
				return false;
			}
		}
		return Sensor.isEntityAttackableIgnoringLineOfSight(piglin, livingEntity);
	}

	@Inject(method = "wasHurtBy", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreHurtByPlayerIfHealed(Piglin piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}
}
