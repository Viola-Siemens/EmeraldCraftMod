package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final ConfiguredStructureFeature<?, ?> SHELTER_HOUSE = ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE);

	public static void init() {
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MODID, "shelter_house"), SHELTER_HOUSE);
	}
}
