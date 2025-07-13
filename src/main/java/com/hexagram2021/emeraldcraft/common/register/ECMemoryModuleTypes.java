package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class ECMemoryModuleTypes {
    public static final MemoryModuleType<BlockPos> NEAREST_DARK_LOCATION = register("nearest_dark_location");
    public static final MemoryModuleType<Integer> DARK_LOCATION_COOLDOWN_TICKS = register("dark_location_cooldown_ticks", Codec.INT);

    @SuppressWarnings("SameParameterValue")
    private static <U> MemoryModuleType<U> register(String name) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, EmeraldCraft.id(name), new MemoryModuleType<>(Optional.empty()));
    }

    @SuppressWarnings("SameParameterValue")
    private static <U> MemoryModuleType<U> register(String name, Codec<U> codec) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, EmeraldCraft.id(name), new MemoryModuleType<>(Optional.of(codec)));
    }

    public static void init() {
    }
}
