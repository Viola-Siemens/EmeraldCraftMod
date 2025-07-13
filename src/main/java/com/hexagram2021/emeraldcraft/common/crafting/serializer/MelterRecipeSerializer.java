package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MelterRecipeSerializer<T extends MelterRecipe> implements RecipeSerializer<T> {
    private final int defaultMeltingTime;
    private final MelterRecipeSerializer.Creator<T> factory;

    public MelterRecipeSerializer(MelterRecipeSerializer.Creator<T> creator, int meltingTime) {
        this.defaultMeltingTime = meltingTime;
        this.factory = creator;
    }

    @Override
    public T fromJson(ResourceLocation id, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        JsonElement jsonelement =
                GsonHelper.isArrayNode(json, "ingredient") ?
                        GsonHelper.getAsJsonArray(json, "ingredient") :
                        GsonHelper.getAsJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(jsonelement);

        if (!json.has("result"))
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find an object");
        FluidStack fluidStack;
        if (json.get("result").isJsonObject()) {
            fluidStack = FluidStack.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(json, "result")).getOrThrow(false, ECLogger::error);
            //Forge单位转为Fabric
            fluidStack.setAmount(FluidStack.convertMbToDroplets(fluidStack.getAmount()));
        } else {
            throw new IllegalStateException("result is not a Json object");
        }
        int time = GsonHelper.getAsInt(json, "meltingtime", this.defaultMeltingTime);
        return this.factory.create(id, group, ingredient, fluidStack, time);
    }

    @Override
    public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String group = buf.readUtf();
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        FluidStack result = FluidStack.readFromPacket(buf);
        int time = buf.readVarInt();
        return this.factory.create(id, group, ingredient, result, time);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, T recipe) {
        buf.writeUtf(recipe.getGroup());
        recipe.getIngredient().toNetwork(buf);
        recipe.resultFluid().writeToPacket(buf);
        buf.writeVarInt(recipe.meltingTime());
    }

    public interface Creator<T extends Recipe<Container>> {
        T create(ResourceLocation id, String group, Ingredient ingredient, FluidStack resultFluid, int meltingTime);
    }
}
