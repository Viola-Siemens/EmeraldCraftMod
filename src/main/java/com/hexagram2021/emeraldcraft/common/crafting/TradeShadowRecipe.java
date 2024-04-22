package com.hexagram2021.emeraldcraft.common.crafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hexagram2021.emeraldcraft.api.tradable.ITradableDataFactory;
import com.hexagram2021.emeraldcraft.api.tradable.TradeListingUtils;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.TradeUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record TradeShadowRecipe(ItemStack costA, ItemStack costB, ItemStack result, EntityType<?> entityType,
								@Nullable VillagerProfession profession, int villagerLevel, int xp) implements Recipe<Container> {
	private static final Map<VillagerProfession, Int2ObjectMap<Villager>> LAZY_RENDER_VILLAGERS = Maps.newHashMap();
	private static final Map<EntityType<?>, Int2ObjectMap<LivingEntity>> LAZY_RENDER_TRADERS = Maps.newHashMap();

	@Nullable
	private static Map<VillagerProfession, List<Block>> cachedJobsites = null;
	@Nullable
	private static List<TradeShadowRecipe> cachedList = null;

	public static Map<VillagerProfession, List<Block>> getAllJobsites() {
		if (cachedJobsites != null) {
			return cachedJobsites;
		}

		Map<VillagerProfession, List<Block>> shadows = Maps.newIdentityHashMap();
		if (ECCommonConfig.ENABLE_JEI_TRADING_SHADOW_RECIPE.get()) {
			ForgeRegistries.VILLAGER_PROFESSIONS.forEach(villagerProfession -> {
				Set<Block> blocks = Sets.newHashSet();
				ForgeRegistries.POI_TYPES.forEach(poiType -> {
					if(villagerProfession.heldJobSite().test(ForgeRegistries.POI_TYPES.getDelegateOrThrow(poiType))) {
						poiType.matchingStates().forEach(blockState -> blocks.add(blockState.getBlock()));
					}
				});
				shadows.putIfAbsent(villagerProfession, blocks.stream().toList());
			});
		}
		return cachedJobsites = shadows;
	}

	public static List<TradeShadowRecipe> getTradeRecipes(Level world) {
		if (cachedList != null) {
			return cachedList;
		}

		List<TradeShadowRecipe> shadows = Lists.newArrayList();
		if (ECCommonConfig.ENABLE_JEI_TRADING_SHADOW_RECIPE.get()) {
			Set<String> names = Sets.newHashSet();

			VillagerTrades.TRADES.forEach((profession, trades) ->
					TradeUtil.addTradeShadowRecipesFromListingMap(trades, EntityType.VILLAGER, profession, world, names, shadows));
			TradeListingUtils.ADDITIONAL_TRADE_LISTINGS.forEach(tradeListing ->
					TradeUtil.addTradeShadowRecipesFromListingMap(tradeListing.listings(), tradeListing.entityType(), tradeListing.profession(), world, names, shadows));
		}

		return cachedList = shadows;
	}

	public static List<Block> getJobsitesFromProfession(VillagerProfession profession) {
		if(cachedJobsites == null) {
			return List.of();
		}
		return cachedJobsites.get(profession);
	}

	public static void setTradeRecipes(List<TradeShadowRecipe> shadows) {
		cachedList = shadows;
	}
	public static void setCachedJobsites(Map<VillagerProfession, List<Block>> shadows) {
		cachedJobsites = shadows;
	}

	@Override
	public boolean matches(Container container, Level level) {
		return ItemStack.isSameItem(this.costA, container.getItem(0)) && ItemStack.isSameItem(this.costB, container.getItem(1));
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return false;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}


	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ECRecipes.TRADE_SHADOW_TYPE.get();
	}


	@OnlyIn(Dist.CLIENT)
	public LivingEntity getRenderVillager() {
		assert Minecraft.getInstance().level != null;
		if (this.entityType != EntityType.VILLAGER || this.profession == null) {
			return LAZY_RENDER_TRADERS.computeIfAbsent(this.entityType, entityType1 -> new Int2ObjectOpenHashMap<>())
					.computeIfAbsent(this.villagerLevel, villagerLevel1 -> {
						Entity trader = this.entityType.create(Minecraft.getInstance().level);
						assert trader != null;

						ITradableDataFactory.setTradableMobData(trader, this.profession, this.villagerLevel);
						return (LivingEntity) trader;
					});
		}
		return LAZY_RENDER_VILLAGERS.computeIfAbsent(this.profession, profession1 -> new Int2ObjectOpenHashMap<>())
				.computeIfAbsent(this.villagerLevel, villagerLevel1 -> {
					Villager villager = EntityType.VILLAGER.create(Minecraft.getInstance().level);
					assert villager != null;
					villager.setNoAi(true);
					villager.setVillagerData(new VillagerData(VillagerType.PLAINS, this.profession, this.villagerLevel));
					return villager;
				});
	}
}
