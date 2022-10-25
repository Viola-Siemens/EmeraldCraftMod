package com.hexagram2021.emeraldcraft.common.entities.goals;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class OfferFlowerToPlayerGoal extends Goal {
	private static final TargetingConditions OFFER_TARGER_CONTEXT = TargetingConditions.forNonCombat().range(6.0D).selector(entity ->
			entity.hasEffect(MobEffects.HERO_OF_THE_VILLAGE));
	public static final int OFFER_TICKS = 400;
	private final IronGolem golem;
	private Player player;
	private int tick;

	public OfferFlowerToPlayerGoal(IronGolem ironGolem) {
		this.golem = ironGolem;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		if (!this.golem.level.isDay()) {
			return false;
		}
		if (this.golem.getRandom().nextInt(4000) != 0) {
			return false;
		}
		this.player = this.golem.level.getNearestEntity(Player.class, OFFER_TARGER_CONTEXT, this.golem, this.golem.getX(), this.golem.getY(), this.golem.getZ(), this.golem.getBoundingBox().inflate(6.0D, 2.0D, 6.0D));
		return this.player != null;
	}

	public boolean canContinueToUse() {
		return this.tick > 0;
	}

	public void start() {
		this.tick = this.adjustedTickDelay(OFFER_TICKS);
		this.golem.offerFlower(true);
	}

	public void stop() {
		this.golem.offerFlower(false);
		this.player = null;
	}

	public void tick() {
		this.golem.getLookControl().setLookAt(this.player, 30.0F, 30.0F);
		--this.tick;
	}
}
