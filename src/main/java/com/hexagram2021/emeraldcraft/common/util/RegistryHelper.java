package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface RegistryHelper {
	static <T extends ForgeRegistryEntry<T>> void register(RegistryEvent.Register<T> event, ResourceLocation id, T entry) {
		entry.setRegistryName(id);
		event.getRegistry().register(entry);
	}
}
