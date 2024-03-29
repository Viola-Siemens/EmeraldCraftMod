package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("unused")
public class ECStructureSetKeys {
	public static final ResourceKey<StructureSet> SHELTER = createKey("shelter");
	public static final ResourceKey<StructureSet> NETHER_WARFIELD = createKey("nether_warfield");
	public static final ResourceKey<StructureSet> ENTRENCHMENT = createKey("entrenchment");
	public static final ResourceKey<StructureSet> HOLLOW_TREE = createKey("hollow_tree");
	public static final ResourceKey<StructureSet> CAMP = createKey("camp");

	private static ResourceKey<StructureSet> createKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(MODID, name));
	}

	public static void init() {

	}
}
