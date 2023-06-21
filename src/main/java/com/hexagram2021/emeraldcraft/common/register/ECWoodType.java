package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ECWoodType {
	public static final WoodType GINKGO = WoodType.register(new WoodType("ec_ginkgo", ECBlockSetTypes.GINKGO));
	public static final WoodType PALM = WoodType.register(new WoodType("ec_palm", ECBlockSetTypes.PALM));
	public static final WoodType PEACH = WoodType.register(new WoodType("ec_peach", ECBlockSetTypes.PEACH));
	public static final WoodType PURPURACEUS = WoodType.register(new WoodType(
			"ec_purpuraceus", ECBlockSetTypes.PURPURACEUS,
			SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN,
			SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN
	));

	public static void init() {}
}
