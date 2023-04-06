package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MelterRecipeSerializer<T extends MelterRecipe> implements RecipeSerializer<T> {
	private final int defaultMeltingTime;
	private final MelterRecipeSerializer.Creator<T> factory;

	public MelterRecipeSerializer(MelterRecipeSerializer.Creator<T> creator, int meltingTime) {
		this.defaultMeltingTime = meltingTime;
		this.factory = creator;
	}

	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");
		JsonElement jsonelement =
				GsonHelper.isArrayNode(json, "ingredient") ?
						GsonHelper.getAsJsonArray(json, "ingredient") :
						GsonHelper.getAsJsonObject(json, "ingredient");
		Ingredient ingredient = Ingredient.fromJson(jsonelement);

		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find an object");
		FluidType fluidType;
		int fluidAmount;
		if (json.get("result").isJsonObject()) {
			JsonObject result = GsonHelper.getAsJsonObject(json, "result");
			fluidType = FluidTypes.getFluidTypeFromName(GsonHelper.getAsString(result, "fluid"));
			fluidAmount = GsonHelper.getAsInt(result, "amount");
		} else {
			throw new IllegalStateException("result is not a Json object");
		}
		int time = GsonHelper.getAsInt(json, "meltingtime", this.defaultMeltingTime);
		return this.factory.create(id, group, ingredient, fluidType, fluidAmount, time);
	}

	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		FluidType fluidType = FluidTypes.getFluidTypeFromName(buf.readUtf());
		int fluidAmount = buf.readVarInt();
		int time = buf.readVarInt();
		return this.factory.create(id, group, ingredient, fluidType, fluidAmount, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeUtf(recipe.getFluidType().toString());
		buf.writeVarInt(recipe.getFluidAmount());
		buf.writeVarInt(recipe.getMeltingTime());
	}

	public interface Creator<T extends Recipe<Container>> {
		T create(ResourceLocation id, String group, Ingredient ingredient, FluidType resultFluid, int resultAmount, int meltingTime);
	}
}
