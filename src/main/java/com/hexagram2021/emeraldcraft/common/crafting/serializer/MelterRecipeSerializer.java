package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MelterRecipeSerializer<T extends MelterRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
	private final int defaultMeltingTime;
	private final MelterRecipeSerializer.Creator<T> factory;

	public MelterRecipeSerializer(MelterRecipeSerializer.Creator<T> creator, int meltingTime) {
		this.defaultMeltingTime = meltingTime;
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

		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find an object");
		FluidType fluidType;
		int fluidAmount;
		if (json.get("result").isJsonObject()) {
			JsonObject result = JSONUtils.getAsJsonObject(json, "result");
			fluidType = FluidTypes.getFluidTypeFromName(JSONUtils.getAsString(result, "fluid"));
			fluidAmount = JSONUtils.getAsInt(result, "amount");
		} else {
			throw new IllegalStateException("result is not a Json object");
		}
		int i = JSONUtils.getAsInt(json, "meltingtime", this.defaultMeltingTime);
		return this.factory.create(id, s, ingredient, fluidType, fluidAmount, i);
	}

	@Nullable
	@Override
	public T fromNetwork(@Nonnull ResourceLocation id, PacketBuffer buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		FluidType fluidType = FluidTypes.getFluidTypeFromName(buf.readUtf());
		int fluidAmount = buf.readVarInt();
		int i = buf.readVarInt();
		return this.factory.create(id, group, ingredient, fluidType, fluidAmount, i);
	}

	@Override
	public void toNetwork(PacketBuffer buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeUtf(recipe.getFluidType().toString());
		buf.writeVarInt(recipe.getFluidAmount());
		buf.writeVarInt(recipe.getMeltingTime());
	}

	public interface Creator<T extends IRecipe<IInventory>> {
		T create(ResourceLocation id, String group, Ingredient ingredient, FluidType resultFluid, int resultAmount, int meltingTime);
	}
}
