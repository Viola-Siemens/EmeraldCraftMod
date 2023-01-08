package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.world.level.block.state.properties.WoodType;

public class ECWoodType {
	public static final WoodType GINKGO = WoodType.register(WoodType.create("ec_ginkgo"));
	public static final WoodType PALM = WoodType.register(WoodType.create("ec_palm"));
	public static final WoodType PEACH = WoodType.register(WoodType.create("ec_peach"));
	public static final WoodType PURPURACEUS = WoodType.register(WoodType.create("ec_purpuraceus"));

	public static void init() {}
}
