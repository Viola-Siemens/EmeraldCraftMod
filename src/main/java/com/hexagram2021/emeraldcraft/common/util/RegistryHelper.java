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

public interface RegistryHelper {
	@Nullable
	static ResourceLocation getRegistryName(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}
	@Nullable
	static ResourceLocation getRegistryName(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block);
	}
	@Nullable
	static ResourceLocation getRegistryName(VillagerProfession profession) {
		return ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession);
	}
	@Nullable
	static ResourceLocation getRegistryName(Biome biome) {
		return ForgeRegistries.BIOMES.getKey(biome);
	}
	@Nullable
	static ResourceLocation getRegistryName(EntityType<?> entityType) {
		return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
	}
	@Nullable
	static ResourceLocation getRegistryName(StructureType<?> structureType) {
		return BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType);
	}
}
