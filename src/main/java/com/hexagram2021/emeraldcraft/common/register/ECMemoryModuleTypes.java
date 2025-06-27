package com.hexagram2021.emeraldcraft.common.register;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECMemoryModuleTypes {
	public static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, MODID);

	public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> NEAREST_DARK_LOCATION = register("nearest_dark_location");
	public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> DARK_LOCATION_COOLDOWN_TICKS = register("dark_location_cooldown_ticks", Codec.INT);

	@SuppressWarnings("SameParameterValue")
	private static <U> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<U>> register(String name) {
		return REGISTER.register(name, () -> new MemoryModuleType<>(Optional.empty()));
	}

	@SuppressWarnings("SameParameterValue")
	private static <U> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<U>> register(String name, Codec<U> codec) {
		return REGISTER.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
