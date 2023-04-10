package com.hexagram2021.emeraldcraft.common.entities.mobs;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.entities.ai.LumineAi;
import com.hexagram2021.emeraldcraft.common.register.ECEntities;
import com.hexagram2021.emeraldcraft.common.register.ECItemTags;
import com.hexagram2021.emeraldcraft.common.register.ECMemoryModuleTypes;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/*
 * 荧灵
 * 行为：
 * - 玩家可以将火把类物品递给荧灵，荧灵会自动前往周围（同一水平面或下方）20×4×20范围内亮度低于3的方块表面插上火把。
 * - 荧灵会自动捡起同类火把。
 * - 被玩家给予火把的荧灵不会受到该玩家的伤害。
 * - 如果荧灵在夜晚身处于方块亮度大于等于floor(14.25 - 4×月相亮度)的环境中，则它会跳舞，此时玩家用萤石粉与其交互则会使它复制。复制后一段时间内不能再次复制。
 * - (?) 意愿系统，并非每晚荧灵都可以跳舞，而是根据插过火把的数量和月相决定。
 */
public class LumineEntity extends PathfinderMob implements InventoryCarrier {
	private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
	private static final Ingredient DUPLICATION_ITEM = Ingredient.of(Items.GLOWSTONE_DUST);
	private final SimpleContainer inventory = new SimpleContainer(1);

	private int duplicationCooldown;
	private float holdingItemAnimationTicks;
	private float holdingItemAnimationTicks0;
	private float dancingAnimationTicks;
	private float spinningAnimationTicks;
	private float spinningAnimationTicks0;

