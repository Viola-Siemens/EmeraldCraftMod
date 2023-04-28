package com.hexagram2021.emeraldcraft.common.entities.ai.behaviors;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class StayCloseToTarget<E extends LivingEntity> extends Behavior<E> {
	private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;
	private final int closeEnough;
	private final int tooFar;
	private final float speedModifier;

	public StayCloseToTarget(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, int closeEnough, int tooFar, float speedModifier) {
		super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
		this.targetPositionGetter = targetPositionGetter;
		this.closeEnough = closeEnough;
		this.tooFar = tooFar;
		this.speedModifier = speedModifier;
	}

	@Override
	protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
		Optional<PositionTracker> optional = this.targetPositionGetter.apply(entity);
		if (optional.isEmpty()) {
			return false;
		} else {
			PositionTracker positiontracker = optional.get();
			return !entity.position().closerThan(positiontracker.currentPosition(), this.tooFar);
		}
	}

	@Override
	protected void start(@NotNull ServerLevel level, @NotNull E entity, long p_217396_) {
		BehaviorUtils.setWalkAndLookTargetMemories(entity, this.targetPositionGetter.apply(entity).get().currentBlockPosition(), this.speedModifier, this.closeEnough);
	}
}