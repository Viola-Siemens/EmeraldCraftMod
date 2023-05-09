package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.entity.animal.Cat;
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

@Mixin(Cat.class)
public class CatEntityMixin {
	@Shadow @Final @Mutable
	private static Ingredient TEMPT_INGREDIENT;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Cat;TEMPT_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;", shift = At.Shift.AFTER))
	private static void addECFishes(CallbackInfo ci) {
		TEMPT_INGREDIENT = Ingredient.of(ArrayUtils.addAll(
				TEMPT_INGREDIENT.getItems(),
				new ItemStack(ECItems.HERRING),
				new ItemStack(ECItems.PURPLE_SPOTTED_BIGEYE)
		));
	}
}
