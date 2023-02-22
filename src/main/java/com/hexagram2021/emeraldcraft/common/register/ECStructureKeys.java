package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ECStructureKeys {
	public static final ResourceKey<Structure> SHELTER = createKey("shelter");
	public static final ResourceKey<Structure> NETHER_WARFIELD = createKey("nether_warfield");
	public static final ResourceKey<Structure> ENTRENCHMENT = createKey("entrenchment");
	public static final ResourceKey<Structure> VILLAGE_SWAMP = createKey("village_swamp");
	public static final ResourceKey<Structure> HOLLOW_TREE = createKey("hollow_tree");
	public static final ResourceKey<Structure> BADLANDS_CAMP = createKey("badlands_camp");
	public static final ResourceKey<Structure> BIRCH_CAMP = createKey("birch_camp");
	public static final ResourceKey<Structure> DESERT_CAMP = createKey("desert_camp");
	public static final ResourceKey<Structure> JUNGLE_CAMP = createKey("jungle_camp");
	public static final ResourceKey<Structure> PLAINS_CAMP = createKey("plains_camp");
	public static final ResourceKey<Structure> SAVANNA_CAMP = createKey("savanna_camp");
	public static final ResourceKey<Structure> SNOW_CAMP = createKey("snow_camp");
	public static final ResourceKey<Structure> STONY_CAMP = createKey("stony_camp");
	public static final ResourceKey<Structure> SWAMP_CAMP = createKey("swamp_camp");
	public static final ResourceKey<Structure> TAIGA_CAMP = createKey("taiga_camp");

	private static ResourceKey<Structure> createKey(String name) {
		return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(MODID, name));
	}
}
