package com.hexgram2021.emeraldcraft.api.crafting;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Function;

public abstract class ECRecipeSerializer <R extends Recipe<?>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<R> {
	public abstract ItemStack getIcon();

	@Override
	public final R fromJson(ResourceLocation recipeId, JsonObject json) {
		if(CraftingHelper.processConditions(json, "conditions"))
			return readFromJson(recipeId, json);
		return null;
	}

	protected ItemStack readOutput(JsonElement outputObject) {
		if(outputObject.isJsonObject()&&outputObject.getAsJsonObject().has("item"))
			return ShapedRecipe.itemStackFromJson(outputObject.getAsJsonObject());
		IngredientWithSize outgredient = IngredientWithSize.deserialize(outputObject);
		return getPreferredStack(outgredient.getMatchingStacks());
	}

	private ItemStack getPreferredStack(ItemStack[] matchingStacks) {
		ItemStack preferredStack = null;
		Function<ItemStack, ResourceLocation> getName = stack -> stack.getItem().getRegistryName();

		for(ItemStack stack : matchingStacks)
		{
			ResourceLocation rl = getName.apply(stack);
			if(rl==null)
				continue;
			if(preferredStack==null)
				preferredStack = stack;
			else if(rl.compareTo(getName.apply(preferredStack)) < 0)
				preferredStack = stack;
		}
		Preconditions.checkNotNull(preferredStack, "No entry found in %s", matchingStacks);
		return preferredStack;
	}

	public abstract R readFromJson(ResourceLocation recipeId, JsonObject json);
}
