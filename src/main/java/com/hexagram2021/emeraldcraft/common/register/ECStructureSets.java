package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.ListAppendable;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.List;

@SuppressWarnings("unused")
public class ECStructureSets {
	public static final Holder<StructureSet> SET_SHELTER_HOUSE = register(
			ECStructureSetKeys.SHELTER,
			ECStructures.SHELTER,
			new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 1926081709)
	);
	public static final Holder<StructureSet> SET_NETHER_WARFIELD = register(
			ECStructureSetKeys.NETHER_WARFIELD,
			ECStructures.NETHER_WARFIELD,
			new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 20387312)
	);

	public static final Holder<StructureSet> SET_ENTRENCHMENT = register(
			ECStructureSetKeys.ENTRENCHMENT,
			ECStructures.ENTRENCHMENT,
			new RandomSpreadStructurePlacement(40, 24, RandomSpreadType.TRIANGULAR, 23456)
	);

	public static final Holder<StructureSet> SET_HOLLOW_TREE = register(
			ECStructureSetKeys.HOLLOW_TREE,
			ECStructures.HOLLOW_TREE,
			new RandomSpreadStructurePlacement(24, 20, RandomSpreadType.LINEAR, 511604474)
	);

	public static final Holder<StructureSet> SET_CAMP = register(
			ECStructureSetKeys.CAMP,
			ECStructures.ALL_CAMPS,
			new RandomSpreadStructurePlacement(50, 20, RandomSpreadType.LINEAR, 200013907)
	);

	@SuppressWarnings("unchecked")
	public static void init() {
		((ListAppendable<StructureSet.StructureSelectionEntry>)(Object)(StructureSets.VILLAGES.get())).appendAll(ECStructures.ALL_VILLAGES);
	}

	static Holder<StructureSet> register(ResourceKey<StructureSet> key, StructureSet set) {
		return BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, key, set);
	}

	static Holder<StructureSet> register(ResourceKey<StructureSet> key, Holder<Structure> structure, StructurePlacement placement) {
		return register(key, new StructureSet(structure, placement));
	}

	@SuppressWarnings("SameParameterValue")
	static Holder<StructureSet> register(ResourceKey<StructureSet> key, List<StructureSet.StructureSelectionEntry> structures, StructurePlacement placement) {
		return register(key, new StructureSet(structures, placement));
	}
}
