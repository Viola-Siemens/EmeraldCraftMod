package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.crafting.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECContainerTypes {
	public static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

	public static final RegistryObject<ContainerType<CarpentryTableMenu>> CARPENTRY_TABLE_MENU = REGISTER.register(
			"carpentry", () -> new ContainerType<>(CarpentryTableMenu::new)
	);
	public static final RegistryObject<ContainerType<GlassKilnMenu>> GLASS_KILN_MENU = REGISTER.register(
			"glass_kiln", () -> new ContainerType<>(GlassKilnMenu::new)
	);
	public static final RegistryObject<ContainerType<MineralTableMenu>> MINERAL_TABLE_MENU = REGISTER.register(
			"mineral_table", () -> new ContainerType<>(MineralTableMenu::new)
	);
	public static final RegistryObject<ContainerType<ContinuousMinerMenu>> CONTINUOUS_MINER_MENU = REGISTER.register(
			"continuous_miner", () -> new ContainerType<>(ContinuousMinerMenu::new)
	);
	public static final RegistryObject<ContainerType<IceMakerMenu>> ICE_MAKER_MENU = REGISTER.register(
			"ice_maker", () -> new ContainerType<>(IceMakerMenu::new)
	);
	public static final RegistryObject<ContainerType<MelterMenu>> MELTER_MENU = REGISTER.register(
			"melter", () -> new ContainerType<>(MelterMenu::new)
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
