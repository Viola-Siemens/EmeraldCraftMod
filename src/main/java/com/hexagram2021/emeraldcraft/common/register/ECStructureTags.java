package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructureTags {
	public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_SAR_EXPLORER_MAPS = create("on_sar_explorer_maps");
	public static final TagKey<ConfiguredStructureFeature<?, ?>> ON_GEOCENTER_EXPLORER_MAPS = create("on_geocenter_explorer_maps");

	private static TagKey<ConfiguredStructureFeature<?, ?>> create(String name) {
		return TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(MODID, name));
	}
}
