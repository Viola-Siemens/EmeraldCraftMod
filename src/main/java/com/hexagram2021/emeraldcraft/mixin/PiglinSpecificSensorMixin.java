package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerHealable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {
	@Redirect(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"))
	private boolean ignoreIfPlayerHealed(LivingEntity instance, LivingEntity target) {
		if(instance instanceof PlayerHealable healable && healable.isPlayerHealed()) {
			return false;
		}
		return instance.canAttack(target);
	}
}
