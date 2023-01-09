package com.hexagram2021.emeraldcraft.common.entities.mobs;

import net.minecraft.world.entity.PlayerRideable;

public interface PlayerRideableFlying extends PlayerRideable {
	default boolean canFly() {
		return true;
	}

	// called each tick when player sits on a flyable mob.
	void fly(int velocity);
}
