package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccess {
	@Accessor
	@Mutable
	static void setNOISE_AFFECTING_FEATURES(List<StructureFeature<?>> NOISE_AFFECTING_FEATURES) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}
}
