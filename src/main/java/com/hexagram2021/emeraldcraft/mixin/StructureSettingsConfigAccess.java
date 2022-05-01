package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(StructureSettings.class)
public interface StructureSettingsConfigAccess {
	@Accessor
	void setStructureConfig(Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig);
}
