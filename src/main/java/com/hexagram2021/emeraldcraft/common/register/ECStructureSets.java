package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructureSets {
	public static final StructureSet SET_SHELTER_HOUSE = new StructureSet(
			ECConfiguredStructures.SHELTER_HOUSE,
			new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 1926081709)
	);
	public static final StructureSet SET_NETHER_WARFIELD = new StructureSet(
			ECConfiguredStructures.NETHER_WARFIELD,
			new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 20387312)
	);

	public static final StructureSet SET_CAMP = new StructureSet(
			ECConfiguredStructures.ALL_CAMPS,
			new RandomSpreadStructurePlacement(50, 20, RandomSpreadType.LINEAR, 200013907)
	);

	public static final StructureSet SET_VILLAGE = new StructureSet(
			ECConfiguredStructures.ALL_VILLAGES,
			new RandomSpreadStructurePlacement(34, 8, RandomSpreadType.LINEAR, 30387312)
	);

	public static void init() {
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "shelter_house"), SET_SHELTER_HOUSE);
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "nether_warfield"), SET_NETHER_WARFIELD);
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "camp"), SET_CAMP);
		Registry.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(MODID, "village"), SET_VILLAGE);
	}
}
