package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import com.hexagram2021.emeraldcraft.common.world.pools.SwampVillagePools;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final ConfiguredStructureFeature<?, ?> SHELTER = register(
			new ResourceLocation(MODID, "shelter"),
			ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> NETHER_WARFIELD = register(
			new ResourceLocation(MODID, "nether_warfield"),
			ECStructures.NETHER_WARFIELD.configured(new JigsawConfiguration(() -> NetherWarfieldPools.START, 6))
	);
	public static final ConfiguredStructureFeature<?, ?> ENTRENCHMENT = register(
			new ResourceLocation(MODID, "entrenchment"),
			ECStructures.ENTRENCHMENT.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> VILLAGE_SWAMP = register(
			new ResourceLocation(MODID, "village_swamp"),
			StructureFeature.VILLAGE.configured(new JigsawConfiguration(() -> SwampVillagePools.START, 6))
	);
	public static final ConfiguredStructureFeature<?, ?> BADLANDS_CAMP = register(
			new ResourceLocation(MODID, "badlands_camp"),
			ECStructures.BADLANDS_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> BIRCH_CAMP = register(
			new ResourceLocation(MODID, "birch_camp"),
			ECStructures.BIRCH_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> DESERT_CAMP = register(
			new ResourceLocation(MODID, "desert_camp"),
			ECStructures.DESERT_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> JUNGLE_CAMP = register(
			new ResourceLocation(MODID, "jungle_camp"),
			ECStructures.JUNGLE_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> PLAINS_CAMP = register(
			new ResourceLocation(MODID, "plains_camp"),
			ECStructures.PLAINS_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> SAVANNA_CAMP = register(
			new ResourceLocation(MODID, "savanna_camp"),
			ECStructures.SAVANNA_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> SNOW_CAMP = register(
			new ResourceLocation(MODID, "snow_camp"),
			ECStructures.SNOW_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> STONY_CAMP = register(
			new ResourceLocation(MODID, "stony_camp"),
			ECStructures.STONY_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> SWAMP_CAMP = register(
			new ResourceLocation(MODID, "swamp_camp"),
			ECStructures.SWAMP_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);
	public static final ConfiguredStructureFeature<?, ?> TAIGA_CAMP = register(
			new ResourceLocation(MODID, "taiga_camp"),
			ECStructures.TAIGA_CAMP.configured(NoneFeatureConfiguration.INSTANCE)
	);

	private static ConfiguredStructureFeature<?, ?> register(ResourceLocation id, ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, configuredStructureFeature);
	}

	public static void init() {}
}
