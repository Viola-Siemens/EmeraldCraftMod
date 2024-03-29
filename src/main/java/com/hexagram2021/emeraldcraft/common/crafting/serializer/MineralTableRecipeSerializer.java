package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;

public class MineralTableRecipeSerializer<T extends MineralTableRecipe> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final MineralTableRecipeSerializer.Creator<T> factory;

	public MineralTableRecipeSerializer(MineralTableRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@SuppressWarnings("deprecation")
	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");
		JsonElement jsonelement =
				GsonHelper.isArrayNode(json, "ingredient") ?
						GsonHelper.getAsJsonArray(json, "ingredient") :
						GsonHelper.getAsJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonelement);

		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack itemstack;
		if (json.get("result").isJsonObject()) {
			itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
		} else {
			String result = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(result);
			itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + result + " does not exist")
			));
		}
		float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
		int i = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
		return this.factory.create(id, group, ingredient, itemstack, f, i);
	}

	@Nullable
	@Override
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(id, group, ingredient, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResult());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
