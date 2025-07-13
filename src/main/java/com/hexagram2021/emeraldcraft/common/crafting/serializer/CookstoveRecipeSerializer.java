package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class CookstoveRecipeSerializer<T extends CookstoveRecipe> implements RecipeSerializer<T> {
    private final int defaultCookingTime;
    private final CookstoveRecipeSerializer.Creator<T> factory;

    public CookstoveRecipeSerializer(CookstoveRecipeSerializer.Creator<T> factory, int cookingTime) {
        this.defaultCookingTime = cookingTime;
        this.factory = factory;
    }

    @Override
    public T fromJson(ResourceLocation id, JsonObject json) {
        if (!json.has("ingredients")) {
            throw new com.google.gson.JsonSyntaxException("Missing ingredients, expected to find an array");
        }
        if (!json.get("ingredients").isJsonArray()) {
            throw new IllegalStateException("ingredients is not a Json object");
        }
        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsArray.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredientsArray.size(); ++i) {
            ingredients.set(i, Ingredient.fromJson(ingredientsArray.get(i), false));
        }
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
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        ICookstoveDisplay display;
        if (json.get("display").isJsonObject()) {
            display = ICookstoveDisplay.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "display")).getOrThrow(false, ECLogger::error);
        } else {
            throw new IllegalStateException("display is not a Json object");
        }
        int time = GsonHelper.getAsInt(json, "cookTime", this.defaultCookingTime);
        return this.factory.create(id, ingredients, fluidStack, container, result, display, time);
    }

    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
        ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));

        FluidStack fluidStack = FluidStack.readFromPacket(buf);
        Ingredient container = Ingredient.fromNetwork(buf);
        ItemStack result = buf.readItem();
        ICookstoveDisplay display = ICookstoveDisplay.fromNetwork(buf);
        int cookTime = buf.readVarInt();
        return this.factory.create(id, ingredients, fluidStack, container, result, display, cookTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CookstoveRecipe recipe) {
        buf.writeVarInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buf);
        }

        recipe.fluidStack().writeToPacket(buf);
        recipe.container().toNetwork(buf);
        buf.writeItem(recipe.result());
        recipe.display().toNetwork(buf);
        buf.writeVarInt(recipe.cookTime());
    }

    public interface Creator<T extends CookstoveRecipe> {
        T create(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack fluidStack, Ingredient container, ItemStack result, ICookstoveDisplay display, int cookTime);
    }
}
