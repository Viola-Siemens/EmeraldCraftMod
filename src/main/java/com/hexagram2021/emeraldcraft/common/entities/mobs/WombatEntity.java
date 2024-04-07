package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.hexagram2021.emeraldcraft.common.register.ECBlockTags;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class WombatEntity extends TamableAnimal implements NeutralMob {
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(WombatEntity.class, EntityDataSerializers.INT);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(16, 31);
	@Nullable
	private UUID persistentAngerTarget;

	public WombatEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
		super(entityType, level);
		this.setTame(false);
		this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WombatPanicGoal(1.5D));
		this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Fox.class, 12.0F, 1.0D, 1.5D));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 12.0F, 1.0D, 1.5D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true) {
			@Override
			protected void resetAttackCooldown() {
				super.resetAttackCooldown();
				if(this.mob.getRandom().nextInt(5) < 2) {
					this.mob.setTarget(null);
				}
			}
		});
		this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
		this.goalSelector.addGoal(6, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.5D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4D)
				.add(Attributes.MAX_HEALTH, 12.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ARMOR, 1.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
		this.playSound(ECSounds.WOMBAT_STEP, 0.15F, 1.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		this.addPersistentAngerSaveData(nbt);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.readPersistentAngerSaveData(this.level(), nbt);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ECSounds.WOMBAT_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_30424_) {
		return ECSounds.WOMBAT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.WOMBAT_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float value) {
		Entity entity = damageSource.getEntity();

		if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
			value = (value + 1.0F) / 2.0F;
		}
		Vec3 sourcePosition = damageSource.getSourcePosition();
		if(sourcePosition != null) {
			Vec3 damageDirection = sourcePosition.vectorTo(this.position()).normalize();
			damageDirection = new Vec3(damageDirection.x, 0.0D, damageDirection.z);
			if (damageDirection.dot(this.getViewVector(1.0F)) > 0.2D) {		//Hit wombat's butt
				value = value / 4.0F;
			}
		}

		return super.hurt(damageSource, value);
	}

	@Override
	public void setTame(boolean tamed) {
		super.setTame(tamed);
		AttributeInstance attribute = Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH));
		if(tamed) {
			attribute.setBaseValue(30.0D);
			this.setHealth(30.0F);
		} else {
			attribute.setBaseValue(12.0D);
		}
	}

	@Override @Nullable
	public WombatEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob other) {
		WombatEntity ret = ECEntities.WOMBAT.create(serverLevel);
		if(ret != null) {
			UUID uuid = this.getOwnerUUID();
			if (uuid != null) {
				ret.setOwnerUUID(uuid);
				ret.setTame(true);
			}
		}
		return ret;
	}

	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
		if(target instanceof Creeper || target instanceof Ghast) {
			return false;
		}
		if(target instanceof OwnableEntity ownable && ownable.getOwner() == owner) {
			return false;
		}
		return super.wantsToAttack(target, owner);
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return !this.isAngry() && super.canBeLeashed(player);
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, 0.6D * this.getEyeHeight(), 0.4D * this.getBbWidth());
	}

	@Override
	protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions entityDimensions, float scale) {
		return new Vector3f(0.0F, entityDimensions.height - 0.03125F * scale, -0.0625F * scale);
	}

	public int getMaxSpawnClusterSize() {
		return 2;
	}

	public int getRemainingPersistentAngerTime() {
		return this.entityData.get(DATA_REMAINING_ANGER_TIME);
	}

	public void setRemainingPersistentAngerTime(int p_30404_) {
		this.entityData.set(DATA_REMAINING_ANGER_TIME, p_30404_);
	}

	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}

	@Nullable
	public UUID getPersistentAngerTarget() {
		return this.persistentAngerTarget;
	}

	public void setPersistentAngerTarget(@Nullable UUID uuid) {
		this.persistentAngerTarget = uuid;
	}

	public static boolean checkWombatSpawnRules(EntityType<? extends WombatEntity> entityType, LevelAccessor level, MobSpawnType spawnType,
											  BlockPos blockPos, RandomSource random) {
		return random.nextInt(4) == 0 && level.getBlockState(blockPos.below()).is(ECBlockTags.WOMBATS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, blockPos);
	}

	private class WombatPanicGoal extends PanicGoal {
		public WombatPanicGoal(double p_25692_) {
			super(WombatEntity.this, p_25692_);
		}

		@Override
		protected boolean shouldPanic() {
			if(WombatEntity.this.isTame()) {
				return super.shouldPanic();
			}
			return this.mob.isFreezing() || this.mob.isOnFire();
		}
	}
}
