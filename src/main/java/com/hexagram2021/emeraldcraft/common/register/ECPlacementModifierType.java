package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.placement_modifiers.AboveHeightmapFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPlacementModifierType {
	public static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, MODID);

	public static final RegistryObject<PlacementModifierType<AboveHeightmapFilter>> ABOVE_HEIGHTMAP_FILTER = REGISTER.register(
			"above_heightmap_filter", () -> () -> AboveHeightmapFilter.CODEC
	);

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
