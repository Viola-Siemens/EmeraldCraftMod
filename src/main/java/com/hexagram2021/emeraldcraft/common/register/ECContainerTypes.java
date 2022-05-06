package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.CarpentryTableMenu;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
