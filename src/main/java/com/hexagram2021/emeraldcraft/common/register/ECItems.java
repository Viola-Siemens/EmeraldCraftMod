package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.compat.ModsLoadedEventSubscriber;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.items.*;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECItems {
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public static final Map<EquipmentSlot, ItemRegObject<EmeraldArmorItem>> EMERALD_ARMOR = new EnumMap<>(EquipmentSlot.class);
	public static final Map<EquipmentSlot, ItemRegObject<LapisArmorItem>> LAPIS_ARMOR = new EnumMap<>(EquipmentSlot.class);

	public static final ItemRegObject<ItemNameBlockItem> WARPED_WART = ItemRegObject.register(
			"warped_wart", () -> new ItemNameBlockItem(ECBlocks.Plant.WARPED_WART.get(), new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> PIGLIN_CUTEY_SPAWN_EGG = ItemRegObject.register(
			"piglin_cutey_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.PIGLIN_CUTEY, 0xF1E2B1, 0xE6BE02, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> NETHER_PIGMAN_SPAWN_EGG = ItemRegObject.register(
			"nether_pigman_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.NETHER_PIGMAN, 0xFF8EB3, 0x053636, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> NETHER_LAMBMAN_SPAWN_EGG = ItemRegObject.register(
			"nether_lambman_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.NETHER_LAMBMAN, 0xFFFFFF, 0x0F9B9B, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> BIGEYE_SPAWN_EGG = ItemRegObject.register(
			"bigeye_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.PURPLE_SPOTTED_BIGEYE, 0xEC1C24, 0xD8B8CC, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> HERRING_SPAWN_EGG = ItemRegObject.register(
			"herring_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.HERRING, 0x12C6EC, 0xB44420, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> WRAITH_SPAWN_EGG = ItemRegObject.register(
			"wraith_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.WRAITH, 0x400040, 0xC8C8C8, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<SpawnEggItem> MANTA_SPAWN_EGG = ItemRegObject.register(
			"manta_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.MANTA, 0xFFFFC8, 0xF8F8E0, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<Item> AGATE_APPLE = ItemRegObject.register(
			"agate_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.AGATE_APPLE))
	);
	public static final ItemRegObject<Item> JADE_APPLE = ItemRegObject.register(
			"jade_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.JADE_APPLE))
	);
	public static final ItemRegObject<Item> GINKGO_NUT = ItemRegObject.register(
			"ginkgo_nut", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.GINKGO_NUT))
	);
	public static final ItemRegObject<Item> PEACH = ItemRegObject.register(
			"peach", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PEACH))
	);
	public static final ItemRegObject<Item> GOLDEN_PEACH = ItemRegObject.register(
			"golden_peach", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.GOLDEN_PEACH))
	);

	public static final ItemRegObject<Item> POTION_COOKIE = ItemRegObject.register(
			"potion_cookie", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.POTION_COOKIE))
	);
	public static final ItemRegObject<Item> COOKED_TROPICAL_FISH = ItemRegObject.register(
			"cooked_tropical_fish", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_TROPICAL_FISH))
	);
	public static final ItemRegObject<Item> COOKED_PURPURACEUS_FUNGUS = ItemRegObject.register(
			"cooked_purpuraceus_fungus", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_PURPURACEUS_FUNGUS))
	);
	public static final ItemRegObject<Item> BOILED_EGG = ItemRegObject.register(
			"boiled_egg", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.BOILED_EGG))
	);
	public static final ItemRegObject<BowlFoodItem> CHORUS_FLOWER_EGGDROP_SOUP = ItemRegObject.register(
			"chorus_flower_eggdrop_soup", () -> new ChorusFlowerEggdropSoupItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1).food(ECFoods.CHORUS_FLOWER_EGGDROP_SOUP))
	);
	public static final ItemRegObject<Item> CARAMELIZED_POTATO = ItemRegObject.register(
			"caramelized_potato", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CARAMELIZED_POTATO))
	);
	public static final ItemRegObject<BowlFoodItem> BEEF_AND_POTATO_STEW = ItemRegObject.register(
			"beef_and_potato_stew", () -> new BowlFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1).food(ECFoods.BEEF_AND_POTATO_STEW))
	);
	public static final ItemRegObject<StickFoodItem> SAUSAGE = ItemRegObject.register(
			"sausage", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.SAUSAGE))
	);
	public static final ItemRegObject<StickFoodItem> COOKED_SAUSAGE = ItemRegObject.register(
			"cooked_sausage", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.COOKED_SAUSAGE))
	);
	public static final ItemRegObject<StickFoodItem> GLUTEN = ItemRegObject.register(
			"gluten", () -> new StickFoodItem(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16).food(ECFoods.GLUTEN))
	);
	public static final ItemRegObject<Item> HERRING = ItemRegObject.register(
			"herring", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.HERRING))
	);
	public static final ItemRegObject<Item> COOKED_HERRING = ItemRegObject.register(
			"cooked_herring", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_HERRING))
	);
	public static final ItemRegObject<Item> PURPLE_SPOTTED_BIGEYE = ItemRegObject.register(
			"purple_spotted_bigeye", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PURPLE_SPOTTED_BIGEYE))
	);
	public static final ItemRegObject<Item> COOKED_PURPLE_SPOTTED_BIGEYE = ItemRegObject.register(
			"cooked_purple_spotted_bigeye", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.COOKED_PURPLE_SPOTTED_BIGEYE))
	);
	public static final ItemRegObject<BottleFoodItem> APPLE_JUICE = ItemRegObject.register(
			"apple_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.APPLE_JUICE))
	);
	public static final ItemRegObject<BottleFoodItem> BEETROOT_JUICE = ItemRegObject.register(
			"beetroot_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.BEETROOT_JUICE))
	);
	public static final ItemRegObject<BottleFoodItem> CARROT_JUICE = ItemRegObject.register(
			"carrot_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.CARROT_JUICE)) {
				@Override
				protected void additionalEffects(Level level, LivingEntity entity) {
					if(!level.isClientSide) {
						entity.removeEffect(MobEffects.BLINDNESS);
					}
				}
			}
	);
	public static final ItemRegObject<BottleFoodItem> MELON_JUICE = ItemRegObject.register(
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
	public static final ItemRegObject<BottleFoodItem> PEACH_JUICE = ItemRegObject.register(
			"peach_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PEACH_JUICE))
	);
	public static final ItemRegObject<BottleFoodItem> PUMPKIN_JUICE = ItemRegObject.register(
			"pumpkin_juice", () -> new BottleFoodItem(20, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.PUMPKIN_JUICE)) {
				@Override
				protected void additionalEffects(Level level, LivingEntity entity) {
					if(!level.isClientSide) {
						entity.removeEffect(MobEffects.WITHER);
					}
				}
			}
	);

	public static final ItemRegObject<Item> DIAMOND_NUGGET = ItemRegObject.register(
			"diamond_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<Item> EMERALD_NUGGET = ItemRegObject.register(
			"emerald_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<Item> LAPIS_NUGGET = ItemRegObject.register(
			"lapis_nugget", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<Item> IRON_CONCENTRATE = ItemRegObject.register(
			"iron_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<Item> GOLD_CONCENTRATE = ItemRegObject.register(
			"gold_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<Item> COPPER_CONCENTRATE = ItemRegObject.register(
			"copper_concentrate", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<Item> MELTED_EMERALD_BUCKET = ItemRegObject.register(
			"melted_emerald_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemRegObject<Item> MELTED_IRON_BUCKET = ItemRegObject.register(
			"melted_iron_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemRegObject<Item> MELTED_GOLD_BUCKET = ItemRegObject.register(
			"melted_gold_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);
	public static final ItemRegObject<Item> MELTED_COPPER_BUCKET = ItemRegObject.register(
			"melted_copper_bucket", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16))
	);

	public static final ItemRegObject<Item> ROCK_BREAKER = ItemRegObject.register(
			"rock_breaker", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<ECBoatItem> GINKGO_BOAT = ItemRegObject.register(
			"ginkgo_boat", () -> new ECBoatItem(ECBoat.ECBoatType.GINKGO, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<ECBoatItem> PALM_BOAT = ItemRegObject.register(
			"palm_boat", () -> new ECBoatItem(ECBoat.ECBoatType.PALM, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<ECBoatItem> PEACH_BOAT = ItemRegObject.register(
			"peach_boat", () -> new ECBoatItem(ECBoat.ECBoatType.PEACH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<MobBucketItem> HERRING_BUCKET = ItemRegObject.register(
			"herring_bucket", () -> new MobBucketItem(ECEntities.HERRING, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);
	public static final ItemRegObject<MobBucketItem> BIGEYE_BUCKET = ItemRegObject.register(
			"bigeye_bucket", () -> new MobBucketItem(ECEntities.PURPLE_SPOTTED_BIGEYE, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final class BannerPatterns {
		public static final ItemRegObject<BannerPatternItem> BEE = ItemRegObject.register(
				"bee_banner_pattern", () -> {
					String enumName = MODID + "_bee";
					String fullId = "ec_bee";
					BannerPattern pattern = BannerPattern.create(enumName.toUpperCase(), enumName, fullId, true);
					return new BannerPatternItem(pattern, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1));
				}
		);
		public static final ItemRegObject<BannerPatternItem> SNOW = ItemRegObject.register(
				"snow_banner_pattern", () -> {
					String enumName = MODID + "_snow";
					String fullId = "ec_snow";
					BannerPattern pattern = BannerPattern.create(enumName.toUpperCase(), enumName, fullId, true);
					return new BannerPatternItem(pattern, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1));
				}
		);
		public static final ItemRegObject<BannerPatternItem> BOTTLE = ItemRegObject.register(
				"bottle_banner_pattern", () -> {
					String enumName = MODID + "_bottle";
					String fullId = "ec_bottle";
					BannerPattern pattern = BannerPattern.create(enumName.toUpperCase(), enumName, fullId, true);
					return new BannerPatternItem(pattern, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1));
				}
		);
		public static final ItemRegObject<BannerPatternItem> POTION = ItemRegObject.register(
				"potion_banner_pattern", () -> {
					String enumName = MODID + "_potion";
					String fullId = "ec_potion";
					BannerPattern pattern = BannerPattern.create(enumName.toUpperCase(), enumName, fullId, true);
					return new BannerPatternItem(pattern, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(1));
				}
		);

		private static void init() {}
	}

	public static class CreateCompatItems {
		public static final ItemRegObject<Item> ZINC_CONCENTRATE = ItemRegObject.register(
				"zinc_concentrate", () -> {
					if(ModsLoadedEventSubscriber.CREATE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_ZINC_BUCKET = ItemRegObject.register(
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
		public static final ItemRegObject<Item> ALUMINUM_CONCENTRATE = ItemRegObject.register(
				"aluminum_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_ALUMINUM_BUCKET = ItemRegObject.register(
				"melted_aluminum_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemRegObject<Item> LEAD_CONCENTRATE = ItemRegObject.register(
				"lead_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_LEAD_BUCKET = ItemRegObject.register(
				"melted_lead_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemRegObject<Item> SILVER_CONCENTRATE = ItemRegObject.register(
				"silver_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_SILVER_BUCKET = ItemRegObject.register(
				"melted_silver_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemRegObject<Item> NICKEL_CONCENTRATE = ItemRegObject.register(
				"nickel_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_NICKEL_BUCKET = ItemRegObject.register(
				"melted_nickel_bucket", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).stacksTo(16));
					}
					return new Item(new Item.Properties().stacksTo(16));
				}
		);
		public static final ItemRegObject<Item> URANIUM_CONCENTRATE = ItemRegObject.register(
				"uranium_concentrate", () -> {
					if(ModsLoadedEventSubscriber.IE) {
						return new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
					}
					return new Item(new Item.Properties());
				}
		);
		public static final ItemRegObject<Item> MELTED_URANIUM_BUCKET = ItemRegObject.register(
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
				EMERALD_ARMOR.put(slot, ItemRegObject.register(
						"emerald_" + slot.getName().toLowerCase(Locale.ENGLISH), () -> new EmeraldArmorItem(slot)
				));
				LAPIS_ARMOR.put(slot, ItemRegObject.register(
						"lapis_" + slot.getName().toLowerCase(Locale.ENGLISH), () -> new LapisArmorItem(slot)
				));
			}
		}

		BannerPatterns.init();

		CreateCompatItems.init();
		IECompatItems.init();
	}

	public static class ItemRegObject<T extends Item> implements Supplier<T>, ItemLike {
		private final RegistryObject<T> regObject;

		private static ItemRegObject<Item> simple(String name) {
			return simple(name, $ -> { }, $ -> { });
		}

		private static ItemRegObject<Item> simple(String name, Consumer<Item.Properties> makeProps, Consumer<Item> processItem) {
			return register(name, () -> Util.make(new Item(Util.make(new Item.Properties(), makeProps)), processItem));
		}

		private static <T extends Item> ItemRegObject<T> register(String name, Supplier<? extends T> make) {
			return new ItemRegObject<>(REGISTER.register(name, make));
		}

		private static <T extends Item> ItemRegObject<T> of(T existing) {
			return new ItemRegObject<>(RegistryObject.create(existing.getRegistryName(), ForgeRegistries.ITEMS));
		}

		private ItemRegObject(RegistryObject<T> regObject)
		{
			this.regObject = regObject;
		}

		@Override
		@Nonnull
		public T get()
		{
			return regObject.get();
		}

		@Nonnull
		@Override
		public Item asItem()
		{
			return regObject.get();
		}

		public ResourceLocation getId()
		{
			return regObject.getId();
		}
	}
}
