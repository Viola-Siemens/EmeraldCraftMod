package com.hexagram2021.emeraldcraft.common.register;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.UUID;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECMemoryModuleTypes {
	public static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MODID);

	public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_DARK_LOCATION = register("nearest_dark_location");
	public static final RegistryObject<MemoryModuleType<Integer>> DARK_LOCATION_COOLDOWN_TICKS = register("dark_location_cooldown_ticks", Codec.INT);
	public static final RegistryObject<MemoryModuleType<Integer>> ITEM_PICKUP_COOLDOWN_TICKS = register("item_pickup_cooldown_ticks", Codec.INT);
	public static final RegistryObject<MemoryModuleType<UUID>> LIKED_PLAYER = REGISTER.register("liked_player", () -> new MemoryModuleType<>(Optional.of(SerializableUUID.CODEC)));

	@SuppressWarnings("SameParameterValue")
	private static <U> RegistryObject<MemoryModuleType<U>> register(String name) {
		return REGISTER.register(name, () -> new MemoryModuleType<>(Optional.empty()));
	}

	@SuppressWarnings("SameParameterValue")
	private static <U> RegistryObject<MemoryModuleType<U>> register(String name, Codec<U> codec) {
		return REGISTER.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
	}

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
