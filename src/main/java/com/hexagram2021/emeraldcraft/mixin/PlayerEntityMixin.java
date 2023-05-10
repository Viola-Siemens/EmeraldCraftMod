package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECEntityTypeTags;
import com.hexagram2021.emeraldcraft.common.register.ECMobTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerEntityMixin {
	@Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getMobType()Lnet/minecraft/world/entity/MobType;"))
	private MobType checkIfMammals(LivingEntity instance) {
		if(instance.getType().is(ECEntityTypeTags.MAMMALS)) {
			return ECMobTypes.MAMMAL;
		}
		return instance.getMobType();
	}
}
