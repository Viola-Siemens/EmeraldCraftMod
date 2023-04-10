package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructureTags {
	public static final TagKey<Structure> ON_SAR_EXPLORER_MAPS = create("on_sar_explorer_maps");
	public static final TagKey<Structure> ON_GEOCENTER_EXPLORER_MAPS = create("on_geocenter_explorer_maps");

	private static TagKey<Structure> create(String name) {
		return TagKey.create(Registries.STRUCTURE, new ResourceLocation(MODID, name));
	}
}
