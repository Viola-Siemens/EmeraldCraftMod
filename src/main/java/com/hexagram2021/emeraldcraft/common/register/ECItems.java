package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
			"piglin_cutey_spawn_egg", () -> new ForgeSpawnEggItem(ECEntities.PIGLIN_CUTEY, 15852209, 15121922, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP))
	);

	public static final ItemRegObject<Item> AGATE_APPLE = ItemRegObject.register(
			"agate_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.AGATE_APPLE))
	);
	public static final ItemRegObject<Item> JADE_APPLE = ItemRegObject.register(
			"jade_apple", () -> new Item(new Item.Properties().tab(EmeraldCraft.ITEM_GROUP).rarity(Rarity.RARE).food(ECFoods.JADE_APPLE))
	);

	public static final class BannerPatterns {
		public static final ItemRegObject<BannerPatternItem> BEE = ItemRegObject.register(
				"bee_banner_pattern", () -> {
					String enumName = MODID + "_bee";
					String fullId = "ec_bee";
					BannerPattern pattern = BannerPattern.create(enumName.toUpperCase(), enumName, fullId, true);
					return new BannerPatternItem(pattern, new Item.Properties().tab(EmeraldCraft.ITEM_GROUP));
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
			return new ItemRegObject<>(RegistryObject.of(existing.getRegistryName(), existing::getRegistryType));
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
