package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

import static com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes.*;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
	@Inject(method = "getCategory", at = @At(value = "HEAD"), cancellable = true)
	private static void injectCustomRecipeTypes(IRecipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
		if(recipe.getType() == ECRecipes.GLASS_KILN_TYPE) {
			if(recipe.getResultItem().getDescriptionId().contains("glass")) {
				cir.setReturnValue(GLASS_KILN_SAND);
				cir.cancel();
				return;
			}
			if(Arrays.stream(recipe.getIngredients().get(0).getItems()).anyMatch(itemStack -> itemStack.getDescriptionId().contains("terracotta"))) {
				cir.setReturnValue(GLASS_KILN_TERRACOTTA);
				cir.cancel();
				return;
			}
			cir.setReturnValue(GLASS_KILN_CLAY);
			cir.cancel();
		}
	}
}
