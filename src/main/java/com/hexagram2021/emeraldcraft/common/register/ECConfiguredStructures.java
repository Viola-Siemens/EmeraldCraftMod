package com.hexagram2021.emeraldcraft.common.register;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import com.hexagram2021.emeraldcraft.common.world.pools.SwampVillagePools;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final Holder<ConfiguredStructureFeature<?, ?>> SHELTER = register(
			new ResourceLocation(MODID, "shelter"),
			ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_SHELTER, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> NETHER_WARFIELD = register(
			new ResourceLocation(MODID, "nether_warfield"),
			ECStructures.NETHER_WARFIELD.configured(new JigsawConfiguration(NetherWarfieldPools.START, 6), ECBiomeTags.HAS_NETHER_WARFIELD, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> HOLLOW_TREE = register(
			new ResourceLocation(MODID, "hollow_tree"),
			ECStructures.HOLLOW_TREE.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_HOLLOW_TREE, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> ENTRENCHMENT = register(
			new ResourceLocation(MODID, "entrenchment"),
			ECStructures.ENTRENCHMENT.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_ENTRENCHMENT, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> VILLAGE_SWAMP = register(
			new ResourceLocation(MODID, "village_swamp"),
			StructureFeature.VILLAGE.configured(new JigsawConfiguration(SwampVillagePools.START, 6), ECBiomeTags.HAS_VILLAGE_SWAMP, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> BADLANDS_CAMP = register(
			new ResourceLocation(MODID, "badlands_camp"),
			ECStructures.BADLANDS_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_BADLANDS, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> BIRCH_CAMP = register(
			new ResourceLocation(MODID, "birch_camp"),
			ECStructures.BIRCH_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_BIRCH, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> DESERT_CAMP = register(
			new ResourceLocation(MODID, "desert_camp"),
			ECStructures.DESERT_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_DESERT, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> JUNGLE_CAMP = register(
			new ResourceLocation(MODID, "jungle_camp"),
			ECStructures.JUNGLE_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_JUNGLE, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> PLAINS_CAMP = register(
			new ResourceLocation(MODID, "plains_camp"),
			ECStructures.PLAINS_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_PLAINS, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> SAVANNA_CAMP = register(
			new ResourceLocation(MODID, "savanna_camp"),
			ECStructures.SAVANNA_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_SAVANNA, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> SNOW_CAMP = register(
			new ResourceLocation(MODID, "snow_camp"),
			ECStructures.SNOW_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_SNOW, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> STONY_CAMP = register(
			new ResourceLocation(MODID, "stony_camp"),
			ECStructures.STONY_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_STONY, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> SWAMP_CAMP = register(
			new ResourceLocation(MODID, "swamp_camp"),
			ECStructures.SWAMP_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_SWAMP, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> TAIGA_CAMP = register(
			new ResourceLocation(MODID, "taiga_camp"),
			ECStructures.TAIGA_CAMP.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_CAMP_TAIGA, true)
	);

	public static final List<StructureSet.StructureSelectionEntry> ALL_CAMPS = Lists.newArrayList(
			StructureSet.entry(BADLANDS_CAMP), StructureSet.entry(BIRCH_CAMP),
			StructureSet.entry(DESERT_CAMP), StructureSet.entry(JUNGLE_CAMP),
			StructureSet.entry(PLAINS_CAMP), StructureSet.entry(SAVANNA_CAMP),
			StructureSet.entry(SNOW_CAMP), StructureSet.entry(STONY_CAMP),
			StructureSet.entry(SWAMP_CAMP), StructureSet.entry(TAIGA_CAMP)
	);

	public static final List<StructureSet.StructureSelectionEntry> ALL_VILLAGES = Lists.newArrayList(
			StructureSet.entry(ECConfiguredStructures.VILLAGE_SWAMP)
	);

	private static Holder<ConfiguredStructureFeature<?, ?>> register(ResourceLocation id, ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, configuredStructureFeature);
	}

	public static void init() {}
}
