package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public interface RegistryHelper {
	static ResourceLocation getRegistryName(Item item) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
	}
	static ResourceLocation getRegistryName(Block block) {
		return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
	}
	static ResourceLocation getRegistryName(VillagerProfession profession) {
		return Objects.requireNonNull(ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession));
	}
	static ResourceLocation getRegistryName(Biome biome) {
		return Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome));
	}
	static ResourceLocation getRegistryName(EntityType<?> entityType) {
		return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
	}
	static ResourceLocation getRegistryName(StructureType<?> structureType) {
		return Objects.requireNonNull(BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType));
	}
}
