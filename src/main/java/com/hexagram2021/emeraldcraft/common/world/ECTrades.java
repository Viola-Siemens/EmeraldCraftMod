package com.hexagram2021.emeraldcraft.common.world;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class ECTrades {
	public static final int DEFAULT_SUPPLY = 12;
	public static final int COMMON_ITEMS_SUPPLY = 16;
	public static final int CURRENCY_EXCHANGE_SUPPLY = 32;
	public static final int UNCOMMON_ITEMS_SUPPLY = 6;
	public static final int ONLY_SUPPLY_ONCE = 1;

	public static final int XP_LEVEL_1_SELL = 1;
	public static final int XP_LEVEL_1_BUY = 2;
	public static final int XP_LEVEL_2_SELL = 5;
	public static final int XP_LEVEL_2_BUY = 10;
	public static final int XP_LEVEL_3_SELL = 10;
	public static final int XP_LEVEL_3_BUY = 20;
	public static final int XP_LEVEL_4_SELL = 15;
	public static final int XP_LEVEL_4_BUY = 30;
	public static final int XP_LEVEL_5_TRADE = 30;

	public static final float LOW_TIER_PRICE_MULTIPLIER = 0.05F;
	public static final float HIGH_TIER_PRICE_MULTIPLIER = 0.2F;

	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> PIGLIN_CUTEY_TRADES =  new Int2ObjectOpenHashMap<>(ImmutableMap.of(
			1, new ItemListing[] {
					new ItemsForGolds(new ItemStack(Items.FIRE_CHARGE), 3, 1, DEFAULT_SUPPLY, XP_LEVEL_1_SELL),
					new ItemsForGolds(new ItemStack(Items.BLAZE_ROD), 8, 1, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_1_SELL),
					new GoldForItems(Items.NETHER_BRICK, 6, DEFAULT_SUPPLY, XP_LEVEL_1_BUY)
			},
			2, new ItemListing[] {
					new ItemsForGolds(new ItemStack(Items.CHARCOAL), 1, 4, COMMON_ITEMS_SUPPLY, XP_LEVEL_2_SELL),
					new ItemsForGolds(new ItemStack(Items.SPECTRAL_ARROW), 1, 12, DEFAULT_SUPPLY, XP_LEVEL_2_SELL),
					new GoldForItems(Items.IRON_NUGGET, 10, DEFAULT_SUPPLY, XP_LEVEL_2_BUY)
			},
			3, new ItemListing[] {
					new ItemsForGolds(new ItemStack(Items.STRING), 1, 8, DEFAULT_SUPPLY, XP_LEVEL_3_SELL),
					new ItemsForGolds(new ItemStack(Items.SADDLE), 7, 1, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_3_SELL),
					new GoldForItems(Items.EMERALD, 1, CURRENCY_EXCHANGE_SUPPLY, XP_LEVEL_3_BUY)
			},
			4, new ItemListing[] {
					new ItemsForEmeralds(new ItemStack(ECItems.WARPED_WART.asItem()), 1, 10, DEFAULT_SUPPLY, XP_LEVEL_4_SELL),
					new EmeraldForItems(Items.ENDER_PEARL, 1, 4, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_4_SELL),
					new ItemsAndGoldsToItems(Items.ANCIENT_DEBRIS, 1, 9, Items.NETHERITE_INGOT, 1, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_4_BUY)
			},
			5, new ItemListing[] {
					new WrittenBookForEmerald(
							new TranslatableComponent("book.emeraldcraft.piglin_cutey.title"),
							new TranslatableComponent("entity.emeraldcraft.piglin_cutey"),
							new TranslatableComponent("book.emeraldcraft.piglin_cutey.content"),
							ONLY_SUPPLY_ONCE,
							XP_LEVEL_5_TRADE
					),
					new ItemsForEmeralds(new ItemStack(Items.PIGLIN_BANNER_PATTERN), 5, 1, ONLY_SUPPLY_ONCE, XP_LEVEL_5_TRADE),
					new EmeraldForItems(Items.QUARTZ, 12, 1, DEFAULT_SUPPLY, XP_LEVEL_5_TRADE)
			}
	));

	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> NETHER_PIGMAN_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
			1, new ItemListing[] {
					new ItemsForDebris(new ItemStack(Items.MUSIC_DISC_PIGSTEP), 1, 1, ONLY_SUPPLY_ONCE, XP_LEVEL_1_SELL),
					new ItemsForDebris(new ItemStack(Items.PIGLIN_BANNER_PATTERN), 1, 1, ONLY_SUPPLY_ONCE, XP_LEVEL_1_SELL),
					new DebrisForItems(Items.PORKCHOP, 47, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY),
					new DebrisForItems(Items.PORKCHOP, 47, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY),
					new DebrisForItems(Items.PORKCHOP, 47, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY)
			},
			2, new ItemListing[] {
					new ItemsForDebris(new ItemStack(Items.COOKED_PORKCHOP), 1, 19, COMMON_ITEMS_SUPPLY, XP_LEVEL_2_SELL),
					new ItemsForDebris(new ItemStack(Items.COOKED_PORKCHOP), 1, 19, COMMON_ITEMS_SUPPLY, XP_LEVEL_2_SELL),
					new DebrisForItems(Items.NETHER_WART_BLOCK, 49, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_2_BUY)
			},
			3, new ItemListing[] {
					new DebrisForTwoKindsOfItem(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, ONLY_SUPPLY_ONCE, XP_LEVEL_3_BUY),
					new GoldForItems(Items.POTATO, 4, DEFAULT_SUPPLY, XP_LEVEL_3_BUY)
			}
	));

	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> NETHER_LAMBMAN_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
			1, new ItemListing[] {
					new ItemsForDebris(new ItemStack(Items.WHITE_WOOL), 1, 31, ONLY_SUPPLY_ONCE, XP_LEVEL_1_SELL),
					new ItemsForDebris(new ItemStack(Items.BLACK_WOOL), 1, 31, ONLY_SUPPLY_ONCE, XP_LEVEL_1_SELL),
					new DebrisForItems(Items.MUTTON, 33, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY),
					new DebrisForItems(Items.MUTTON, 33, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY),
					new DebrisForItems(Items.MUTTON, 33, COMMON_ITEMS_SUPPLY, XP_LEVEL_1_BUY)
			},
			2, new ItemListing[] {
					new ItemsForDebris(new ItemStack(Items.COOKED_MUTTON), 1, 12, COMMON_ITEMS_SUPPLY, XP_LEVEL_2_SELL),
					new ItemsForDebris(new ItemStack(Items.COOKED_MUTTON), 1, 12, COMMON_ITEMS_SUPPLY, XP_LEVEL_2_SELL),
					new DebrisForItems(Items.WARPED_WART_BLOCK, 43, UNCOMMON_ITEMS_SUPPLY, XP_LEVEL_2_BUY)
			},
			3, new ItemListing[] {
					new DebrisForTwoKindsOfItem(Items.IRON_HOE, Items.GOLDEN_HOE, ONLY_SUPPLY_ONCE, XP_LEVEL_3_BUY),
					new GoldForItems(Items.GRASS_BLOCK, 2, DEFAULT_SUPPLY, XP_LEVEL_3_BUY)
			}
	));

	static class WrittenBookForEmerald implements VillagerTrades.ItemListing {
		private final Component title;
		private final Component author;
		private final Component content;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public WrittenBookForEmerald(Component title, Component author, Component content, int maxUses, int Xp) {
			this.title = title;
			this.author = author;
			this.content = content;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(Items.WRITTEN_BOOK);
			CompoundTag compoundtag = new CompoundTag();
			compoundtag.putString(WrittenBookItem.TAG_TITLE, title.getString());
			compoundtag.putString(WrittenBookItem.TAG_AUTHOR, author.getString());
			ListTag pages = new ListTag();
			pages.add(StringTag.valueOf(content.getString()));
			compoundtag.put(WrittenBookItem.TAG_PAGES, pages);
			itemstack.setTag(compoundtag);
			return new MerchantOffer(new ItemStack(Items.EMERALD), itemstack, this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int cost;
		private final int numberOfEmerald;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public EmeraldForItems(ItemLike item, int cost, int numberOfEmerald, int maxUses, int Xp) {
			this.item = item.asItem();
			this.cost = cost;
			this.numberOfEmerald = numberOfEmerald;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(this.item, this.cost);
			return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD, numberOfEmerald), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public ItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int Xp) {
			this.itemStack = itemStack;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int baseEmeraldCost;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public EnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int Xp) {
			this.itemStack = new ItemStack(item);
			this.baseEmeraldCost = baseEmeraldCost;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		public MerchantOffer getOffer(Entity trader, Random rand) {
			int i = 5 + rand.nextInt(15);
			ItemStack itemstack = EnchantmentHelper.enchantItem(rand, new ItemStack(this.itemStack.getItem()), i, false);
			int j = Math.min(this.baseEmeraldCost + i, 64);
			return new MerchantOffer(new ItemStack(Items.EMERALD, j), itemstack, this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack fromItem;
		private final int fromCount;
		private final int emeraldCost;
		private final ItemStack toItem;
		private final int toCount;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public ItemsAndEmeraldsToItems(ItemLike forItem, int fromCount, int emeraldCost, Item toItem, int toCount, int maxUses, int Xp) {
			this.fromItem = new ItemStack(forItem);
			this.fromCount = fromCount;
			this.emeraldCost = emeraldCost;
			this.toItem = new ItemStack(toItem);
			this.toCount = toCount;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class SuspisciousStewForEmerald implements VillagerTrades.ItemListing {
		final MobEffect effect;
		final int duration;
		final int maxUses;
		final int xp;
		private final float priceMultiplier;

		public SuspisciousStewForEmerald(MobEffect effect, int duration, int maxUses, int xp) {
			this.effect = effect;
			this.duration = duration;
			this.maxUses = maxUses;
			this.xp = xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
			SuspiciousStewItem.saveMobEffect(itemstack, this.effect, this.duration);
			return new MerchantOffer(new ItemStack(Items.EMERALD, 1), itemstack, this.maxUses, this.xp, this.priceMultiplier);
		}
	}

	static class EmeraldsForVillagerTypeItem implements VillagerTrades.ItemListing {
		private final Map<VillagerType, Item> trades;
		private final int cost;
		private final int emeraldCost;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public EmeraldsForVillagerTypeItem(int cost, int emeraldCost, int maxUses, int Xp, Map<VillagerType, Item> trades) {
			Registry.VILLAGER_TYPE.stream().filter((villagerType) -> !trades.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
				throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(villagerType));
			});
			this.trades = trades;
			this.cost = cost;
			this.emeraldCost = emeraldCost;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		public MerchantOffer getOffer(Entity trader, Random rand) {
			if (trader instanceof VillagerDataHolder) {
				ItemStack itemstack = new ItemStack(this.trades.get(((VillagerDataHolder)trader).getVillagerData().getType()), this.cost);
				return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD, emeraldCost), this.maxUses, this.Xp, priceMultiplier);
			} else {
				return null;
			}
		}
	}

	static class VillagerTypeItemForEmeralds implements VillagerTrades.ItemListing {
		private final Map<VillagerType, Item> trades;
		private final int numberOfItems;
		private final int emeraldCost;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public VillagerTypeItemForEmeralds(int numberOfItems, int emeraldCost, int maxUses, int Xp, Map<VillagerType, Item> trades) {
			Registry.VILLAGER_TYPE.stream().filter((villagerType) -> !trades.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
				throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(villagerType));
			});
			this.trades = trades;
			this.numberOfItems = numberOfItems;
			this.emeraldCost = emeraldCost;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		public MerchantOffer getOffer(Entity trader, Random rand) {
			if (trader instanceof VillagerDataHolder) {
				ItemStack itemstack = new ItemStack(this.trades.get(((VillagerDataHolder)trader).getVillagerData().getType()), this.numberOfItems);
				return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldCost), itemstack, this.maxUses, this.Xp, priceMultiplier);
			} else {
				return null;
			}
		}
	}

	static class GoldForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int cost;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public GoldForItems(ItemLike item, int cost, int maxUses, int Xp) {
			this.item = item.asItem();
			this.cost = cost;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}


		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(this.item, this.cost);
			return new MerchantOffer(itemstack, new ItemStack(Items.GOLD_INGOT), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsForGolds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int goldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;


		public ItemsForGolds(ItemStack itemStack, int goldCost, int numberOfItems, int maxUses, int Xp) {
			this.itemStack = itemStack;
			this.goldCost = goldCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}


		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(
					new ItemStack(Items.GOLD_INGOT, this.goldCost),
					new ItemStack(this.itemStack.getItem(), this.numberOfItems),
					this.maxUses, this.Xp, this.priceMultiplier
			);
		}
	}

	static class ItemsAndGoldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack fromItem;
		private final int fromCount;
		private final int goldCost;
		private final ItemStack toItem;
		private final int toCount;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public ItemsAndGoldsToItems(ItemLike forItem, int fromCount, int goldCost, Item toItem, int toCount, int maxUses, int Xp) {
			this.fromItem = new ItemStack(forItem);
			this.fromCount = fromCount;
			this.goldCost = goldCost;
			this.toItem = new ItemStack(toItem);
			this.toCount = toCount;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(new ItemStack(Items.GOLD_INGOT, this.goldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class DebrisForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int cost;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public DebrisForItems(ItemLike item, int cost, int maxUses, int Xp) {
			this.item = item.asItem();
			this.cost = cost;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}


		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(this.item, this.cost);
			return new MerchantOffer(itemstack, new ItemStack(Items.ANCIENT_DEBRIS), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}

	static class ItemsForDebris implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int debrisCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;


		public ItemsForDebris(ItemStack itemStack, int debrisCost, int numberOfItems, int maxUses, int Xp) {
			this.itemStack = itemStack;
			this.debrisCost = debrisCost;
			this.numberOfItems = numberOfItems;
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}


		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			return new MerchantOffer(
					new ItemStack(Items.ANCIENT_DEBRIS, this.debrisCost),
					new ItemStack(this.itemStack.getItem(), this.numberOfItems),
					this.maxUses, this.Xp, this.priceMultiplier
			);
		}
	}

	static class DebrisForTwoKindsOfItem implements VillagerTrades.ItemListing {
		private final Item item1;
		private final Item item2;
		private final int maxUses;
		private final int Xp;
		private final float priceMultiplier;

		public DebrisForTwoKindsOfItem(ItemLike item1, ItemLike item2, int maxUses, int Xp) {
			this.item1 = item1.asItem();
			this.item2 = item2.asItem();
			this.maxUses = maxUses;
			this.Xp = Xp;
			this.priceMultiplier = LOW_TIER_PRICE_MULTIPLIER;
		}


		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack1 = new ItemStack(this.item1);
			ItemStack itemstack2 = new ItemStack(this.item2);
			return new MerchantOffer(itemstack1, itemstack2, new ItemStack(Items.ANCIENT_DEBRIS), this.maxUses, this.Xp, this.priceMultiplier);
		}
	}
}
