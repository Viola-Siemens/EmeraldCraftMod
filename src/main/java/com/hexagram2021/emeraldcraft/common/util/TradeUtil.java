package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class TradeUtil {
	private static void addTradeShadowRecipe(VillagerTrades.ItemListing list, LivingEntity trader, @Nullable VillagerProfession profession, int level,
											Set<String> names, Map<ResourceLocation, Recipe<?>> shadows) {
		MerchantOffer offer;
		if(list instanceof VillagerTrades.TreasureMapForEmeralds || list instanceof ECTrades.NetherStructureMapForEmeralds) {
			offer = new MerchantOffer(new ItemStack(Items.EMERALD, 13), new ItemStack(Items.COMPASS), new ItemStack(Items.FILLED_MAP), 12, 5, 0.2F);
		} else {
			offer = list.getOffer(trader, trader.getRandom());
		}
		if(offer != null) {
			ItemStack costAItem = offer.getBaseCostA();
			ItemStack costBItem = offer.getCostB();
			ItemStack resultItem = offer.getResult();
			costAItem.setTag(null);
			costBItem.setTag(null);
			resultItem.setTag(null);
			String costA = Objects.requireNonNull(costAItem.getItem().getRegistryName()).toString()
					.replace(':', '_') + "_" + costAItem.getCount();
			String costB = Objects.requireNonNull(costBItem.getItem().getRegistryName()).toString()
					.replace(':', '_') + "_" + costBItem.getCount();
			String result = Objects.requireNonNull(resultItem.getItem().getRegistryName()).toString()
					.replace(':', '_') + "_" + resultItem.getCount();

			String name;
			name = Objects.requireNonNull(Objects.requireNonNullElseGet(profession, trader::getType).getRegistryName())
					.toString().replace(':', '_') + "_" + costA + "_" + costB + "_" + result;
			if(names.contains(name)) {
				ECLogger.error("Duplicated trade entry: " + name);
			} else {
				names.add(name);
				ResourceLocation id = new ResourceLocation(MODID, "trade_shadow/" + name);
				shadows.put(id, new TradeShadowRecipe(id, costAItem, costBItem, resultItem, trader.getType(), profession, level, offer.getXp()));
			}
		}
	}

	public static <T extends LivingEntity> void addTradeShadowRecipesFromListingMap(Int2ObjectMap<VillagerTrades.ItemListing[]> listingMap, EntityType<T> entityType, @Nullable VillagerProfession profession,
																					ServerLevel world, Set<String> names, Map<ResourceLocation, Recipe<?>> shadows) {
		listingMap.forEach((level, lists) -> {
			T entity = entityType.create(world);
			if(entity != null) {
				for(VillagerTrades.ItemListing list: lists) {
					addTradeShadowRecipe(list, entity, profession, level, names, shadows);
				}
				entity.discard();
			}
		});
	}
}
