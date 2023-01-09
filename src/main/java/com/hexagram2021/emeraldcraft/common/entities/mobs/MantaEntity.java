package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.entities.ai.MantaAi;
import com.hexagram2021.emeraldcraft.common.register.ECTriggers;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import com.hexagram2021.emeraldcraft.common.util.Vec3Util;
import com.mojang.serialization.Dynamic;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MantaEntity extends PathfinderMob implements PlayerRideableFlying, OwnableEntity, PlayerHealable {
	protected static final ImmutableList<SensorType<? extends Sensor<? super MantaEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.HURT_BY
	);
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.PATH,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.HURT_BY,
			MemoryModuleType.LIKED_PLAYER,
			MemoryModuleType.IS_PANICKING
	);

	public MantaEntity(EntityType<? extends MantaEntity> type, Level level) {
		super(type, level);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override @NotNull
	protected Brain.Provider<MantaEntity> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	@Override @NotNull
	protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
		return MantaAi.makeBrain(this.brainProvider().makeBrain(dynamic));
	}

	@Override @NotNull @SuppressWarnings("unchecked")
	public Brain<MantaEntity> getBrain() {
		return (Brain<MantaEntity>)super.getBrain();
	}

	@SuppressWarnings("unused")
	public boolean isTame() {
		return this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).isPresent();
	}

	@Override @Nullable
	public UUID getOwnerUUID() {
		Optional<UUID> player = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
		return player.orElse(null);
	}

	@Override @Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerUUID();
			return uuid == null ? null : this.level.getPlayerByUUID(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	public void cureFrom(Phantom phantom, Player player) {
		this.setHealedPlayer(player.getUUID());
		if (player instanceof ServerPlayer serverPlayer) {
			ECTriggers.CURED_PHANTOM.trigger(serverPlayer, phantom, this);
		}
	}

	@Override
	public void fly(int velocity) {
		if(!this.isOnGround() || velocity > 12) {
			Vec3 move = this.getDeltaMovement();
			this.setDeltaMovement(move.x, (velocity - 12) / 45.0D, move.z);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.FLYING_SPEED, 0.2D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
	}

	@Override @NotNull
	protected PathNavigation createNavigation(@NotNull Level level) {
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}

	@Override
	protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dim) {
		return dim.height * 0.6F;
	}

	@Override
	public boolean isBaby() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float damage) {
		Entity entity = damageSource.getEntity();
		if (entity instanceof Player player) {
			UUID owner = this.getOwnerUUID();
			if (owner != null && player.getUUID().equals(owner)) {
				return false;
			}
		}

		return super.hurt(damageSource, damage);
	}

	@Override
	public void travel(@NotNull Vec3 velo) {
		if (this.isAlive() && (this.isEffectiveAi() || this.isControlledByLocalInstance())) {
			LivingEntity passenger = this.getControllingPassenger();
			if(this.isOnGround()) {
				this.moveRelative(0.02F, Vec3Util.UP);
			}
			if (this.isInWater()) {
				this.moveRelative(0.02F, velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
			} else if (this.isInLava()) {
				this.moveRelative(0.02F, velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.3D));
			} else if (this.isVehicle() && passenger != null) {
				this.setYRot(passenger.getYRot());
				this.yRotO = this.getYRot();
				this.setXRot(passenger.getXRot() * 0.5F);
				this.setRot(this.getYRot(), this.getXRot());
				this.yBodyRot = this.getYRot();
				this.yHeadRot = this.yBodyRot;
				super.travel(new Vec3(passenger.xxa * 8.0D, velo.y, passenger.zza * 8.0D));
			} else{
				this.moveRelative(this.getSpeed(), velo);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
			}
		}

		this.calculateEntityAnimation(this, false);
	}

	@Override @Nullable
	public LivingEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof Player player ? player : null;
	}

	@Override @Nullable
	public SoundEvent getAmbientSound() {
		return ECSounds.MANTA_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ECSounds.MANTA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.MANTA_DEATH;
	}

	@Override
	protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
	}

	@Override
	protected void checkFallDamage(double velo, boolean onGround, @NotNull BlockState blockState, @NotNull BlockPos blockPos) {
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
		this.getBrain().tick((ServerLevel)this.level, this);
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
			float f = Mth.cos((float) (this.getUniqueFlapTickOffset() + this.tickCount) * 0.1F + (float) Math.PI);

			this.level.addParticle(ParticleTypes.FIREWORK,
					this.getX() + f + this.random.nextDouble() - 0.5D,
					this.getY() + f + this.random.nextDouble() * 0.5D,
					this.getZ() + f + this.random.nextDouble() - 0.5D,
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource damageSource) {
		return false;
	}

	@Override @NotNull
	public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
		if (!this.isBaby()) {
			if (this.isVehicle()) {
				return super.mobInteract(player, hand);
			}
		}

		if (!this.level.isClientSide) {
			player.setYRot(this.getYRot());
			player.setXRot(this.getXRot());
			player.startRiding(this);
		}

		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

	@Override
	protected void sendDebugPackets() {
		super.sendDebugPackets();
		DebugPackets.sendEntityBrain(this);
	}

	@Override
	public boolean isFlapping() {
		return !this.isOnGround();
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override @NotNull
	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, this.getEyeHeight() * 0.6D, this.getBbWidth() * 0.1D);
	}

	@Override
	public boolean isPlayerHealed() {
		return this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).isPresent();
	}

	@Deprecated
	@Override
	public void setPlayerHealed(boolean healed) {}

	@Override @NotNull
	public UUID getHealedPlayer() {
		Optional<UUID> player = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
		return player.orElse(Util.NIL_UUID);
	}

	@Override
	public void setHealedPlayer(@Nullable UUID player) {
		this.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player);
	}
}
