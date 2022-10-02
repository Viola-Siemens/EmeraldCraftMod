package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CarpentryTableRecipeSerializer<T extends CarpentryTableRecipe> implements RecipeSerializer<T>{
	private final CarpentryTableRecipeSerializer.Creator<T> factory;

	public CarpentryTableRecipeSerializer(CarpentryTableRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
	}

	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String s = GsonHelper.getAsString(json, "group", "");
		Ingredient ingredient;
		if (GsonHelper.isArrayNode(json, "ingredients")) {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
		} else {
			ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredients"));
		}

		String s1 = GsonHelper.getAsString(json, "result");
		int i = GsonHelper.getAsInt(json, "count");
		ItemStack itemstack = new ItemStack(Registry.ITEM.get(new ResourceLocation(s1)), i);
		return this.factory.create(id, s, ingredient, itemstack);
	}

	@Nullable
	@Override
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		return this.factory.create(id, group, ingredient, itemstack);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResultItem());
	}

	public interface Creator<T extends CarpentryTableRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack itemStack);
	}
}
