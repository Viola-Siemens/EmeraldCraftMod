package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.hexagram2021.emeraldcraft.common.items.ECBoatItem;
import com.hexagram2021.emeraldcraft.common.items.EmeraldArmorItem;
import com.hexagram2021.emeraldcraft.common.items.LapisArmorItem;
import com.hexagram2021.emeraldcraft.common.util.ECFoods;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BannerPattern;
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

	public static final ItemRegObject<Item> AGATE_APPLE = ItemRegObject.register(
			"agate_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.AGATE_APPLE))
	);
	public static final ItemRegObject<Item> JADE_APPLE = ItemRegObject.register(
			"jade_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.JADE_APPLE))
	);
	public static final ItemRegObject<Item> GINKGO_NUT = ItemRegObject.register(
			"ginkgo_nut", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).food(ECFoods.GINKGO_NUT))
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
			"ginkgo_boat", () -> new ECBoatItem(ECBoat.ECBoatType.GINKGO, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
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
