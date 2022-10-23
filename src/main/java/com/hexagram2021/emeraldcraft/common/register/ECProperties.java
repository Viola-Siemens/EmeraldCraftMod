package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

public class ECProperties {
	public static final IntegerProperty EXP_COUNT = IntegerProperty.create("exp_count", 0, 15);
	public static final IntegerProperty HONEY_COUNT = IntegerProperty.create("honey_count", 0, 7);
	public static final BooleanProperty LEAF = BooleanProperty.create("leaf");
}
