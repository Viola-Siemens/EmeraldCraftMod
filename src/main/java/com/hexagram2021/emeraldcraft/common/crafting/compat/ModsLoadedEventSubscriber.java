package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.compat.create.CreateFluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.compat.example.EmeraldCraftContinuousMinerBlocks;
import net.minecraftforge.fml.ModList;

public class ModsLoadedEventSubscriber {
	public static boolean CREATE = false;
	public static void compatModLoaded() {
		ModList modList = ModList.get();
		if(modList.isLoaded("create")) {
			CREATE = true;
		}
	}

	public static void SolveCompat() {
		if(CREATE) {
			CreateFluidTypes.init();
		}

		EmeraldCraftContinuousMinerBlocks.init();
	}
}
