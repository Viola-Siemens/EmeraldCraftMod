package com.hexagram2021.emeraldcraft.mixin.vanilla.food;

import cn.sh1rocu.emeraldcraft.util.api.extension.IItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @ModifyExpressionValue(
            method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;getFoodProperties()Lnet/minecraft/world/food/FoodProperties;"
            )
    )
    private FoodProperties ec$getFoodProperties(FoodProperties original, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof IItem item)
            return item.getFoodProperties(stack, null);
        return original;
    }
}
