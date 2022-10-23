package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PiglinTasks.class)
public class PiglinAiMixin {
	@Inject(method = "setAngerTarget", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreIfPlayerHealed(AbstractPiglinEntity piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable) {
			if(((PlayerHealable)piglin).isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}

	@Redirect(method = "findNearestValidAttackTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/piglin/PiglinEntity;getBrain()Lnet/minecraft/entity/ai/brain/Brain;"))
	private static Brain<PiglinEntity> checkAngryIfPlayerHealed(PiglinEntity piglin) {
		Brain<PiglinEntity> ret = piglin.getBrain();
		if(piglin instanceof PlayerHealable && ((PlayerHealable)piglin).isPlayerHealed()) {
			Optional<LivingEntity> memory1 = BrainUtil.getLivingEntityFromUUIDMemory(piglin, MemoryModuleType.ANGRY_AT);
			if(memory1.isPresent()) {
				if(memory1.get().getType() == EntityType.PLAYER) {
					piglin.getBrain().setMemory(MemoryModuleType.ANGRY_AT, Optional.empty());
				}
			}
			Optional<PlayerEntity> memory2 = ret.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
			if(memory2.isPresent()) {
				ret.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, Optional.empty());
			}
		}

		return ret;
	}

	@Inject(method = "wasHurtBy", at = @At(value = "HEAD"), cancellable = true)
	private static void ignoreHurtByPlayerIfHealed(PiglinEntity piglin, LivingEntity livingEntity, CallbackInfo ci) {
		if(piglin instanceof PlayerHealable) {
			if(((PlayerHealable)piglin).isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER) {
				ci.cancel();
			}
		}
	}
}
