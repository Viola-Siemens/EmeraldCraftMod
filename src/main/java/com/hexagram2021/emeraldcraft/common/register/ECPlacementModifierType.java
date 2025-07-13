package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.util.placement_modifiers.AboveHeightmapFilter;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ECPlacementModifierType {
    public static final PlacementModifierType<AboveHeightmapFilter> ABOVE_HEIGHTMAP_FILTER = register(
            "above_heightmap_filter", AboveHeightmapFilter.CODEC
    );

    @SuppressWarnings("SameParameterValue")
    private static <T extends PlacementModifier> PlacementModifierType<T> register(String name, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, EmeraldCraft.id(name), () -> codec);
    }

    public static void init() {
    }
}
