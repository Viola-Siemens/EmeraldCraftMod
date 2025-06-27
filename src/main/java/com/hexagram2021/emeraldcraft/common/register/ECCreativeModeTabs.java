package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECCreativeModeTabs {
	private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_BLOCKS = register(
			"building_blocks",
			Component.translatable("itemGroup.emeraldcraft.building_blocks"),
			() -> new ItemStack(ECBlocks.TO_STAIRS.get(new ResourceLocation("emerald_block"))),
			(flags, output) -> ECItems.ItemEntry.BUILDING_BLOCKS.forEach(output::accept)
	);
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> FUNCTIONAL_BLOCKS_AND_MATERIALS = register(
			"functional_blocks_and_materials",
			Component.translatable("itemGroup.emeraldcraft.functional_blocks_and_materials"),
			() -> new ItemStack(ECBlocks.WorkStation.CARPENTRY_TABLE),
			(flags, output) -> ECItems.ItemEntry.FUNCTIONAL_BLOCKS_AND_MATERIALS.forEach(output::accept)
	);
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_AND_ARMORS = register(
			"tools_and_armors",
			Component.translatable("itemGroup.emeraldcraft.tools_and_armors"),
			() -> new ItemStack(ECItems.EMERALD_ARMOR.get(ArmorItem.Type.CHESTPLATE)),
			(flags, output) -> ECItems.ItemEntry.TOOLS_AND_ARMORS.forEach(output::accept)
	);
	public static DeferredHolder<CreativeModeTab, CreativeModeTab> FOODS_AND_DRINKS = register(
			"foods_and_drinks",
			Component.translatable("itemGroup.emeraldcraft.foods_and_drinks"),
			() -> new ItemStack(ECItems.ROUGAMO),
			(flags, output) -> ECItems.ItemEntry.FOODS_AND_DRINKS.forEach(output::accept)
	);

	private static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String name, Component title, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator generator) {
		return REGISTER.register(name, () -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
				.title(title).icon(icon).displayItems(generator).build());
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
