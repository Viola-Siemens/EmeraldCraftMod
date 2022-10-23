package com.hexagram2021.emeraldcraft.common.entities.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.entities.ai.tasks.*;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.IPosWrapper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.UUID;

public class MantaAi {
	public static Brain<MantaEntity> makeBrain(Brain<MantaEntity> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<MantaEntity> brain) {
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(
				new SwimTask(0.8F),
				new AnimalPanicTask(2.5F),
				new LookTask(45, 90),
				new WalkToTargetTask()
		));
	}

	private static void initIdleActivity(Brain<MantaEntity> brain) {
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
				Pair.of(0, new RunSometimesTask<>(new LookAtEntityTask(entity -> true, 6.0F), RangedInteger.of(30, 60))),
				Pair.of(1, new StayCloseToTargetTask<>(MantaAi::getLikedPlayerPositionTracker, 7, 32, 1.0F)),
				Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(
						Pair.of(new FlyRandomlyTask(1.0F), 2),
						Pair.of(new WalkTowardsLookTargetTask(1.0F, 3), 2),
						Pair.of(new DummyTask(30, 60), 1)
				)))
		), ImmutableSet.of());
	}

	public static void updateActivity(MantaEntity manta) {
		manta.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}

	private static Optional<IPosWrapper> getLikedPlayerPositionTracker(LivingEntity manta) {
		return getLikedPlayer(manta).map(entity -> new EntityPosWrapper(entity, true));
	}

	private static Optional<ServerPlayerEntity> getLikedPlayer(LivingEntity manta) {
		World level = manta.level;
		if (!level.isClientSide() && level instanceof ServerWorld) {
			Optional<UUID> optional = manta.getBrain().getMemory(ECMemoryModuleTypes.LIKED_PLAYER.get());
			if (optional.isPresent()) {
				Entity entity = ((ServerWorld)level).getEntity(optional.get());
				if (entity instanceof ServerPlayerEntity) {
					ServerPlayerEntity serverplayer = (ServerPlayerEntity) entity;
					if ((serverplayer.gameMode.isSurvival() || serverplayer.gameMode.isCreative()) && serverplayer.closerThan(manta, 64.0D)) {
						return Optional.of(serverplayer);
					}
				}

				return Optional.empty();
			}
		}

		return Optional.empty();
	}
}
