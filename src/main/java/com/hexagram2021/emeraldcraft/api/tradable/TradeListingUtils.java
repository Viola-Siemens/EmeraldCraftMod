package com.hexagram2021.emeraldcraft.api.tradable;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import javax.annotation.Nullable;
import java.util.List;

public interface TradeListingUtils {
	List<TradeListingUtils.TradeListing> ADDITIONAL_TRADE_LISTINGS = Lists.newArrayList();

	static void registerTradeListing(Int2ObjectMap<VillagerTrades.ItemListing[]> listings, EntityType<? extends LivingEntity> entityType, @Nullable VillagerProfession profession) {
		ADDITIONAL_TRADE_LISTINGS.add(new TradeListing(listings, entityType, profession));
	}

	record TradeListing(Int2ObjectMap<VillagerTrades.ItemListing[]> listings, EntityType<? extends LivingEntity> entityType, @Nullable VillagerProfession profession) {}
}
