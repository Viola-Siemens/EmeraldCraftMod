package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final ConfiguredStructureFeature<?, ?> SHELTER_HOUSE = ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE);
	public static final ConfiguredStructureFeature<?, ?> NETHER_WARFIELD = ECStructures.NETHER_WARFIELD.configured(
			new JigsawConfiguration(() -> NetherWarfieldPools.START, 6)
	);

	public static void init() {
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MODID, "shelter_house"), SHELTER_HOUSE);
		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MODID, "nether_warfield"), NETHER_WARFIELD);
	}
}
