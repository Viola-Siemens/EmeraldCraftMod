package com.hexagram2021.emeraldcraft.common.entities.ai.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;

public class FlyRandomlyTask extends Task<CreatureEntity> {
	private final float speedModifier;
	private final int maxHorizontalDistance;
	private final int maxVerticalDistance;

	public FlyRandomlyTask(float speedModifier) {
		this(speedModifier, 10, 7);
	}

	public FlyRandomlyTask(float speedModifier, int maxHorizontalDistance, int maxVerticalDistance) {
		super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
		this.speedModifier = speedModifier;
		this.maxHorizontalDistance = maxHorizontalDistance;
		this.maxVerticalDistance = maxVerticalDistance;
	}

	@Override
	protected void start(@Nonnull ServerWorld level, @Nonnull CreatureEntity entity, long tick) {
		Optional<Vector3d> optional = Optional.ofNullable(RandomPositionGenerator.getAirPos(
				entity, this.maxHorizontalDistance, this.maxVerticalDistance, -2, entity.getViewVector(0.0F), Math.PI / 2.0D
		));
		entity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, optional.map((vec3) -> new WalkTarget(vec3, this.speedModifier, 0)));
	}
}
