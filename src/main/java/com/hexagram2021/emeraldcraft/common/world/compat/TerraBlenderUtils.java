package com.hexagram2021.emeraldcraft.common.world.compat;

import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.world.surface.ECSurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class TerraBlenderUtils {
	public static void init() {
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ECSurfaceRules.overworld());
		SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.NETHER, MODID, ECSurfaceRules.nether());
		Regions.register(new ECOverworldBiomeRegion(ECCommonConfig.EMERALD_CRAFT_OVERWORLD_BIOMES_WEIGHT.get()));
		Regions.register(new ECNetherBiomeRegion(ECCommonConfig.EMERALD_CRAFT_NETHER_BIOMES_WEIGHT.get()));
	}
}
