package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.MeatGrinderRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Optional;

public class MeatGrinderRecipeSerializer<T extends MeatGrinderRecipe> implements RecipeSerializer<T> {
    private final int defaultCookingTime;
    private final MeatGrinderRecipeSerializer.Creator<T> factory;

    public MeatGrinderRecipeSerializer(MeatGrinderRecipeSerializer.Creator<T> creator, int cookingTime) {
        this.defaultCookingTime = cookingTime;
        this.factory = creator;
    }

    @Override
    public T fromJson(ResourceLocation id, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        Ingredient ingredient;
        if (GsonHelper.isArrayNode(json, "ingredient")) {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
        }

        String resultId = GsonHelper.getAsString(json, "result");
        int count = GsonHelper.getAsInt(json, "count");
        Optional<Item> resultItem = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(resultId));
        if (resultItem.isEmpty()) {
            throw new IllegalStateException("Item: " + resultId + " does not exist");
        }
        ItemStack result = new ItemStack(resultItem.get(), count);
        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int time = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
        float bonus = GsonHelper.getAsFloat(json, "experience", 0.0F);
        return this.factory.create(id, group, ingredient, result, experience, time, bonus);
    }

    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String group = buf.readUtf();
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        ItemStack itemstack = buf.readItem();
        float xp = buf.readFloat();
        int time = buf.readVarInt();
        float bonusChance = buf.readFloat();
        return this.factory.create(id, group, ingredient, itemstack, xp, time, bonusChance);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, T recipe) {
        buf.writeUtf(recipe.getGroup());
        recipe.getIngredient().toNetwork(buf);
        buf.writeItem(recipe.getResult());
        buf.writeFloat(recipe.getExperience());
        buf.writeVarInt(recipe.getCookingTime());
        buf.writeFloat(recipe.getBonusChance());
    }

    public interface Creator<T extends MeatGrinderRecipe> {
        T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime, float bonusChance);
    }
}
