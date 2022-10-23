package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.util.triggers.*;
import net.minecraft.advancements.CriteriaTriggers;

public class ECTriggers {
	public static final PiglinCuteyTrigger PIGLIN_CUTEY = CriteriaTriggers.register(new PiglinCuteyTrigger());
	public static final CuredZombifiedPiglinTrigger CURED_ZOMBIFIED_PIGLIN = CriteriaTriggers.register(new CuredZombifiedPiglinTrigger());
	public static final CuredPhantomTrigger CURED_PHANTOM = CriteriaTriggers.register(new CuredPhantomTrigger());

	public static void init() { }
}
