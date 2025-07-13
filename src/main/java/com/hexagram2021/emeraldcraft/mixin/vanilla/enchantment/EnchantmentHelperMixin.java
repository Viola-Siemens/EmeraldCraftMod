package com.hexagram2021.emeraldcraft.mixin.vanilla.enchantment;

import cn.sh1rocu.emeraldcraft.util.api.extension.IEnchantment;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyExpressionValue(
            method = "getAvailableEnchantmentResults",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean ec$canApplyAtEnchantingTable(boolean original, @Local(argsOnly = true) ItemStack stack, @Local Enchantment enchantment) {
        if (enchantment instanceof IEnchantment && !((IEnchantment) enchantment).canApplyAtEnchantingTable(stack)) {
            return false;
        }
        return original;
    }
}
