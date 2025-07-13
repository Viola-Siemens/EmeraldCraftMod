package com.hexagram2021.emeraldcraft.mixin.compat.diet;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import com.illusivesoulworks.diet.common.DietApiImpl;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.BiFunction;

@Mixin(DietApiImpl.class)
public class DietApiImplMixin {
    @ModifyExpressionValue(
            remap = false,
            method = "getGroups",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/illusivesoulworks/diet/platform/services/IRegistryService;getOverride(Lnet/minecraft/world/item/Item;)Ljava/util/function/BiFunction;"
            )
    )
    private BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>> ec$getGroups_getOverride(
            BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>> original,
            @Local(ordinal = 1) ItemStack foodItem
    ) {
        if (BuiltInRegistries.ITEM.getKey(foodItem.getItem()).getNamespace().equals(EmeraldCraft.MODID)) {
            return ECFoods.dietItems.get(foodItem.getItem());
        }
        return original;
    }

    @ModifyExpressionValue(
            remap = false,
            method = "get(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lcom/illusivesoulworks/diet/api/type/IDietResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/illusivesoulworks/diet/platform/services/IRegistryService;getOverride(Lnet/minecraft/world/item/Item;)Ljava/util/function/BiFunction;"
            )
    )
    private BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>> ec$get_getOverride(
            BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>> original,
            @Local(argsOnly = true) ItemStack foodItem
    ) {
        if (BuiltInRegistries.ITEM.getKey(foodItem.getItem()).getNamespace().equals(EmeraldCraft.MODID)) {
            return ECFoods.dietItems.get(foodItem.getItem());
        }
        return original;
    }
}
