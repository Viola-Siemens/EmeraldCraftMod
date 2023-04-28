package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.menu.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECContainerTypes {
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

	public static final RegistryObject<MenuType<CarpentryTableMenu>> CARPENTRY_TABLE_MENU = REGISTER.register(
			"carpentry", () -> new MenuType<>(CarpentryTableMenu::new)
	);
	public static final RegistryObject<MenuType<GlassKilnMenu>> GLASS_KILN_MENU = REGISTER.register(
			"glass_kiln", () -> new MenuType<>(GlassKilnMenu::new)
	);
	public static final RegistryObject<MenuType<MineralTableMenu>> MINERAL_TABLE_MENU = REGISTER.register(
			"mineral_table", () -> new MenuType<>(MineralTableMenu::new)
	);
	public static final RegistryObject<MenuType<ContinuousMinerMenu>> CONTINUOUS_MINER_MENU = REGISTER.register(
			"continuous_miner", () -> new MenuType<>(ContinuousMinerMenu::new)
	);
	public static final RegistryObject<MenuType<IceMakerMenu>> ICE_MAKER_MENU = REGISTER.register(
			"ice_maker", () -> new MenuType<>(IceMakerMenu::new)
	);
	public static final RegistryObject<MenuType<MelterMenu>> MELTER_MENU = REGISTER.register(
			"melter", () -> new MenuType<>(MelterMenu::new)
	);
	public static final RegistryObject<MenuType<RabbleFurnaceMenu>> RABBLE_FURNACE_MENU = REGISTER.register(
			"rabble_furnace", () -> new MenuType<>(RabbleFurnaceMenu::new)
	);
	public static final RegistryObject<MenuType<PiglinCuteyMerchantMenu>> PIGLIN_CUTEY_MERCHANT_MENU = REGISTER.register(
			"piglin_cutey_merchant", () -> new MenuType<>(PiglinCuteyMerchantMenu::new)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
