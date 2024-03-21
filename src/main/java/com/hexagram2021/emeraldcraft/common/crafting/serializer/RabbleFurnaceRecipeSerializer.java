package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class RabbleFurnaceRecipeSerializer<T extends RabbleFurnaceRecipe> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final RabbleFurnaceRecipeSerializer.Creator<T> factory;

	public RabbleFurnaceRecipeSerializer(RabbleFurnaceRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");
		String category = GsonHelper.getAsString(json, "group", "");
		JsonElement ingredientJson =
				GsonHelper.isArrayNode(json, "ingredient") ?
						GsonHelper.getAsJsonArray(json, "ingredient") :
						GsonHelper.getAsJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(ingredientJson);
		Ingredient mix1 = Ingredient.EMPTY, mix2 = Ingredient.EMPTY;
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
			String result = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(result);
			Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
			if(item == null) {
				throw new IllegalStateException("Item: " + result + " does not exist");
			}
			itemstack = new ItemStack(item);
		}
		float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
		int i = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
		return this.factory.create(id, group, category, ingredient, mix1, mix2, itemstack, f, i);
	}

	@Override @Nullable
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		String category = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		Ingredient mix1 = Ingredient.fromNetwork(buf);
		Ingredient mix2 = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(id, group, category, ingredient, mix1, mix2, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeUtf(recipe.category());
		recipe.ingredient().toNetwork(buf);
		recipe.mix1().toNetwork(buf);
		recipe.mix2().toNetwork(buf);
		buf.writeItem(recipe.result());
		buf.writeFloat(recipe.experience());
		buf.writeVarInt(recipe.rabblingTime());
	}

	public interface Creator<T extends Recipe<?>> {
		T create(ResourceLocation id, String group, String category, Ingredient ingredient, Ingredient mix1, Ingredient mix2,
				 ItemStack result, float experience, int cookingTime);
	}
}
