package com.hexagram2021.emeraldcraft.network;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ClientboundTradeSyncPacket implements IECPacket {
	private final List<TradeShadowRecipe> recipes;

	public ClientboundTradeSyncPacket(List<TradeShadowRecipe> recipes) {
		this.recipes = recipes;
	}

	public ClientboundTradeSyncPacket(FriendlyByteBuf buf) {
		this.recipes = buf.readCollection(Lists::newArrayListWithCapacity, friendlyByteBuf -> {
			ResourceLocation id = friendlyByteBuf.readResourceLocation();
			return ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get().fromNetwork(id, friendlyByteBuf);
		});
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeCollection(this.recipes, (friendlyByteBuf, recipe) -> {
			friendlyByteBuf.writeResourceLocation(recipe.getId());
			ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get().toNetwork(friendlyByteBuf, recipe);
		});
	}

	@Override
	public void handle() {
		if(ECCommonConfig.ENABLE_JEI_TRADING_SHADOW_RECIPE.get()) {
			TradeShadowRecipe.setTradeRecipes(this.recipes);
		}
	}
}
