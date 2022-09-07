package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IceMakerRecipeSerializer<T extends IceMakerRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
	private final int defaultFreezingTime;
	private final IceMakerRecipeSerializer.Creator<T> factory;

	public IceMakerRecipeSerializer(IceMakerRecipeSerializer.Creator<T> creator, int freezingTime) {
		this.defaultFreezingTime = freezingTime;
		this.factory = creator;
	}

	@Override @Nonnull
	public T fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
		String group = JSONUtils.getAsString(json, "group", "");

		if (!json.has("ingredient")) throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find an object");
		FluidType fluidType;
		int fluidAmount;
		if (json.get("ingredient").isJsonObject()) {
			JsonObject ingredient = JSONUtils.getAsJsonObject(json, "ingredient");
			fluidType = FluidTypes.getFluidTypeFromName(JSONUtils.getAsString(ingredient, "fluid"));
			fluidAmount = JSONUtils.getAsInt(ingredient, "amount", IceMakerRecipe.DEFAULT_INPUT_AMOUNT);
		} else {
			throw new IllegalStateException("ingredient is not a Json object");
		}

		ItemStack result;
		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		if (json.get("result").isJsonObject()) {
			result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
		} else {
			String s1 = JSONUtils.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(s1);
			result = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(
					() -> new IllegalStateException("Item: " + s1 + " does not exist")
			));
		}
		int i = JSONUtils.getAsInt(json, "freezingtime", this.defaultFreezingTime);
		return this.factory.create(id, group, fluidType, fluidAmount, result, i);
	}

	@Nullable
	@Override
	public T fromNetwork(@Nonnull ResourceLocation id, PacketBuffer buf) {
		String group = buf.readUtf();
		FluidType fluidType = FluidTypes.getFluidTypeFromName(buf.readUtf());
		int fluidAmount = buf.readVarInt();
		ItemStack result = buf.readItem();
		int i = buf.readVarInt();
		return this.factory.create(id, group, fluidType, fluidAmount, result, i);
	}

	@Override
	public void toNetwork(PacketBuffer buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeUtf(recipe.getFluidType().toString());
		buf.writeVarInt(recipe.getFluidAmount());
		buf.writeItem(recipe.getResultItem());
		buf.writeVarInt(recipe.getFreezingTime());
	}

	public interface Creator<T extends IRecipe<IInventory>> {
		T create(ResourceLocation id, String group, FluidType inputFluid, int inputAmount, ItemStack result, int freezingTime);
	}
}
