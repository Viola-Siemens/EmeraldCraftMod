package com.hexagram2021.emeraldcraft.common.entities.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hexagram2021.emeraldcraft.common.entities.ai.behaviors.GoToNearestDarkPosition;
import com.hexagram2021.emeraldcraft.common.entities.mobs.LumineEntity;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
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

public class LumineAi {
	public static Brain<?> makeBrain(Brain<LumineEntity> brain) {
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}

	private static void initCoreActivity(Brain<LumineEntity> brain) {
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(
				new Swim(0.8F),
				new AnimalPanic(2.5F),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink(),
				new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS),
				new CountDownCooldownTicks(ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get())
		));
	}

	private static void initIdleActivity(Brain<LumineEntity> brain) {
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
				Pair.of(0, new GoToNearestDarkPosition<>(1.75F, true, 32)),
				Pair.of(1, new GoToWantedItem<>(lumine -> true, 1.75F, true, 32)),
				Pair.of(1, new GoAndGiveItemsToTarget<>(LumineAi::getLikedPlayerPositionTracker, 2.25F)),
				Pair.of(2, new StayCloseToTarget<>(LumineAi::getLikedPlayerPositionTracker, 4, 16, 2.25F)),
				Pair.of(3, new RunSometimes<>(new SetEntityLookTarget(livingEntity -> true, 6.0F), UniformInt.of(30, 60))),
				Pair.of(4, new RunOne<>(ImmutableList.of(
						Pair.of(new FlyingRandomStroll(1.0F), 2),
						Pair.of(new SetWalkTargetFromLookTarget(1.0F, 3), 2),
						Pair.of(new DoNothing(30, 60), 1)
				)))
		), ImmutableSet.of());
	}

	public static void updateActivity(LumineEntity entity) {
		entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}

	private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity livingEntity) {
		return getLikedPlayer(livingEntity).map(player -> new EntityTracker(player, true));
	}

	public static Optional<ServerPlayer> getLikedPlayer(LivingEntity livingEntity) {
		Level level = livingEntity.getLevel();
		if (!level.isClientSide() && level instanceof ServerLevel serverlevel) {
			Optional<UUID> optional = livingEntity.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (optional.isPresent()) {
				Entity entity = serverlevel.getEntity(optional.get());
				if (entity instanceof ServerPlayer serverplayer) {
					if ((serverplayer.gameMode.isSurvival() || serverplayer.gameMode.isCreative()) && serverplayer.closerThan(livingEntity, 64.0D)) {
						return Optional.of(serverplayer);
					}
				}

				return Optional.empty();
			}
		}

		return Optional.empty();
	}
}
