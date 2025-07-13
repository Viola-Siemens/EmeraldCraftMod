package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.compat.example.EmeraldCraftContinuousMinerBlocks;
import com.hexagram2021.emeraldcraft.common.world.compat.TerraBlenderUtils;
import net.fabricmc.loader.api.FabricLoader;

public class ModsLoadedEventSubscriber {
    public static boolean CREATE = false;
    public static boolean IE = false;
    public static boolean TERRABLENDER = false;

    public static void compatModLoaded() {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("create")) {
            CREATE = true;
        }
        if (loader.isModLoaded("immersiveengineering")) {
            IE = true;
        }
        if (loader.isModLoaded("terrablender")) {
            TERRABLENDER = true;
        }
    }

    public static void solveCompat() {
        EmeraldCraftContinuousMinerBlocks.init();
    }

    public static void solveTerraBlender() {
        if (TERRABLENDER) {
            TerraBlenderUtils.init();
        }
    }
}
