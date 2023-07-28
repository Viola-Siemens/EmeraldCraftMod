package com.hexagram2021.emeraldcraft.common.entities.goals;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;

public class OfferFlowerToPlayerGoal extends Goal {
	private static final TargetingConditions OFFER_TARGET_CONTEXT = TargetingConditions.forNonCombat().range(6.0D).selector(entity ->
			entity.hasEffect(MobEffects.HERO_OF_THE_VILLAGE));
	public static final int OFFER_TICKS = 400;
	private final IronGolem golem;

	@Nullable
	private Player player;
	private int tick;

	public OfferFlowerToPlayerGoal(IronGolem ironGolem) {
		this.golem = ironGolem;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		if (!this.golem.level().isDay()) {
			return false;
		}
		if (this.golem.getRandom().nextInt(4000) != 0) {
			return false;
		}
		this.player = this.golem.level().getNearestPlayer(OFFER_TARGET_CONTEXT, this.golem);
		return this.player != null && this.golem.closerThan(this.player, 6.0D);
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
		this.golem.getLookControl().setLookAt(Objects.requireNonNull(this.player), 30.0F, 30.0F);
		--this.tick;
	}
}
