package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class IceMakerRecipeSerializer<T extends IceMakerRecipe> implements RecipeSerializer<T> {
	private final int defaultFreezingTime;
	private final IceMakerRecipeSerializer.Creator<T> factory;

	public IceMakerRecipeSerializer(IceMakerRecipeSerializer.Creator<T> creator, int freezingTime) {
		this.defaultFreezingTime = freezingTime;
		this.factory = creator;
	}

	@SuppressWarnings("deprecation")
	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");

		if (!json.has("ingredient")) throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find an object");
		FluidType fluidType;
		int fluidAmount;
		if (json.get("ingredient").isJsonObject()) {
			JsonObject ingredient = GsonHelper.getAsJsonObject(json, "ingredient");
			fluidType = FluidTypes.getFluidTypeFromName(GsonHelper.getAsString(ingredient, "fluid"));
			fluidAmount = GsonHelper.getAsInt(ingredient, "amount", IceMakerRecipe.DEFAULT_INPUT_AMOUNT);
		} else {
			throw new IllegalStateException("ingredient is not a Json object");
		}

		ItemStack result;
		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		if (json.get("result").isJsonObject()) {
			result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
		} else {
			String s1 = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			result = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + s1 + " does not exist")
			));
		}
		int i = GsonHelper.getAsInt(json, "freezingtime", this.defaultFreezingTime);
		return this.factory.create(id, group, fluidType, fluidAmount, result, i);
	}

	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		FluidType fluidType = FluidTypes.getFluidTypeFromName(buf.readUtf());
		int fluidAmount = buf.readVarInt();
		ItemStack result = buf.readItem();
		int time = buf.readVarInt();
		return this.factory.create(id, group, fluidType, fluidAmount, result, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeUtf(recipe.getFluidType().toString());
		buf.writeVarInt(recipe.getFluidAmount());
		buf.writeItem(recipe.getResultItem());
		buf.writeVarInt(recipe.getFreezingTime());
	}

	public interface Creator<T extends Recipe<Container>> {
		T create(ResourceLocation id, String group, FluidType inputFluid, int inputAmount, ItemStack result, int freezingTime);
	}
}
