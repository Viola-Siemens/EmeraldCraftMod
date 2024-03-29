package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.world.village.Villages;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class TradeShadowRecipeSerializer<T extends TradeShadowRecipe> implements RecipeSerializer<T> {
	private final TradeShadowRecipeSerializer.Creator<T> factory;

	public TradeShadowRecipeSerializer(TradeShadowRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
	}

	@Deprecated
	@Override
	public T fromJson(ResourceLocation id, JsonObject json) {
		JsonObject costAObject = GsonHelper.getAsJsonObject(json, "costA");
		ItemStack costA = new ItemStack(
				BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(costAObject, "item"))),
				GsonHelper.getAsInt(costAObject, "count")
		);
		JsonObject costBObject = GsonHelper.getAsJsonObject(json, "costB");
		ItemStack costB = new ItemStack(
				BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(costBObject, "item"))),
				GsonHelper.getAsInt(costBObject, "count")
		);
		JsonObject resultObject = GsonHelper.getAsJsonObject(json, "result");
		ItemStack result = new ItemStack(
				BuiltInRegistries.ITEM.get(new ResourceLocation(GsonHelper.getAsString(resultObject, "item"))),
				GsonHelper.getAsInt(resultObject, "count")
		);
		EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(GsonHelper.getAsString(json, "entityType")));
		VillagerProfession profession = BuiltInRegistries.VILLAGER_PROFESSION.get(
				new ResourceLocation(GsonHelper.getAsString(json, "profession"))
		);
		int villagerLevel = GsonHelper.getAsInt(json, "villagerLevel");
		int xp = GsonHelper.getAsInt(json, "xp");

		return this.factory.create(id, costA, costB, result, entityType, profession, villagerLevel, xp);
	}

	@SuppressWarnings("deprecation")
	@Override @Nullable
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		ItemStack costA = buf.readItem();
		ItemStack costB = buf.readItem();
		ItemStack result = buf.readItem();
		EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(buf.readResourceLocation());
		VillagerProfession profession = BuiltInRegistries.VILLAGER_PROFESSION.get(buf.readResourceLocation());
		int villagerLevel = buf.readInt();
		int xp = buf.readInt();

		return this.factory.create(id, costA, costB, result, entityType, profession, villagerLevel, xp);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeItem(recipe.getCostA());
		buf.writeItem(recipe.getCostB());
		buf.writeItem(recipe.getResult());
		buf.writeResourceLocation(getRegistryName(recipe.getEntityType()));
		VillagerProfession profession = recipe.getProfession();
		if(profession == null) {
			buf.writeResourceLocation(Villages.CARPENTER);
		} else {
			buf.writeResourceLocation(getRegistryName(profession));
		}
		buf.writeInt(recipe.getVillagerLevel());
		buf.writeInt(recipe.getXp());
	}


	public interface Creator<T extends TradeShadowRecipe> {
		T create(ResourceLocation id, ItemStack costA, ItemStack costB, ItemStack result, EntityType<?> entityType, VillagerProfession profession, int villagerLevel, int xp);
	}
}
