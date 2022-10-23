package com.hexagram2021.emeraldcraft.common.crafting.compat;

import com.hexagram2021.emeraldcraft.common.crafting.compat.create.CreateFluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.compat.example.EmeraldCraftContinuousMinerBlocks;
import com.hexagram2021.emeraldcraft.common.crafting.compat.immersive_engineering.IEFluidTypes;
import net.minecraftforge.fml.ModList;

public class ModsLoadedEventSubscriber {
	public static boolean CREATE = false;
	public static boolean IE = false;
	
	public static void compatModLoaded() {
		ModList modList = ModList.get();
		if(modList.isLoaded("create")) {
			CREATE = true;
		}
		if(modList.isLoaded( "immersiveengineering")) {
			IE = true;
		}
	}

	public static void SolveCompat() {
		if(CREATE) {
			CreateFluidTypes.init();
		}
		if(IE) {
			IEFluidTypes.init();
		}
		
		EmeraldCraftContinuousMinerBlocks.init();
	}
}
