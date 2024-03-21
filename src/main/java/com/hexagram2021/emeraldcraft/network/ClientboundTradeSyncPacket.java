package com.hexagram2021.emeraldcraft.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class ClientboundTradeSyncPacket implements IECPacket {
	private final Map<VillagerProfession, List<Block>> jobsites;
	private final List<TradeShadowRecipe> recipes;

	public ClientboundTradeSyncPacket(Map<VillagerProfession, List<Block>> jobsites, List<TradeShadowRecipe> recipes) {
		this.jobsites = jobsites;
		this.recipes = recipes;
	}

	public ClientboundTradeSyncPacket(FriendlyByteBuf buf) {
		this.jobsites = Maps.newIdentityHashMap();
		int professionSize = buf.readVarInt();
		for(int i = 0; i < professionSize; ++i) {
			VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(buf.readResourceLocation());
			List<Block> blocks = buf.readCollection(Lists::newArrayListWithCapacity, friendlyByteBuf -> {
				ResourceLocation id = friendlyByteBuf.readResourceLocation();
				return ForgeRegistries.BLOCKS.getValue(id);
			});
			this.jobsites.put(profession, blocks);
		}
		this.recipes = buf.readCollection(Lists::newArrayListWithCapacity, friendlyByteBuf -> ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get().fromNetwork(friendlyByteBuf));
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(this.jobsites.size());
		this.jobsites.forEach((profession, blocks) -> {
			buf.writeResourceLocation(getRegistryName(profession));
			buf.writeCollection(blocks, (friendlyByteBuf, block) -> friendlyByteBuf.writeResourceLocation(getRegistryName(block)));
		});
		buf.writeCollection(this.recipes, (friendlyByteBuf, recipe) -> ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get().toNetwork(friendlyByteBuf, recipe));
	}

	@Override
	public void handle() {
		if(ECCommonConfig.ENABLE_JEI_TRADING_SHADOW_RECIPE.get()) {
			TradeShadowRecipe.setCachedJobsites(this.jobsites);
			TradeShadowRecipe.setTradeRecipes(this.recipes);
		}
	}
}
