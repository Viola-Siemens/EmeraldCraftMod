package com.hexagram2021.emeraldcraft.common.entities.ai.behaviors;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.phys.Vec3;

public class RandomFly extends RandomStroll {
	public RandomFly(float modifier) {
		super(modifier, true);
	}

	@Override
	protected Vec3 getTargetPos(PathfinderMob entity) {
		Vec3 vec3 = entity.getViewVector(0.0F);
		return AirAndWaterRandomPos.getPos(entity, this.maxHorizontalDistance, this.maxVerticalDistance, -2, vec3.x, vec3.z, Math.PI / 2.0D);
	}
}
