package com.hexagram2021.emeraldcraft.mixin.vanilla.entity;

import com.hexagram2021.emeraldcraft.common.register.ECEntityTypeTags;
import com.hexagram2021.emeraldcraft.common.register.ECMobTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getMobType()Lnet/minecraft/world/entity/MobType;"))
    private MobType emeraldcraft$checkIfMammals(LivingEntity instance, Operation<MobType> original) {
        if (instance.getType().is(ECEntityTypeTags.MAMMALS)) {
            return ECMobTypes.MAMMAL;
        }
        return original.call(instance);
    }
}
