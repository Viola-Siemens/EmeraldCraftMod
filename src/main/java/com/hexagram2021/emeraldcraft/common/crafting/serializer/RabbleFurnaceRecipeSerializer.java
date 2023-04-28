package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RabbleFurnaceRecipeSerializer<T extends RabbleFurnaceRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final RabbleFurnaceRecipeSerializer.Creator<T> factory;

	public RabbleFurnaceRecipeSerializer(RabbleFurnaceRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@SuppressWarnings("deprecation")
	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String s = GsonHelper.getAsString(json, "group", "");
		JsonElement ingredientJson =
				GsonHelper.isArrayNode(json, "ingredient") ?
						GsonHelper.getAsJsonArray(json, "ingredient") :
						GsonHelper.getAsJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(ingredientJson);
		Ingredient mix1 = null, mix2 = null;
		if(json.has("mix1")) {
			JsonElement mix1Json =
					GsonHelper.isArrayNode(json, "mix1") ?
							GsonHelper.getAsJsonArray(json, "mix1") :
							GsonHelper.getAsJsonObject(json, "mix1");
			mix1 = Ingredient.fromJson(mix1Json);
			if(json.has("mix2")) {
				JsonElement mix2Json =
						GsonHelper.isArrayNode(json, "mix2") ?
								GsonHelper.getAsJsonArray(json, "mix2") :
								GsonHelper.getAsJsonObject(json, "mix2");
				mix2 = Ingredient.fromJson(mix2Json);
			}
		}

		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack itemstack;
		if (json.get("result").isJsonObject()) {
			itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
		} else {
			String s1 = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + s1 + " does not exist")
			));
		}
		float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
		int i = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
		return this.factory.create(id, s, ingredient, mix1, mix2, itemstack, f, i);
	}

	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		int ingredientSize = buf.readVarInt();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		Ingredient mix1 = null, mix2 = null;
		if(ingredientSize > 1) {
			mix1 = Ingredient.fromNetwork(buf);
			if(ingredientSize > 2) {
				mix2 = Ingredient.fromNetwork(buf);
			}
		}
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(id, group, ingredient, mix1, mix2, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeVarInt(Mth.clamp(recipe.getIngredients().size(), 1, 3));

		for(Ingredient ingredient : recipe.getIngredients()) {
			ingredient.toNetwork(buf);
		}
		buf.writeItem(recipe.getResultItem());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getRabblingTime());
	}

	public interface Creator<T extends Recipe<?>> {
		T create(ResourceLocation id, String group, Ingredient ingredient, @Nullable Ingredient mix1, @Nullable Ingredient mix2,
				 ItemStack result, float experience, int cookingTime);
	}
}
