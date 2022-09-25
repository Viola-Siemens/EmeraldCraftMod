package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.world.level.block.state.properties.WoodType;

public class ECWoodType {
	public static final WoodType GINKGO = WoodType.register(WoodType.create("ginkgo"));
	public static final WoodType PALM = WoodType.register(WoodType.create("palm"));
	public static final WoodType PEACH = WoodType.register(WoodType.create("peach"));
	public static final WoodType PURPURACEUS = WoodType.register(WoodType.create("purpuraceus"));

	public static void init() {}
}
