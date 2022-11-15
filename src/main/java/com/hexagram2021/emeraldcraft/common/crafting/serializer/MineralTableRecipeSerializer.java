package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class MineralTableRecipeSerializer<T extends MineralTableRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final MineralTableRecipeSerializer.Creator<T> factory;

	public MineralTableRecipeSerializer(MineralTableRecipeSerializer.Creator<T> creator, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = creator;
	}

	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String s = GsonHelper.getAsString(json, "group", "");
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
			String s1 = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + s1 + " does not exist")
			));
		}
		float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
		int i = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
		return this.factory.create(id, s, ingredient, itemstack, f, i);
	}

	@Nullable
	@Override
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String s = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float f = buf.readFloat();
		int i = buf.readVarInt();
		return this.factory.create(id, s, ingredient, itemstack, f, i);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
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
