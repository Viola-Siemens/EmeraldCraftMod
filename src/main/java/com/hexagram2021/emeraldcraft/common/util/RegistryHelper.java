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

public interface RegistryHelper {
	static ResourceLocation getRegistryName(Item item) {
		return ForgeRegistries.ITEMS.getKey(item);
	}
	static ResourceLocation getRegistryName(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block);
	}
	static ResourceLocation getRegistryName(VillagerProfession profession) {
		return ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession);
	}
	static ResourceLocation getRegistryName(Biome biome) {
		return ForgeRegistries.BIOMES.getKey(biome);
	}
	static ResourceLocation getRegistryName(EntityType<?> entityType) {
		return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
	}
	static ResourceLocation getRegistryName(StructureType<?> structureType) {
		return BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType);
	}
}