	private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(LumineEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(LumineEntity.class, EntityDataSerializers.BOOLEAN);

	protected static final ImmutableList<SensorType<? extends Sensor<? super LumineEntity>>> SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_LIVING_ENTITIES,
			SensorType.NEAREST_PLAYERS,
			SensorType.HURT_BY,
			SensorType.NEAREST_ITEMS
	);
	protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
			MemoryModuleType.PATH,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
			MemoryModuleType.HURT_BY,
			ECMemoryModuleTypes.NEAREST_DARK_LOCATION.get(),
			MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
			MemoryModuleType.LIKED_PLAYER,
			MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
			ECMemoryModuleTypes.DARK_LOCATION_COOLDOWN_TICKS.get(),
			MemoryModuleType.IS_PANICKING
	);

	public LumineEntity(EntityType<? extends LumineEntity> type, Level level) {
		super(type, level);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override @NotNull
	protected Brain.Provider<LumineEntity> brainProvider() {
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}

	@Override @NotNull
	protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
		return LumineAi.makeBrain(this.brainProvider().makeBrain(dynamic));
	}

	@Override @NotNull @SuppressWarnings("unchecked")
	public Brain<LumineEntity> getBrain() {
		return (Brain<LumineEntity>)super.getBrain();
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.FLYING_SPEED, 0.1D).add(Attributes.MOVEMENT_SPEED, 0.1D).add(Attributes.ATTACK_DAMAGE, 0.5D).add(Attributes.FOLLOW_RANGE, 48.0D);
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
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_DANCING, false);
		this.entityData.define(DATA_CAN_DUPLICATE, true);
	}

	@Override
	public void travel(@NotNull Vec3 vec) {
		if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
			if (this.isInWater()) {
				this.moveRelative(0.02F, vec);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
			} else if (this.isInLava()) {
				this.moveRelative(0.02F, vec);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
			} else {
				this.moveRelative(this.getSpeed(), vec);
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().scale(0.91D));
			}
		}

		this.calculateEntityAnimation(this, false);
	}

	@Override
	protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.6F;
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float modifier, @NotNull DamageSource damageSource) {
		return false;
	}

	@Override
	public boolean hurt(DamageSource damageSource, float value) {
		Entity entity = damageSource.getEntity();
		if (entity instanceof Player player) {
			Optional<UUID> optional = this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (optional.isPresent() && player.getUUID().equals(optional.get())) {
				return false;
			}
		}

		return super.hurt(damageSource, value);
	}

	@Override
	protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
	}

	@Override
	protected void checkFallDamage(double fallDistance, boolean onGround, @NotNull BlockState blockState, @NotNull BlockPos blockPos) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.hasItemInSlot(EquipmentSlot.MAINHAND) ? ECSounds.LUMINE_AMBIENT_WITH_ITEM : ECSounds.LUMINE_AMBIENT_WITHOUT_ITEM;
	}

	@Override
	protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
		return ECSounds.LUMINE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ECSounds.LUMINE_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	protected void customServerAiStep() {
		this.level.getProfiler().push("lumineBrain");
		this.getBrain().tick((ServerLevel)this.level, this);
		this.level.getProfiler().pop();
		this.level.getProfiler().push("lumineActivityUpdate");
		LumineAi.updateActivity(this);
		this.level.getProfiler().pop();
		super.customServerAiStep();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level.isClientSide && this.isAlive() && this.tickCount % 20 == 0) {
			this.heal(1.0F);
		}

		if (this.isDancing()) {
			if(this.tickCount % 100 == 0 && !this.shouldDance()) {
				this.setDancing(false);
			}
		} else {
			if(this.tickCount % 100 == 0 && this.shouldDance()) {
				this.setDancing(true);
			}
		}

		this.updateDuplicationCooldown();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide) {
			this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
			if (this.hasTorchInHand()) {
				this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
			} else {
				this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
			}

			if (this.isDancing()) {
				++this.dancingAnimationTicks;
				this.spinningAnimationTicks0 = this.spinningAnimationTicks;
				if (this.isSpinning()) {
					++this.spinningAnimationTicks;
				} else {
					--this.spinningAnimationTicks;
				}

				this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
			} else {
				this.dancingAnimationTicks = 0.0F;
				this.spinningAnimationTicks = 0.0F;
				this.spinningAnimationTicks0 = 0.0F;
			}
		} else if(this.isPanicking()) {
			this.setDancing(false);
		}
	}

	@Override @NotNull
	protected InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
		ItemStack playerItem = player.getItemInHand(hand);
		ItemStack lumineItem = this.getItemInHand(InteractionHand.MAIN_HAND);
		if (this.isDancing() && this.isDuplicationItem(playerItem) && this.canDuplicate()) {
			this.duplicateLumine();
			this.level.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
			this.level.playSound(player, this, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2.0F, 1.0F);
			this.removeInteractionItem(player, playerItem);
			return InteractionResult.SUCCESS;
		}
		if (lumineItem.isEmpty() && playerItem.is(ECItemTags.TORCHES)) {
			ItemStack itemstack3 = playerItem.copy();
			itemstack3.setCount(1);
			this.setItemInHand(InteractionHand.MAIN_HAND, itemstack3);
			this.removeInteractionItem(player, playerItem);
			this.level.playSound(player, this, ECSounds.LUMINE_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
			this.getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
			return InteractionResult.SUCCESS;
		}
		if (!lumineItem.isEmpty() && hand == InteractionHand.MAIN_HAND && playerItem.isEmpty() &&
				this.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER).orElse(player.getUUID()).equals(player.getUUID())) {
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.level.playSound(player, this, ECSounds.LUMINE_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
			this.swing(InteractionHand.MAIN_HAND);

			for(ItemStack itemstack2 : this.getInventory().removeAllItems()) {
				BehaviorUtils.throwItem(this, itemstack2, player.position());
			}

			this.getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
			player.addItem(lumineItem);
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean canTakeItem(@NotNull ItemStack itemStack) {
		return false;
	}

	private boolean isOnPickupCooldown() {
		return this.getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
	}

	@Override
	public boolean canPickUpLoot() {
		return !this.isOnPickupCooldown() && this.hasTorchInHand();
	}

	public boolean hasTorchInHand() {
		return this.getItemInHand(InteractionHand.MAIN_HAND).is(ECItemTags.TORCHES);
	}

	@Override @NotNull
	public SimpleContainer getInventory() {
		return this.inventory;
	}

	@Override @NotNull
	protected Vec3i getPickupReach() {
		return ITEM_PICKUP_REACH;
	}

	@Override
	public boolean wantsToPickUp(@NotNull ItemStack itemStack) {
		ItemStack handItemStack = this.getItemInHand(InteractionHand.MAIN_HAND);
		return itemStack.is(ECItemTags.TORCHES) && handItemStack.sameItem(itemStack) && this.inventory.canAddItem(itemStack) && ForgeEventFactory.getMobGriefingEvent(this.level, this);
	}

	@Override
	protected void pickUpItem(@NotNull ItemEntity itemStack) {
		InventoryCarrier.pickUpItem(this, this, itemStack);
	}

	@Override
	public boolean isFlapping() {
		return !this.isOnGround();
	}

	public boolean isDancing() {
		return this.entityData.get(DATA_DANCING);
	}

	public boolean isPanicking() {
		return this.brain.getMemory(MemoryModuleType.IS_PANICKING).isPresent();
	}

	public void setDancing(boolean dancing) {
		if (!this.level.isClientSide && this.isEffectiveAi() && (!dancing || !this.isPanicking())) {
			this.entityData.set(DATA_DANCING, dancing);
		}
	}

	private boolean shouldDance() {
		return this.level.isNight() && this.level.getBrightness(LightLayer.BLOCK, this.blockPosition()) >= Mth.floor(14.25F - 4.0F * this.level.getMoonBrightness());
	}

	public float getHoldingItemAnimationProgress(float progress) {
		return Mth.lerp(progress, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F;
	}

	public boolean isSpinning() {
		float f = this.dancingAnimationTicks % 55.0F;
		return f < 15.0F;
	}

	public float getSpinningProgress(float progress) {
		return Mth.lerp(progress, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F;
	}

	@Override
	protected void dropEquipment() {
		super.dropEquipment();
		this.inventory.removeAllItems().forEach(this::spawnAtLocation);
		ItemStack handItem = this.getItemBySlot(EquipmentSlot.MAINHAND);
		if (!handItem.isEmpty() && !EnchantmentHelper.hasVanishingCurse(handItem)) {
			this.spawnAtLocation(handItem);
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		this.writeInventoryToTag(nbt);
		nbt.putInt("DuplicationCooldown", this.duplicationCooldown);
		nbt.putBoolean("CanDuplicate", this.canDuplicate());
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.readInventoryFromTag(nbt);
		this.duplicationCooldown = nbt.getInt("DuplicationCooldown");
		this.entityData.set(DATA_CAN_DUPLICATE, nbt.getBoolean("CanDuplicate"));
	}

	@Override
	protected boolean shouldStayCloseToLeashHolder() {
		return false;
	}

	private void updateDuplicationCooldown() {
		if (this.duplicationCooldown > 0L) {
			--this.duplicationCooldown;
		}

		if (!this.level.isClientSide() && this.duplicationCooldown == 0L && !this.canDuplicate()) {
			this.entityData.set(DATA_CAN_DUPLICATE, true);
		}
	}

	private boolean isDuplicationItem(ItemStack itemStack) {
		return DUPLICATION_ITEM.test(itemStack);
	}

	private void duplicateLumine() {
		LumineEntity lumine = ECEntities.LUMINE.create(this.level);
		if (lumine != null) {
			lumine.moveTo(this.position());
			lumine.setPersistenceRequired();
			lumine.resetDuplicationCooldown();
			this.resetDuplicationCooldown();
			this.level.addFreshEntity(lumine);
		}
	}

	private void resetDuplicationCooldown() {
		this.duplicationCooldown = 6000;
		this.entityData.set(DATA_CAN_DUPLICATE, false);
	}

	private boolean canDuplicate() {
		return this.entityData.get(DATA_CAN_DUPLICATE);
	}

	private void removeInteractionItem(Player player, ItemStack itemStack) {
		if (!player.getAbilities().instabuild) {
			itemStack.shrink(1);
		}
	}

	@Override @NotNull
	public Vec3 getLeashOffset() {
		return new Vec3(0.0D, (double)this.getEyeHeight() * 0.6D, (double)this.getBbWidth() * 0.1D);
	}

	@Override
	public double getMyRidingOffset() {
		return 0.4D;
	}

	@Override
	public void handleEntityEvent(byte event) {
		if (event == EntityEvent.IN_LOVE_HEARTS) {
			for(int i = 0; i < 3; ++i) {
				this.spawnHeartParticle();
			}
		} else {
			super.handleEntityEvent(event);
		}
	}

	private void spawnHeartParticle() {
		double d0 = this.random.nextGaussian() * 0.02D;
		double d1 = this.random.nextGaussian() * 0.02D;
		double d2 = this.random.nextGaussian() * 0.02D;
		this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
	}
}
