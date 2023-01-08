package com.hexagram2021.emeraldcraft.common.entities.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class MantaAi {
	public static Brain<?> makeBrain(Brain<MantaEntity> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<MantaEntity> brain) {
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(
				new Swim(0.8F),
				new AnimalPanic(2.5F),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink()
		));
	}

	private static void initIdleActivity(Brain<MantaEntity> brain) {
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
				Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(entity -> true, 6.0F), UniformInt.of(30, 60))),
				Pair.of(1, new StayCloseToTarget<>(MantaAi::getLikedPlayerPositionTracker, 7, 32, 1.0F)),
				Pair.of(2, new RunOne<>(ImmutableList.of(
						Pair.of(new FlyingRandomStroll(1.0F), 2),
						Pair.of(new SetWalkTargetFromLookTarget(1.0F, 3), 2),
						Pair.of(new DoNothing(30, 60), 1)
				)))
		), ImmutableSet.of());
	}

	public static void updateActivity(MantaEntity manta) {
		manta.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}

	private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity manta) {
		return getLikedPlayer(manta).map(entity -> new EntityTracker(entity, true));
	}

	private static Optional<ServerPlayer> getLikedPlayer(LivingEntity manta) {
		Level level = manta.getLevel();
		if (!level.isClientSide() && level instanceof ServerLevel serverlevel) {
			Optional<UUID> optional = manta.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (optional.isPresent()) {
				Entity entity = serverlevel.getEntity(optional.get());
				if (entity instanceof ServerPlayer serverplayer) {
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
