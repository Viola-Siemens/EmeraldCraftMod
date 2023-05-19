package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chicken.class)
public class ChickenEntityMixin {
	@Shadow @Final @Mutable
	private static Ingredient FOOD_ITEMS;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Chicken;FOOD_ITEMS:Lnet/minecraft/world/item/crafting/Ingredient;", shift = At.Shift.AFTER))
	private static void addECSeeds(CallbackInfo ci) {
		FOOD_ITEMS = Ingredient.of(ArrayUtils.addAll(
				FOOD_ITEMS.getItems(),
				new ItemStack(ECItems.CHILI_SEED),
				new ItemStack(ECItems.CABBAGE_SEED)
		));
	}
}
