package com.hexagram2021.emeraldcraft.common.entities.ai.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.IPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Function;

public class StayCloseToTargetTask<E extends LivingEntity> extends Task<E> {
	private final Function<LivingEntity, Optional<IPosWrapper>> targetPositionGetter;
	private final int closeEnough;
	private final int tooFar;
	private final float speedModifier;

	public StayCloseToTargetTask(Function<LivingEntity, Optional<IPosWrapper>> targetPositionGetter, int closeEnough, int tooFar, float speedModifier) {
		super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
		this.targetPositionGetter = targetPositionGetter;
		this.closeEnough = closeEnough;
		this.tooFar = tooFar;
		this.speedModifier = speedModifier;
	}

	@Override
	protected boolean checkExtraStartConditions(@Nonnull ServerWorld level, @Nonnull E entity) {
		Optional<IPosWrapper> optional = this.targetPositionGetter.apply(entity);
		if(optional.isPresent()) {
			IPosWrapper wrapper = optional.get();
			return !entity.position().closerThan(wrapper.currentPosition(), this.tooFar);
		}
		return false;
	}
	
	@Override
	protected void start(@Nonnull ServerWorld level, @Nonnull E entity, long tick) {
		BrainUtil.setWalkAndLookTargetMemories(entity, this.targetPositionGetter.apply(entity).get().currentBlockPosition(), this.speedModifier, this.closeEnough);
	}
}
