package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ECProperty {
	public static final IntegerProperty EXP_COUNT = IntegerProperty.create("exp_count", 0, 15);
	public static final IntegerProperty HONEY_COUNT = IntegerProperty.create("honey_count", 0, 7);
}
