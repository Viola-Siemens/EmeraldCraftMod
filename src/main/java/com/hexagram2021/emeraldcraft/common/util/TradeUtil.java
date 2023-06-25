package com.hexagram2021.emeraldcraft.common.util;

import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.hexagram2021.emeraldcraft.common.world.village.ECTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class TradeUtil {
	private static void addTradeShadowRecipe(VillagerTrades.ItemListing list, LivingEntity trader, @Nullable VillagerProfession profession, int level,
											Set<String> names, List<TradeShadowRecipe> shadows) {
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
			String costA = getRegistryName(costAItem.getItem()).toString().replace(':', '_') + "_" + costAItem.getCount();
			String costB = getRegistryName(costBItem.getItem()).toString().replace(':', '_') + "_" + costBItem.getCount();
			String result = getRegistryName(resultItem.getItem()).toString().replace(':', '_') + "_" + resultItem.getCount();

			String name;
			if(profession == null) {
				name = getRegistryName(trader.getType()).toString().replace(':', '_') +
						"_" + costA + "_" + costB + "_" + result;
			} else {
				name = getRegistryName(profession).toString().replace(':', '_') +
						"_" + costA + "_" + costB + "_" + result;
			}
			if(names.contains(name)) {
				ECLogger.warn("Duplicated trade entry: " + name);
			} else {
				names.add(name);
				ResourceLocation id = new ResourceLocation(MODID, "trade_shadow/" + name);
				shadows.add(new TradeShadowRecipe(id, costAItem, costBItem, resultItem, trader.getType(), profession, level, offer.getXp()));
			}
		}
	}

	public static <T extends LivingEntity> void addTradeShadowRecipesFromListingMap(Int2ObjectMap<VillagerTrades.ItemListing[]> listingMap, EntityType<T> entityType, @Nullable VillagerProfession profession,
																					Level world, Set<String> names, List<TradeShadowRecipe> shadows) {
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
