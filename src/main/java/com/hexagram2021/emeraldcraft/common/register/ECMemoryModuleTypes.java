package com.hexagram2021.emeraldcraft.common.register;

import com.mojang.serialization.Codec;
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
	private static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MODID);
	
	public static final RegistryObject<MemoryModuleType<UUID>> LIKED_PLAYER = REGISTER.register("liked_player", () -> new MemoryModuleType<>(Optional.of(SerializableUUID.CODEC)));

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
