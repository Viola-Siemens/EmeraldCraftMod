package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ECBlockSetTypes {
	public static final BlockSetType GINKGO = new BlockSetType("ec_ginkgo");
	public static final BlockSetType PALM = new BlockSetType("ec_palm");
	public static final BlockSetType PEACH = new BlockSetType("ec_peach");
	public static final BlockSetType PURPURACEUS = new BlockSetType(
			"ec_purpuraceus", true, SoundType.NETHER_WOOD,
			SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN,
			SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
			SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
			SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON
	);

	public static void init() {}
}
