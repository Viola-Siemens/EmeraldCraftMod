package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

public class CarpentryTableRecipeSerializer<T extends CarpentryTableRecipe> implements RecipeSerializer<T>{
	private final CarpentryTableRecipeSerializer.Creator<T> factory;

	public CarpentryTableRecipeSerializer(CarpentryTableRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
	}

	@SuppressWarnings("deprecation")
	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");
		Ingredient ingredient;
		if (GsonHelper.isArrayNode(json, "ingredients")) {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
		} else {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredients"));
		}

		String result = GsonHelper.getAsString(json, "result");
		int count = GsonHelper.getAsInt(json, "count");
		ItemStack itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(new ResourceLocation(result)).orElseThrow(
				() -> new IllegalStateException("Item: " + result + " does not exist")
		), count);
		return this.factory.create(id, group, ingredient, itemstack);
	}

	@Override @Nullable
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		return this.factory.create(id, group, ingredient, itemstack);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResult());
	}

	public interface Creator<T extends CarpentryTableRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack itemStack);
	}
}
