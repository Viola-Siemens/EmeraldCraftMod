package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import net.minecraft.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(RecipeBookCategory.class)
public class RecipeBookCategoryMixin {
	public RecipeBookCategoryMixin(String name, int ord) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}

	@Final
	@Shadow
	@Mutable
	private static RecipeBookCategory[] $VALUES;

	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/item/crafting/RecipeBookCategory;$VALUES:[Lnet/minecraft/item/crafting/RecipeBookCategory;"))
	private static void injectEnum(CallbackInfo ci) {
		int ordinal = $VALUES.length;
		$VALUES = Arrays.copyOf($VALUES, ordinal + 1);
		ECRecipeBookTypes.GLASS_KILN = $VALUES[ordinal] = (RecipeBookCategory)(Object)new RecipeBookCategoryMixin("GLASS_KILN", ordinal);
	}
}
