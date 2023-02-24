package com.hexagram2021.emeraldcraft.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ECCommonConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.IntValue EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT;
	public static final ForgeConfigSpec.IntValue EMERALD_CRAFT_NETHER_BIOMES_WEIGHT;
	public static final ForgeConfigSpec.BooleanValue GENERATE_DEAD_CRIMSON_OCEAN;
	public static final ForgeConfigSpec.BooleanValue GENERATE_DEAD_WARPED_OCEAN;
	public static final ForgeConfigSpec.BooleanValue GENERATE_XANADU;
	public static final ForgeConfigSpec.BooleanValue GENERATE_GINKGO_FOREST;
	public static final ForgeConfigSpec.BooleanValue GENERATE_KARST_HILLS;
	public static final ForgeConfigSpec.BooleanValue GENERATE_PETUNIA_PLAINS;
	public static final ForgeConfigSpec.BooleanValue GENERATE_GOLDEN_BEACH;
	public static final ForgeConfigSpec.BooleanValue GENERATE_PALM_BEACH;
	public static final ForgeConfigSpec.BooleanValue GENERATE_AZURE_DESERT;
	public static final ForgeConfigSpec.BooleanValue GENERATE_JADEITE_DESERT;
	public static final ForgeConfigSpec.BooleanValue GENERATE_VOLCANIC_CAVES;
	public static final ForgeConfigSpec.BooleanValue GENERATE_MOSSY_CAVES;
	public static final ForgeConfigSpec.BooleanValue GENERATE_EMERY_DESERT;
	public static final ForgeConfigSpec.BooleanValue GENERATE_QUARTZ_DESERT;
	public static final ForgeConfigSpec.BooleanValue GENERATE_PURPURACEUS_SWAMP;

	public static final ForgeConfigSpec.BooleanValue ENABLE_CURE_ZOMBIFIED_PIGLIN;
	public static final ForgeConfigSpec.IntValue ZOMBIFIED_PIGLIN_CONVERT_TO_PIGLIN_BRUTE_POSSIBILITY_INV;
	public static final ForgeConfigSpec.BooleanValue ENABLE_CURE_PHANTOM;

	public static final ForgeConfigSpec.BooleanValue ENABLE_JEI_TRADING_SHADOW_RECIPE;

	static {
		BUILDER.push("emeraldcraft-common-config");
			BUILDER.comment("You can determine whether each biome can generate in the world or not.");
				BUILDER.push("biomes-generation");
				EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT = BUILDER.defineInRange("EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT", 40, 0, 65536);
				EMERALD_CRAFT_NETHER_BIOMES_WEIGHT = BUILDER.defineInRange("EMERALD_CRAFT_NETHER_BIOMES_WEIGHT", 40, 0, 65536);
				GENERATE_DEAD_CRIMSON_OCEAN = BUILDER.define("GENERATE_DEAD_CRIMSON_OCEAN", true);
				GENERATE_DEAD_WARPED_OCEAN = BUILDER.define("GENERATE_DEAD_WARPED_OCEAN", true);
				GENERATE_XANADU = BUILDER.define("GENERATE_XANADU", true);
				GENERATE_GINKGO_FOREST = BUILDER.define("GENERATE_GINKGO_FOREST", true);
				GENERATE_KARST_HILLS = BUILDER.define("GENERATE_KARST_HILLS", true);
				GENERATE_PETUNIA_PLAINS = BUILDER.define("GENERATE_PETUNIA_PLAINS", true);
				GENERATE_GOLDEN_BEACH = BUILDER.define("GENERATE_GOLDEN_BEACH", true);
				GENERATE_PALM_BEACH = BUILDER.define("GENERATE_PALM_BEACH", true);
				GENERATE_AZURE_DESERT = BUILDER.define("GENERATE_AZURE_DESERT", true);
				GENERATE_JADEITE_DESERT = BUILDER.define("GENERATE_JADEITE_DESERT", true);
				GENERATE_VOLCANIC_CAVES = BUILDER.define("GENERATE_VOLCANIC_CAVES", true);
				GENERATE_MOSSY_CAVES = BUILDER.define("GENERATE_MOSSY_CAVES", true);
				GENERATE_EMERY_DESERT = BUILDER.define("GENERATE_EMERY_DESERT", true);
				GENERATE_QUARTZ_DESERT = BUILDER.define("GENERATE_QUARTZ_DESERT", true);
				GENERATE_PURPURACEUS_SWAMP = BUILDER.define("GENERATE_PURPURACEUS_SWAMP", true);
			BUILDER.pop();

			BUILDER.comment("You can enable/disable curing mobs and change the possibilities.");
			BUILDER.push("cure-mobs");
				ENABLE_CURE_ZOMBIFIED_PIGLIN = BUILDER.comment("Allow players to cure zombified piglin.")
						.define("ENABLE_CURE_ZOMBIFIED_PIGLIN", true);
				ZOMBIFIED_PIGLIN_CONVERT_TO_PIGLIN_BRUTE_POSSIBILITY_INV = BUILDER.comment("Multiplicative Inverse of the possibility of cured zombified piglin convert to piglin brute. For example, 16 means 6.25% (1/16) chance for converting to piglin brute and 93.75% for piglin.")
						.defineInRange("ZOMBIFIED_PIGLIN_CONVERT_TO_PIGLIN_BRUTE_POSSIBILITY_INV", 16, 1, 256);
				ENABLE_CURE_PHANTOM = BUILDER.comment("Allow players to cure phantom.")
						.define("ENABLE_CURE_PHANTOM", true);
			BUILDER.pop();

			BUILDER.comment("You can disable some functions to improve the speed of server starting.");
			BUILDER.push("additional-functions");
				ENABLE_JEI_TRADING_SHADOW_RECIPE = BUILDER.comment("If disabled, players cannot use JEI to look over the trading list of villagers and other tradable mobs, but it might be faster for starting the server.")
						.define("ENABLE_JEI_TRADING_SHADOW_RECIPE", true);
			BUILDER.pop();
		BUILDER.pop();

		SPEC = BUILDER.build();
	}
}
