package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WraithEntity extends Monster {
	protected int spellCastingTickCount;

	public WraithEntity(EntityType<? extends WraithEntity> type, Level level) {
		super(type, level);
		this.xpReward = 12;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.6D).add(Attributes.FOLLOW_RANGE, 12.0D).add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ATTACK_SPEED, 8.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WraithNauseaSpellGoal());
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.0D, false));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.spellCastingTickCount = nbt.getInt("SpellTicks");
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt("SpellTicks", this.spellCastingTickCount);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ECSounds.WRAITH_AMBIENT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.WRAITH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ECSounds.WRAITH_HURT;
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (this.spellCastingTickCount > 0) {
			--this.spellCastingTickCount;
		}
	}

	class WraithNauseaSpellGoal extends Goal {
		protected int attackWarmupDelay;
		protected int nextAttackTickCount;

		@Override
		public boolean canUse() {
			LivingEntity livingentity = WraithEntity.this.getTarget();
			if (livingentity != null && livingentity.isAlive()) {
				if(WraithEntity.this.spellCastingTickCount > 0) {
					return false;
				}
				return WraithEntity.this.tickCount >= this.nextAttackTickCount;
			}
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			LivingEntity livingentity = WraithEntity.this.getTarget();
			return livingentity != null && livingentity.isAlive() && this.attackWarmupDelay > 0;
		}

		@Override
		public void start() {
			this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
			WraithEntity.this.spellCastingTickCount = this.getCastingTime();
			this.nextAttackTickCount = WraithEntity.this.tickCount + this.getCastingInterval() + WraithEntity.this.getRandom().nextInt(100);
		}

		@Override
		public void tick() {
			--this.attackWarmupDelay;
			if (this.attackWarmupDelay == 0) {
				this.performSpellCasting();
			}
		}

		protected void performSpellCasting() {
			LivingEntity target = Objects.requireNonNull(WraithEntity.this.getTarget());
			target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 2), WraithEntity.this);
			target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 160), WraithEntity.this);
			target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120), WraithEntity.this);
		}

		protected int getCastWarmupTime() {
			return 40;
		}

		protected int getCastingTime() {
			return 20;
		}

		protected int getCastingInterval() {
			return 280;
		}
	}
}
