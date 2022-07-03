package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.world.pools.NetherWarfieldPools;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECConfiguredStructures {
	public static final Holder<ConfiguredStructureFeature<?, ?>> SHELTER_HOUSE = register(
			new ResourceLocation(MODID, "shelter_house"),
			ECStructures.SHELTER.configured(NoneFeatureConfiguration.INSTANCE, ECBiomeTags.HAS_SHELTER, true)
	);
	public static final Holder<ConfiguredStructureFeature<?, ?>> NETHER_WARFIELD = register(
			new ResourceLocation(MODID, "nether_warfield"),
			ECStructures.NETHER_WARFIELD.configured(new JigsawConfiguration(NetherWarfieldPools.START, 6), ECBiomeTags.HAS_NETHER_WARFIELD, true)
	);

	public static final StructureSet SET_SHELTER_HOUSE = new StructureSet(SHELTER_HOUSE, new RandomSpreadStructurePlacement(64, 32, RandomSpreadType.LINEAR, 1926081709));
	public static final StructureSet SET_NETHER_WARFIELD = new StructureSet(NETHER_WARFIELD, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 10387312));

	public static void init() {
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "shelter_house"), SET_SHELTER_HOUSE);
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "nether_warfield"), SET_NETHER_WARFIELD);
	}

	private static Holder<ConfiguredStructureFeature<?, ?>> register(ResourceLocation id, ConfiguredStructureFeature<?, ?> configuredStructureFeature) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, configuredStructureFeature);
	}
}
