package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class TradeShadowRecipeSerializer<T extends TradeShadowRecipe> implements RecipeSerializer<T> {
	private final TradeShadowRecipeSerializer.Creator<T> factory;

	public TradeShadowRecipeSerializer(TradeShadowRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
	}

	@Deprecated
	@Override @NotNull
	public T fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
		JsonObject costAObject = GsonHelper.getAsJsonObject(json, "costA");
		ItemStack costA = new ItemStack(Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(costAObject, "item"))), GsonHelper.getAsInt(costAObject, "count"));
		JsonObject costBObject = GsonHelper.getAsJsonObject(json, "costB");
		ItemStack costB = new ItemStack(Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(costBObject, "item"))), GsonHelper.getAsInt(costBObject, "count"));
		JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
		ItemStack result = new ItemStack(Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(resultObject, "item"))), GsonHelper.getAsInt(resultObject, "count"));
		VillagerProfession profession = Registry.VILLAGER_PROFESSION.get(new ResourceLocation(GsonHelper.getAsString(json, "profession")));
		int villagerLevel = GsonHelper.getAsInt(json, "villagerLevel");
		int xp = GsonHelper.getAsInt(json, "xp");

		return this.factory.create(id, costA, costB, result, profession, villagerLevel, xp);
	}

	@SuppressWarnings("deprecation")
	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
		ItemStack costA = buf.readItem();
		ItemStack costB = buf.readItem();
		ItemStack result = buf.readItem();
		VillagerProfession profession = Registry.VILLAGER_PROFESSION.get(buf.readResourceLocation());
		int villagerLevel = buf.readInt();
		int xp = buf.readInt();

		return this.factory.create(id, costA, costB, result, profession, villagerLevel, xp);
	}

	@Override
	public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull T recipe) {
		buf.writeItem(recipe.getCostA());
		buf.writeItem(recipe.getCostB());
		buf.writeItem(recipe.getResultItem());
		buf.writeResourceLocation(getRegistryName(recipe.getProfession()));
		buf.writeInt(recipe.getVillagerLevel());
		buf.writeInt(recipe.getXp());
	}


	public interface Creator<T extends TradeShadowRecipe> {
		T create(ResourceLocation id, ItemStack costA, ItemStack costB, ItemStack result, VillagerProfession profession, int villagerLevel, int xp);
	}
}
