package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GlassKilnRecipeSerializer<T extends GlassKilnRecipe> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final GlassKilnRecipeSerializer.Creator<T> factory;

	public GlassKilnRecipeSerializer(GlassKilnRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@SuppressWarnings("deprecation")
	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");
		CookingBookCategory category = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
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
		return this.factory.create(id, group, category, ingredient, itemstack, f, i);
	}

	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		CookingBookCategory category = buf.readEnum(CookingBookCategory.class);
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(id, group, category, ingredient, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeEnum(recipe.category());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResultItem());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
