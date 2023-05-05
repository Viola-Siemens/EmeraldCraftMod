package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class TradeShadowRecipeSerializer<T extends TradeShadowRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
	private static final ResourceLocation NIL_PROFESSION = new ResourceLocation("no_profession");

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
		EntityType<?> entityType = Registry.ENTITY_TYPE.get(new ResourceLocation(GsonHelper.getAsString(json, "entityType")));
		VillagerProfession profession = Registry.VILLAGER_PROFESSION.get(new ResourceLocation(GsonHelper.getAsString(json, "profession")));
		int villagerLevel = GsonHelper.getAsInt(json, "villagerLevel");
		int xp = GsonHelper.getAsInt(json, "xp");

		return this.factory.create(id, costA, costB, result, entityType, profession, villagerLevel, xp);
	}

	@SuppressWarnings("deprecation")
	@Override @Nullable
	public T fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
		ItemStack costA = buf.readItem();
		ItemStack costB = buf.readItem();
		ItemStack result = buf.readItem();
		EntityType<?> entityType = Registry.ENTITY_TYPE.get(buf.readResourceLocation());
		ResourceLocation professionId = buf.readResourceLocation();
		VillagerProfession profession;
		if(professionId.equals(NIL_PROFESSION)) {
			profession = null;
		} else {
			profession = Registry.VILLAGER_PROFESSION.get(professionId);
		}
		int villagerLevel = buf.readInt();
		int xp = buf.readInt();

		return this.factory.create(id, costA, costB, result, entityType, profession, villagerLevel, xp);
	}

	@Override
	public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull T recipe) {
		buf.writeItem(recipe.getCostA());
		buf.writeItem(recipe.getCostB());
		buf.writeItem(recipe.getResultItem());
		buf.writeResourceLocation(Objects.requireNonNull(recipe.getEntityType().getRegistryName()));
		if(recipe.getProfession() == null) {
			buf.writeResourceLocation(NIL_PROFESSION);
		} else {
			buf.writeResourceLocation(Objects.requireNonNull(recipe.getProfession().getRegistryName()));
		}
		buf.writeInt(recipe.getVillagerLevel());
		buf.writeInt(recipe.getXp());
	}


	public interface Creator<T extends TradeShadowRecipe> {
		T create(ResourceLocation id, ItemStack costA, ItemStack costB, ItemStack result, @NotNull EntityType<?> entityType, @Nullable VillagerProfession profession, int villagerLevel, int xp);
	}
}
