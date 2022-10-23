package com.hexagram2021.emeraldcraft.common.register;

import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.UUIDCodec;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.UUID;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECMemoryModuleTypes {
	private static final DeferredRegister<MemoryModuleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MODID);
	
	public static final RegistryObject<MemoryModuleType<UUID>> LIKED_PLAYER = REGISTER.register("liked_player", () -> new MemoryModuleType<>(Optional.of(UUIDCodec.CODEC)));
	public static final RegistryObject<MemoryModuleType<Boolean>> IS_PANICKING = REGISTER.register("is_panicking", () -> new MemoryModuleType<>(Optional.of(Codec.BOOL)));

	public static void init(IEventBus bus) {
		REGISTER.register(bus);
	}
}
