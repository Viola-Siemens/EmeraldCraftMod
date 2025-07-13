package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.CookedDumplingCookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CookedDumplingCookstoveRecipeSerializer<T extends CookedDumplingCookstoveRecipe> implements RecipeSerializer<T> {
    private final int defaultCookingTime;
    private final CookedDumplingCookstoveRecipeSerializer.Creator<T> factory;

    public CookedDumplingCookstoveRecipeSerializer(CookedDumplingCookstoveRecipeSerializer.Creator<T> factory, int cookingTime) {
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
            //Forge单位转为Fabric
            fluidStack.setAmount(FluidStack.convertMbToDroplets(fluidStack.getAmount()));
        } else {
            throw new IllegalStateException("fluid is not a Json object");
        }
        Ingredient container;
        if (!json.has("container")) {
            container = Ingredient.EMPTY;
        } else if (GsonHelper.isArrayNode(json, "container")) {
            container = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "container"));
        } else {
            container = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "container"));
        }
        ICookstoveDisplay display;
        if (json.get("display").isJsonObject()) {
            display = ICookstoveDisplay.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "display")).getOrThrow(false, ECLogger::error);
        } else {
            throw new IllegalStateException("display is not a Json object");
        }
        int time = GsonHelper.getAsInt(json, "cookTime", this.defaultCookingTime);
        return this.factory.create(id, fluidStack, container, display, time);
    }

    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        FluidStack fluidStack = FluidStack.readFromPacket(buf);
        Ingredient container = Ingredient.fromNetwork(buf);
        ICookstoveDisplay display = ICookstoveDisplay.fromNetwork(buf);
        int cookTime = buf.readVarInt();
        return this.factory.create(id, fluidStack, container, display, cookTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CookedDumplingCookstoveRecipe recipe) {
        recipe.fluidStack().writeToPacket(buf);
        recipe.container().toNetwork(buf);
        recipe.display().toNetwork(buf);
        buf.writeVarInt(recipe.cookTime());
    }

    public interface Creator<T extends CookedDumplingCookstoveRecipe> {
        T create(ResourceLocation id, FluidStack fluidStack, Ingredient container, ICookstoveDisplay display, int cookTime);
    }
}
