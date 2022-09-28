package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.triggers.PiglinCuteyTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ECTriggers {
	public static final PiglinCuteyTrigger PIGLIN_CUTEY = CriteriaTriggers.register(new PiglinCuteyTrigger());

	public static void init() { }
}
