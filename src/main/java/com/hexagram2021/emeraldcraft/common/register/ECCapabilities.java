package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.items.capabilities.IFoodStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECCapabilities {
	public static final ResourceLocation FOOD_CAPABILITY_ID = new ResourceLocation(MODID, "food_storage");
	public static final Capability<IFoodStorage> FOOD_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	private ECCapabilities() {
	}
}
