package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteBrain;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteAiMixin {
	@Redirect(method = "findNearestValidAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/piglin/PiglinBruteBrain;getTargetIfWithinRange(Lnet/minecraft/entity/monster/piglin/AbstractPiglinEntity;Lnet/minecraft/entity/ai/brain/memory/MemoryModuleType;)Ljava/util/Optional;"))
	private static Optional<? extends LivingEntity> ignoreIfPlayerHealed(AbstractPiglinEntity piglin, MemoryModuleType<? extends LivingEntity> type) {
		if(piglin instanceof PlayerHealable) {
			if(((PlayerHealable)piglin).isPlayerHealed()) {
				return Optional.empty();
			}
		}
		return piglin.getBrain().getMemory(type).filter((entity) -> entity.closerThan(piglin, 12.0D));
	}

	@Inject(method = "wasHurtBy", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreHurtByPlayerIfHealed(PiglinBruteEntity piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable) {
			if(((PlayerHealable)piglin).isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}
}
