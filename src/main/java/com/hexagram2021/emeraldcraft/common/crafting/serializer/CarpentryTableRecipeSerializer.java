package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CarpentryTableRecipeSerializer<T extends CarpentryTableRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>{
	private final CarpentryTableRecipeSerializer.Creator<T> factory;

	public CarpentryTableRecipeSerializer(CarpentryTableRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
	}

	@Override @Nonnull
	public T fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
		String s = JSONUtils.getAsString(json, "group", "");
		Ingredient ingredient;
		if (JSONUtils.isArrayNode(json, "ingredients")) {
			ingredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
		} else {
			ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredients"));
		}

		String s1 = JSONUtils.getAsString(json, "result");
		int i = JSONUtils.getAsInt(json, "count");
		ItemStack itemstack = new ItemStack(Registry.ITEM.get(new ResourceLocation(s1)), i);
		return this.factory.create(id, s, ingredient, itemstack);
	}

	@Nullable
	@Override
	public T fromNetwork(@Nonnull ResourceLocation id, PacketBuffer buf) {
		String s = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		return this.factory.create(id, s, ingredient, itemstack);
	}

	@Override
	public void toNetwork(PacketBuffer buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResultItem());
	}

	public interface Creator<T extends CarpentryTableRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack itemStack);
	}
}
