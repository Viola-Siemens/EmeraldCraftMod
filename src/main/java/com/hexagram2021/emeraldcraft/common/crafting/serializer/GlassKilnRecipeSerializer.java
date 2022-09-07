package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GlassKilnRecipeSerializer<T extends GlassKilnRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
	private final int defaultCookingTime;
	private final GlassKilnRecipeSerializer.Creator<T> factory;

	public GlassKilnRecipeSerializer(GlassKilnRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@Override @Nonnull
	public T fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
		String s = JSONUtils.getAsString(json, "group", "");
		JsonElement jsonelement =
				JSONUtils.isArrayNode(json, "ingredient") ?
						JSONUtils.getAsJsonArray(json, "ingredient") :
						JSONUtils.getAsJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonelement);

		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack itemstack;
		if (json.get("result").isJsonObject()) {
			itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
		} else {
			String s1 = JSONUtils.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + s1 + " does not exist")
			));
		}
		float f = JSONUtils.getAsFloat(json, "experience", 0.0F);
		int i = JSONUtils.getAsInt(json, "cookingtime", this.defaultCookingTime);
		return this.factory.create(id, s, ingredient, itemstack, f, i);
	}

	@Nullable
	@Override
	public T fromNetwork(@Nonnull ResourceLocation id, PacketBuffer buf) {
		String s = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float f = buf.readFloat();
		int i = buf.readVarInt();
		return this.factory.create(id, s, ingredient, itemstack, f, i);
	}

	@Override
	public void toNetwork(PacketBuffer buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResultItem());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends AbstractCookingRecipe> {
		T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
