package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.entities.ai.MantaAi;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class MantaEntity extends CreatureEntity implements PlayerRideableFlying {
	protected static final ImmutableList<SensorType<? extends Sensor<? super MantaEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.HURT_BY
	);
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.PATH,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.HURT_BY,
			ECMemoryModuleTypes.LIKED_PLAYER.get(),
			ECMemoryModuleTypes.IS_PANICKING.get()
	);

	public MantaEntity(EntityType<? extends MantaEntity> type, World level) {
		super(type, level);
		this.moveControl = new FlyingMovementController(this, 20, true);
	}

	@Override @Nonnull
	protected Brain.BrainCodec<MantaEntity> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	@Override @Nonnull
	protected Brain<?> makeBrain(@Nonnull Dynamic<?> dynamic) {
		return MantaAi.makeBrain(this.brainProvider().makeBrain(dynamic));
	}

	@Override @Nonnull @SuppressWarnings("unchecked")
	public Brain<MantaEntity> getBrain() {
		return (Brain<MantaEntity>)super.getBrain();
	}

	@SuppressWarnings("unused")
	public boolean isTame() {
		return this.getBrain().getMemory(ECMemoryModuleTypes.LIKED_PLAYER.get()).isPresent();
	}

	@Nullable
	public UUID getOwnerUUID() {
		Optional<UUID> player = this.getBrain().getMemory(ECMemoryModuleTypes.LIKED_PLAYER.get());
		return player.orElse(null);
	}

	public void setOwnerUUID(@Nullable UUID uuid) {
		this.getBrain().setMemory(ECMemoryModuleTypes.LIKED_PLAYER.get(), uuid);
	}
	
	@SuppressWarnings("unused")
	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerUUID();
			return uuid == null ? null : this.level.getPlayerByUUID(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	public void cureFrom(PhantomEntity phantom, PlayerEntity player) {
		this.setOwnerUUID(player.getUUID());
		if (player instanceof ServerPlayerEntity) {
			ECTriggers.CURED_PHANTOM.trigger((ServerPlayerEntity)player, phantom, this);
		}
	}
	
	@Override
	// called each tick when player sits on a flyable mob.
	public void fly(int velocity) {
		if(!this.onGround || velocity > 15) {
			Vector3d move = this.getDeltaMovement();
			this.setDeltaMovement(move.x, (velocity - 15) / 45.0D, move.z);
		}
	}
	
	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.FLYING_SPEED, 0.2D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D)
				.add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	@Override @Nonnull
	protected PathNavigator createNavigation(@Nonnull World level) {
		FlyingPathNavigator flyingpathnavigation = new FlyingPathNavigator(this, level);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	@Override
	protected float getStandingEyeHeight(@Nonnull Pose pose, EntitySize dim) {
		return dim.height * 0.6F;
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float damage) {
		Entity entity = damageSource.getEntity();
		if (entity instanceof PlayerEntity) {
			UUID owner = this.getOwnerUUID();
			if (owner != null && entity.getUUID().equals(owner)) {
				return false;
			}
		}

		return super.hurt(damageSource, damage);
	}

	@Override
	public void travel(@Nonnull Vector3d velo) {
		if (this.isAlive() && (this.isEffectiveAi() || this.isControlledByLocalInstance())) {
			LivingEntity passenger = this.getControllingPassenger();
			if (this.isInWater()) {
				this.moveRelative(0.02F, velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
			} else if (this.isInLava()) {
				this.moveRelative(0.02F, velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.3D));
			} else if (this.isVehicle() && passenger != null) {
				this.setRot(passenger.yRot, passenger.xRot * 0.5F);
				this.yRotO = this.yRot;
				this.yBodyRot = this.yRot;
				this.yHeadRot = this.yBodyRot;
				
				super.travel(new Vector3d(passenger.xxa * 4.0D, velo.y, passenger.zza * (passenger.zza > 0.0D ? 5.0D : 3.0D)));
			} else {
				this.moveRelative(this.getSpeed(), velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
			}
		}

		this.calculateEntityAnimation(this, false);
	}

	@Override @Nullable
	public LivingEntity getControllingPassenger() {
		Entity passenger = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
		return passenger instanceof PlayerEntity ? (PlayerEntity)passenger : null;
	}

	@Override @Nullable
	public SoundEvent getAmbientSound() {
		return ECSounds.MANTA_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
		return ECSounds.MANTA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.MANTA_DEATH;
	}

	@Override
	protected void playStepSound(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
	}

	@Override
	protected void checkFallDamage(double velo, boolean onGround, @Nonnull BlockState blockState, @Nonnull BlockPos blockPos) {
	}

	@Override
	public boolean onClimbable() {
		return false;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	public int getUniqueFlapTickOffset() {
		return this.getId() * 3;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level.isClientSide && this.isAlive() && this.tickCount % 20 == 0) {
			this.heal(1.0F);
		}
	}

	@Override
	protected void customServerAiStep() {
		this.level.getProfiler().push("mantaBrain");
		this.getBrain().tick((ServerWorld) this.level, this);
		this.level.getProfiler().pop();
		this.level.getProfiler().push("mantaActivityUpdate");
		MantaAi.updateActivity(this);
		this.level.getProfiler().pop();
		super.customServerAiStep();
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level.isClientSide) {
			float f = MathHelper.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount) * 0.1F + (float) Math.PI);

			this.level.addParticle(ParticleTypes.FIREWORK,
					this.getX() + f + this.random.nextDouble() - 0.5D,
					this.getY() + f + this.random.nextDouble() * 0.5D,
					this.getZ() + f + this.random.nextDouble() - 0.5D,
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier) {
		return false;
	}

	@Override @Nonnull
	public ActionResultType mobInteract(@Nonnull PlayerEntity player, @Nonnull Hand hand) {
		if (!this.isBaby()) {
			if (this.isVehicle()) {
				return super.mobInteract(player, hand);
			}
		}

		if (!this.level.isClientSide) {
			player.yRot = this.yRot;
			player.xRot = this.xRot;
			player.startRiding(this);
		}

		return ActionResultType.sidedSuccess(this.level.isClientSide);
	}

	@Override
	protected void sendDebugPackets() {
		super.sendDebugPackets();
		DebugPacketSender.sendEntityBrain(this);
	}

	@Override
	public boolean makeFlySound() {
		return !this.isOnGround();
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override @Nonnull
	public Vector3d getLeashOffset() {
		return new Vector3d(0.0D, this.getEyeHeight() * 0.6D, this.getBbWidth() * 0.1D);
	}
}
