package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECEntityTypeTags {
	public static final TagKey<EntityType<?>> VILLAGERS = create("villagers");
	public static final TagKey<EntityType<?>> MAMMALS = create("mammals");

	private static TagKey<EntityType<?>> create(String name) {
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MODID, name));
	}
}
