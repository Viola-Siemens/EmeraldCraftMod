package com.hexagram2021.emeraldcraft.mixin.vanilla.entity;

import cn.sh1rocu.emeraldcraft.util.api.extension.IItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyExpressionValue(
            method = "addEatEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;"
            )
    )
    private FoodProperties ec$getFoodProperties(
            FoodProperties original,
            @Local(argsOnly = true) ItemStack food,
            @Local(argsOnly = true) LivingEntity livingEntity
    ) {
        if (food.getItem() instanceof IItem item)
            return item.getFoodProperties(food, livingEntity);
        return original;
    }
}
