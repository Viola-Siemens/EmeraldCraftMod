package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.placement_modifiers.AboveHeightmapFilter;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECPlacementModifierType {
	public static final PlacementModifierType<AboveHeightmapFilter> ABOVE_HEIGHTMAP_FILTER = register(
			"above_heightmap_filter", AboveHeightmapFilter.CODEC
	);
	
	@SuppressWarnings("SameParameterValue")
	private static <P extends PlacementModifier> PlacementModifierType<P> register(String name, Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(MODID, name), () -> codec);
	}

	public static void init() {
	}
}
