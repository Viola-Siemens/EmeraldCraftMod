package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.compat.example.EmeraldCraftContinuousMinerBlocks;
import com.hexagram2021.emeraldcraft.common.world.compat.TerraBlenderUtils;
import net.minecraftforge.fml.ModList;

public class ModsLoadedEventSubscriber {
	public static boolean CREATE = false;
	public static boolean IE = false;
	public static boolean TERRABLENDER = false;

	public static void compatModLoaded() {
		ModList modList = ModList.get();
		if(modList.isLoaded("create")) {
			CREATE = true;
		}
		if(modList.isLoaded( "immersiveengineering")) {
			IE = true;
		}
		if(modList.isLoaded("terrablender")) {
			TERRABLENDER = true;
		}
	}

	public static void solveCompat() {
		EmeraldCraftContinuousMinerBlocks.init();
	}
	
	public static void solveTerraBlender() {
		if(TERRABLENDER) {
			TerraBlenderUtils.init();
		}
	}
}
