package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class IceMakerRecipeSerializer<T extends IceMakerRecipe> implements RecipeSerializer<T> {
	private final int defaultFreezingTime;
	private final IceMakerRecipeSerializer.Creator<T> factory;

	public IceMakerRecipeSerializer(IceMakerRecipeSerializer.Creator<T> creator, int freezingTime) {
		this.defaultFreezingTime = freezingTime;
		this.factory = creator;
	}

	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		String group = GsonHelper.getAsString(json, "group", "");

		if (!json.has("ingredient")) throw new com.google.gson.JsonSyntaxException("Missing ingredient, expected to find an object");
		FluidStack fluidStack;
		if (json.get("ingredient").isJsonObject()) {
			fluidStack = FluidStack.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "ingredient")).getOrThrow(false, ECLogger::error);
		} else {
			throw new IllegalStateException("ingredient is not a Json object");
		}

		ItemStack itemStack;
		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		if (json.get("result").isJsonObject()) {
			itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
		} else {
			String result = GsonHelper.getAsString(json, "result");
			ResourceLocation resourcelocation = new ResourceLocation(result);
			Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
			if(item == null) {
				throw new IllegalStateException("Item: " + result + " does not exist");
			}
			itemStack = new ItemStack(item);
		}
		int time = GsonHelper.getAsInt(json, "freezingtime", this.defaultFreezingTime);
		return this.factory.create(id, group, fluidStack, itemStack, time);
	}

	@Override @Nullable
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String group = buf.readUtf();
		FluidStack inputFluid = FluidStack.readFromPacket(buf);
		ItemStack result = buf.readItem();
		int time = buf.readVarInt();
		return this.factory.create(id, group, inputFluid, result, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.group());
		recipe.inputFluid().writeToPacket(buf);
		buf.writeItem(recipe.result());
		buf.writeVarInt(recipe.freezingTime());
	}

	public interface Creator<T extends Recipe<Container>> {
		T create(ResourceLocation id, String group, FluidStack inputFluid, ItemStack result, int freezingTime);
	}
}
