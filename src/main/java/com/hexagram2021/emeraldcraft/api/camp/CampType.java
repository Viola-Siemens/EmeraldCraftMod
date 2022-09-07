package com.hexagram2021.emeraldcraft.api.camp;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.StructureFeature;

public interface CampType {
	String toString();
	boolean isTargetBiome(ResourceLocation biome);
	StructureFeature<?, ?> getCampStructure();
}