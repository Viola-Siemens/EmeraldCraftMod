package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.Objects;

public interface RegistryHelper {
    static ResourceLocation getRegistryName(Item item) {
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }

    static ResourceLocation getRegistryName(Block block) {
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block));
    }

    static ResourceLocation getRegistryName(Fluid fluid) {
        return Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid));
    }

    static ResourceLocation getRegistryName(VillagerProfession profession) {
        return Objects.requireNonNull(BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession));
    }

    static ResourceLocation getRegistryName(MinecraftServer server, Biome biome) {
        return Objects.requireNonNull(server.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome));
    }

    static ResourceLocation getRegistryName(EntityType<?> entityType) {
        return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    static ResourceLocation getRegistryName(StructureType<?> structureType) {
        return Objects.requireNonNull(BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType));
    }

    static <T> T getRegistryEntry(Registry<T> registry, ResourceLocation registryName) {
        return Objects.requireNonNull(registry.get(registryName));
    }

    @Contract("_,_,!null->!null;_,_,null->_")
    @Nullable
    static <T> T getRegistryEntry(Registry<T> registry, ResourceLocation registryName, @Nullable T e) {
        T ret = registry.get(registryName);
        return ret == null ? e : ret;
    }
}
