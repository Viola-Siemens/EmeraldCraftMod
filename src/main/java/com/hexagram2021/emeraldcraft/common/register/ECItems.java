package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.items.ECBoatItem;
import com.hexagram2021.emeraldcraft.common.items.armors.*;
import com.hexagram2021.emeraldcraft.common.items.foods.*;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings("unused")
public class ECItems {
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public static final Map<EquipmentSlot, ItemEntry<EmeraldArmorItem>> EMERALD_ARMOR = new EnumMap<>(EquipmentSlot.class);
	public static final Map<EquipmentSlot, ItemEntry<LapisArmorItem>> LAPIS_ARMOR = new EnumMap<>(EquipmentSlot.class);
	public static final Map<EquipmentSlot, ItemEntry<WoodenArmorItem>> WOODEN_ARMOR = new EnumMap<>(EquipmentSlot.class);

	public static final ItemEntry<ItemNameBlockItem> WARPED_WART = ItemEntry.register(
			"warped_wart", () -> new ItemNameBlockItem(ECBlocks.Plant.WARPED_WART.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> CHILI = ItemEntry.register(
			"chili", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CHILI)) {
				@Override @NotNull
				public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
					if(entity instanceof Player player) {
						player.getCooldowns().addCooldown(this, 120);
					}
					return super.finishUsingItem(itemStack, level, entity);
				}
			}
	);
	public static final ItemEntry<ItemNameBlockItem> CHILI_SEED = ItemEntry.register(
			"chili_seed", () -> new ItemNameBlockItem(ECBlocks.Plant.CHILI.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> CABBAGE = ItemEntry.register(
			"cabbage", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CABBAGE))
	);
	public static final ItemEntry<SpawnEggItem> PIGLIN_CUTEY_SPAWN_EGG = ItemEntry.register(
			"piglin_cutey_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.PIGLIN_CUTEY, 0xF1E2B1, 0xE6BE02, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> NETHER_PIGMAN_SPAWN_EGG = ItemEntry.register(
			"nether_pigman_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.NETHER_PIGMAN, 0xFF8EB3, 0x053636, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> NETHER_LAMBMAN_SPAWN_EGG = ItemEntry.register(
			"nether_lambman_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.NETHER_LAMBMAN, 0xFFFFFF, 0x0F9B9B, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> BIGEYE_SPAWN_EGG = ItemEntry.register(
			"bigeye_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.PURPLE_SPOTTED_BIGEYE, 0xEC1C24, 0xD8B8CC, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> HERRING_SPAWN_EGG = ItemEntry.register(
			"herring_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.HERRING, 0x12C6EC, 0xB44420, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> SNAKEHEAD_SPAWN_EGG = ItemEntry.register(
			"snakehead_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.SNAKEHEAD, 0x413830, 0x646464, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> WRAITH_SPAWN_EGG = ItemEntry.register(
			"wraith_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.WRAITH, 0x400040, 0xC8C8C8, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> MANTA_SPAWN_EGG = ItemEntry.register(
			"manta_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.MANTA, 0xFFFFC8, 0xF8F8E0, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<SpawnEggItem> LUMINE_SPAWN_EGG = ItemEntry.register(
			"lumine_spawn_egg", () -> new ForgeSpawnEggItem(() -> ECEntities.LUMINE, 0xF7FF55, 0xF9FFAA, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<Item> AGATE_APPLE = ItemEntry.register(
			"agate_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.AGATE_APPLE))
	);
	public static final ItemEntry<Item> JADE_APPLE = ItemEntry.register(
			"jade_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.JADE_APPLE))
	);
	public static final ItemEntry<Item> GINKGO_NUT = ItemEntry.register(
			"ginkgo_nut", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.GINKGO_NUT))
	);
	public static final ItemEntry<Item> PEACH = ItemEntry.register(
			"peach", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PEACH))
	);
	public static final ItemEntry<Item> GOLDEN_PEACH = ItemEntry.register(
			"golden_peach", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.GOLDEN_PEACH))
	);

	public static final ItemEntry<Item> POTION_COOKIE = ItemEntry.register(
			"potion_cookie", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.POTION_COOKIE))
	);
	public static final ItemEntry<Item> COOKED_TROPICAL_FISH = ItemEntry.register(
			"cooked_tropical_fish", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_TROPICAL_FISH))
	);
	public static final ItemEntry<Item> COOKED_PURPURACEUS_FUNGUS = ItemEntry.register(
			"cooked_purpuraceus_fungus", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_PURPURACEUS_FUNGUS))
	);
	public static final ItemEntry<Item> BOILED_EGG = ItemEntry.register(
			"boiled_egg", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.BOILED_EGG))
	);
	public static final ItemEntry<BowlFoodItem> CHORUS_FLOWER_EGGDROP_SOUP = ItemEntry.register(
			"chorus_flower_eggdrop_soup", () -> new ChorusFlowerEggdropSoupItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.CHORUS_FLOWER_EGGDROP_SOUP))
	);
	public static final ItemEntry<Item> CARAMELIZED_POTATO = ItemEntry.register(
			"caramelized_potato", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CARAMELIZED_POTATO))
	);
	public static final ItemEntry<Item> ROUGAMO = ItemEntry.register(
			"rougamo", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.ROUGAMO))
	);
	public static final ItemEntry<BowlFoodItem> BEEF_AND_POTATO_STEW = ItemEntry.register(
			"beef_and_potato_stew", () -> new BowlFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.BEEF_AND_POTATO_STEW))
	);
	public static final ItemEntry<BowlFoodItem> BRAISED_CHICKEN = ItemEntry.register(
			"braised_chicken", () -> new BowlFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.BRAISED_CHICKEN))
	);
	public static final ItemEntry<BowlFoodItem> SAUERKRAUT_FISH = ItemEntry.register(
			"sauerkraut_fish", () -> new BowlFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.SAUERKRAUT_FISH))
	);
	public static final ItemEntry<StickFoodItem> SAUSAGE = ItemEntry.register(
			"sausage", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.SAUSAGE))
	);
	public static final ItemEntry<StickFoodItem> COOKED_SAUSAGE = ItemEntry.register(
			"cooked_sausage", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.COOKED_SAUSAGE))
	);
	public static final ItemEntry<StickFoodItem> GLUTEN = ItemEntry.register(
			"gluten", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.GLUTEN))
	);
	public static final ItemEntry<Item> WARDEN_HEART = ItemEntry.register(
			"warden_heart", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.EPIC).food(ECFoods.WARDEN_HEART))
	);
	public static final ItemEntry<BowlFoodItem> STIR_FRIED_WARDEN_HEART = ItemEntry.register(
			"stir_fried_warden_heart", () -> new BowlFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).rarity(Rarity.EPIC).food(ECFoods.STIR_FRIED_WARDEN_HEART))
	);
	public static final ItemEntry<Item> HERRING = ItemEntry.register(
			"herring", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.HERRING))
	);
	public static final ItemEntry<Item> COOKED_HERRING = ItemEntry.register(
			"cooked_herring", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_HERRING))
	);
	public static final ItemEntry<Item> PURPLE_SPOTTED_BIGEYE = ItemEntry.register(
			"purple_spotted_bigeye", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PURPLE_SPOTTED_BIGEYE))
	);
	public static final ItemEntry<Item> COOKED_PURPLE_SPOTTED_BIGEYE = ItemEntry.register(
			"cooked_purple_spotted_bigeye", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_PURPLE_SPOTTED_BIGEYE))
	);
	public static final ItemEntry<Item> SNAKEHEAD = ItemEntry.register(
			"snakehead", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.SNAKEHEAD))
	);
	public static final ItemEntry<Item> COOKED_SNAKEHEAD = ItemEntry.register(
			"cooked_snakehead", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_SNAKEHEAD))
	);
	public static final ItemEntry<BottleFoodItem> APPLE_JUICE = ItemEntry.register(
			"apple_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.APPLE_JUICE))
	);
	public static final ItemEntry<BottleFoodItem> BEETROOT_JUICE = ItemEntry.register(
			"beetroot_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.BEETROOT_JUICE))
	);
	public static final ItemEntry<BottleFoodItem> CARROT_JUICE = ItemEntry.register(
			"carrot_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CARROT_JUICE)) {
				@Override
				protected void additionalEffects(Level level, LivingEntity entity) {
					if(!level.isClientSide) {
						entity.removeEffect(MobEffects.BLINDNESS);
						entity.removeEffect(MobEffects.DARKNESS);
					}
				}
			}
	);
	public static final ItemEntry<BottleFoodItem> MELON_JUICE = ItemEntry.register(
			"melon_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.MELON_JUICE)) {
				@Override
				protected void additionalEffects(Level level, LivingEntity entity) {
					if(!level.isClientSide) {
						if(entity.isOnFire()) {
							entity.clearFire();
						}
					}
				}
			}
	);
	public static final ItemEntry<BottleFoodItem> PEACH_JUICE = ItemEntry.register(
			"peach_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PEACH_JUICE))
	);
	public static final ItemEntry<BottleFoodItem> PUMPKIN_JUICE = ItemEntry.register(
			"pumpkin_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PUMPKIN_JUICE)) {
				@Override
				protected void additionalEffects(Level level, LivingEntity entity) {
					if(!level.isClientSide) {
						entity.removeEffect(MobEffects.WITHER);
					}
				}
			}
	);

	public static final ItemEntry<Item> RESIN_SHARD = ItemEntry.register(
			"resin_shard", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> RESIN_BOTTLE = ItemEntry.register(
			"resin_bottle", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> RESIN_BUCKET = ItemEntry.register(
			"resin_bucket", () -> new Item(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(16).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> WINDOW_FILM = ItemEntry.register(
			"window_film", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> THICK_WINDOW_FILM = ItemEntry.register(
			"thick_window_film", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<Item> DIAMOND_NUGGET = ItemEntry.register(
			"diamond_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> EMERALD_NUGGET = ItemEntry.register(
			"emerald_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> LAPIS_NUGGET = ItemEntry.register(
			"lapis_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<Item> IRON_CONCENTRATE = ItemEntry.register(
			"iron_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> GOLD_CONCENTRATE = ItemEntry.register(
			"gold_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<Item> COPPER_CONCENTRATE = ItemEntry.register(
			"copper_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<Item> MELTED_EMERALD_BUCKET = ItemEntry.register(
			"melted_emerald_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemEntry<Item> MELTED_IRON_BUCKET = ItemEntry.register(
			"melted_iron_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemEntry<Item> MELTED_GOLD_BUCKET = ItemEntry.register(
			"melted_gold_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemEntry<Item> MELTED_COPPER_BUCKET = ItemEntry.register(
			"melted_copper_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);

	public static final ItemEntry<Item> ROCK_BREAKER = ItemEntry.register(
			"rock_breaker", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<ECBoatItem> GINKGO_BOAT = ItemEntry.register(
			"ginkgo_boat", () -> new ECBoatItem(false, ECBoat.ECBoatType.GINKGO, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<ECBoatItem> PALM_BOAT = ItemEntry.register(
			"palm_boat", () -> new ECBoatItem(false, ECBoat.ECBoatType.PALM, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<ECBoatItem> PEACH_BOAT = ItemEntry.register(
			"peach_boat", () -> new ECBoatItem(false, ECBoat.ECBoatType.PEACH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<ECBoatItem> GINKGO_CHEST_BOAT = ItemEntry.register(
			"ginkgo_chest_boat", () -> new ECBoatItem(true, ECBoat.ECBoatType.GINKGO, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<ECBoatItem> PALM_CHEST_BOAT = ItemEntry.register(
			"palm_chest_boat", () -> new ECBoatItem(true, ECBoat.ECBoatType.PALM, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<ECBoatItem> PEACH_CHEST_BOAT = ItemEntry.register(
			"peach_chest_boat", () -> new ECBoatItem(true, ECBoat.ECBoatType.PEACH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemEntry<MobBucketItem> HERRING_BUCKET = ItemEntry.register(
			"herring_bucket", () -> new MobBucketItem(() -> ECEntities.HERRING, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<MobBucketItem> BIGEYE_BUCKET = ItemEntry.register(
			"bigeye_bucket", () -> new MobBucketItem(() -> ECEntities.PURPLE_SPOTTED_BIGEYE, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemEntry<MobBucketItem> SNAKEHEAD_BUCKET = ItemEntry.register(
			"snakehead_bucket", () -> new MobBucketItem(() -> ECEntities.SNAKEHEAD, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);

	public static class CreateCompatItems {
		public static final ItemEntry<Item> ZINC_CONCENTRATE = ItemEntry.register(
				"zinc_concentrate", () -> {
					if(ModsLoadedEventSubscriber.CREATE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_ZINC_BUCKET = ItemEntry.register(
				"melted_zinc_bucket", () -> {
					if(ModsLoadedEventSubscriber.CREATE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);

		private static void init() {}
	}

	public static class IECompatItems {
		public static final ItemEntry<Item> ALUMINUM_CONCENTRATE = ItemEntry.register(
				"aluminum_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_ALUMINUM_BUCKET = ItemEntry.register(
				"melted_aluminum_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemEntry<Item> LEAD_CONCENTRATE = ItemEntry.register(
				"lead_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_LEAD_BUCKET = ItemEntry.register(
				"melted_lead_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemEntry<Item> SILVER_CONCENTRATE = ItemEntry.register(
				"silver_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_SILVER_BUCKET = ItemEntry.register(
				"melted_silver_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemEntry<Item> NICKEL_CONCENTRATE = ItemEntry.register(
				"nickel_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_NICKEL_BUCKET = ItemEntry.register(
				"melted_nickel_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemEntry<Item> URANIUM_CONCENTRATE = ItemEntry.register(
				"uranium_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemEntry<Item> MELTED_URANIUM_BUCKET = ItemEntry.register(
				"melted_uranium_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);

		private static void init() {}
	}

	private ECItems() { }

	public static void init(IEventBus bus) {
		REGISTER.register(bus);

		for(EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				EMERALD_ARMOR.put(slot, ItemEntry.register(
						"emerald_" + slot.getName().toLowerCase(Locale.ENGLISH), () -> new EmeraldArmorItem(slot)
				));
				LAPIS_ARMOR.put(slot, ItemEntry.register(
						"lapis_" + slot.getName().toLowerCase(Locale.ENGLISH), () -> new LapisArmorItem(slot)
				));
				WOODEN_ARMOR.put(slot, ItemEntry.register(
						"wooden_" + slot.getName().toLowerCase(Locale.ENGLISH), () -> new WoodenArmorItem(slot)
				));
			}
		}

		CreateCompatItems.init();
		IECompatItems.init();
	}

	public static class ItemEntry<T extends Item> implements Supplier<T>, ItemLike {
		private final RegistryObject<T> regObject;

		private static ItemEntry<Item> simple(String name) {
			return simple(name, $ -> { }, $ -> { });
		}

		private static ItemEntry<Item> simple(String name, Consumer<Item.Properties> makeProps, Consumer<Item> processItem) {
			return register(name, () -> Util.make(new Item(Util.make(new Item.Properties(), makeProps)), processItem));
		}

		static <T extends Item> ItemEntry<T> register(String name, Supplier<? extends T> make) {
			return new ItemEntry<>(REGISTER.register(name, make));
		}

		private static <T extends Item> ItemEntry<T> of(T existing) {
			return new ItemEntry<>(RegistryObject.create(getRegistryName(existing), ForgeRegistries.ITEMS));
		}

		private ItemEntry(RegistryObject<T> regObject)
		{
			this.regObject = regObject;
		}

		@Override @NotNull
		public T get() {
			return regObject.get();
		}

		@Override @NotNull
		public Item asItem() {
			return regObject.get();
		}

		public ResourceLocation getId() {
			return regObject.getId();
		}
	}
}
