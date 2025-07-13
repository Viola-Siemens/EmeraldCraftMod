package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.crafting.menu.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ECContainerTypes {
    public static final MenuType<CarpentryTableMenu> CARPENTRY_TABLE_MENU = register(
            "carpentry", new MenuType<>(CarpentryTableMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<GlassKilnMenu> GLASS_KILN_MENU = register(
            "glass_kiln", new MenuType<>(GlassKilnMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<MineralTableMenu> MINERAL_TABLE_MENU = register(
            "mineral_table", new MenuType<>(MineralTableMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<ContinuousMinerMenu> CONTINUOUS_MINER_MENU = register(
            "continuous_miner", new MenuType<>(ContinuousMinerMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<IceMakerMenu> ICE_MAKER_MENU = register(
            "ice_maker", new MenuType<>(IceMakerMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<MelterMenu> MELTER_MENU = register(
            "melter", new MenuType<>(MelterMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<RabbleFurnaceMenu> RABBLE_FURNACE_MENU = register(
            "rabble_furnace", new MenuType<>(RabbleFurnaceMenu::new, FeatureFlags.VANILLA_SET)
    );
    public static final MenuType<PiglinCuteyMerchantMenu> PIGLIN_CUTEY_MERCHANT_MENU = register(
            "piglin_cutey_merchant", new MenuType<>(PiglinCuteyMerchantMenu::new, FeatureFlags.VANILLA_SET)
    );

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType<T> menuType) {
        return Registry.register(BuiltInRegistries.MENU, EmeraldCraft.id(name), menuType);
    }

    public static void init() {
    }
}
