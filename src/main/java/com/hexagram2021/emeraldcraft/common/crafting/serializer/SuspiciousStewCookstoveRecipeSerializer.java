package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.SuspiciousStewCookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fluids.FluidStack;

public class SuspiciousStewCookstoveRecipeSerializer<T extends SuspiciousStewCookstoveRecipe> implements RecipeSerializer<T> {
	private final int defaultCookingTime;
	private final SuspiciousStewCookstoveRecipeSerializer.Creator<T> factory;

	public SuspiciousStewCookstoveRecipeSerializer(SuspiciousStewCookstoveRecipeSerializer.Creator<T> factory, int cookingTime) {
		this.defaultCookingTime = cookingTime;
		this.factory = factory;
	}

	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		FluidStack fluidStack;
		if (!json.has("fluid")) {
			fluidStack = FluidStack.EMPTY;
		} else if (json.get("fluid").isJsonObject()) {
			fluidStack = FluidStack.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "fluid")).getOrThrow(false, ECLogger::error);
		} else {
			throw new IllegalStateException("fluid is not a Json object");
		}
		Ingredient container;
		if(!json.has("container")) {
			container = Ingredient.EMPTY;
		} else if (GsonHelper.isArrayNode(json, "container")) {
			container = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "container"));
		} else {
			container = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "container"));
		}
		ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
		ICookstoveDisplay display;
		if (json.get("display").isJsonObject()) {
			display = ICookstoveDisplay.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "display")).getOrThrow(false, ECLogger::error);
		} else {
			throw new IllegalStateException("display is not a Json object");
		}
		int time = GsonHelper.getAsInt(json, "cookTime", this.defaultCookingTime);
		return this.factory.create(id, fluidStack, container, result, display, time);
	}

	@Override
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		FluidStack fluidStack = FluidStack.readFromPacket(buf);
		Ingredient container = Ingredient.fromNetwork(buf);
		ItemStack result = buf.readItem();
		ICookstoveDisplay display = ICookstoveDisplay.fromNetwork(buf);
		int cookTime = buf.readVarInt();
		return this.factory.create(id, fluidStack, container, result, display, cookTime);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, SuspiciousStewCookstoveRecipe recipe) {
		recipe.fluidStack().writeToPacket(buf);
		recipe.container().toNetwork(buf);
		buf.writeItem(recipe.result());
		recipe.display().toNetwork(buf);
		buf.writeVarInt(recipe.cookTime());
	}

	public interface Creator<T extends SuspiciousStewCookstoveRecipe> {
		T create(ResourceLocation id, FluidStack fluidStack, Ingredient container, ItemStack result, ICookstoveDisplay display, int cookTime);
	}
}
