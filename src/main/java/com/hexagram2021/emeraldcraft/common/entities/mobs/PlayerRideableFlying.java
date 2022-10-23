package com.hexagram2021.emeraldcraft.common.entities.mobs;

public interface PlayerRideableFlying {
	default boolean canFly() {
		return true;
	}
	
	// called each tick when player sits on a flyable mob.
	void fly(int velocity);
}
