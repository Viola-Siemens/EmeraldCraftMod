package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerHealable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PiglinBruteAi.class)
public class PiglinBruteAiMixin {
	@Redirect(method = "findNearestValidAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinBruteAi;getTargetIfWithinRange(Lnet/minecraft/world/entity/monster/piglin/AbstractPiglin;Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)Ljava/util/Optional;"))
	private static Optional<? extends LivingEntity> ignoreIfPlayerHealed(AbstractPiglin piglin, MemoryModuleType<? extends LivingEntity> type) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed()) {
				return Optional.empty();
			}
		}
		return piglin.getBrain().getMemory(type).filter((entity) -> entity.closerThan(piglin, 12.0D));
	}

	@Inject(method = "wasHurtBy", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreHurtByPlayerIfHealed(PiglinBrute piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable healable) {
			if(healable.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}
}
