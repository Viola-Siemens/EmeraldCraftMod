package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PlayerHealable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(AbstractPiglin.class)
public class AbstractPiglinEntityMixin extends Monster implements PlayerHealable {
	@SuppressWarnings("WrongEntityDataParameterClass")
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BYTE);

	private UUID healedPlayer = Util.NIL_UUID;

	protected AbstractPiglinEntityMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	protected void defineFlagsData(CallbackInfo ci) {
		this.getEntityData().define(DATA_FLAGS_ID, (byte)0);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		nbt.putBoolean("PlayerHealed", this.isPlayerHealed());
		nbt.putUUID("HealedPlayer", this.healedPlayer);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		this.setPlayerHealed(nbt.getBoolean("PlayerHealed"));
		if(nbt.hasUUID("HealedPlayer")) {
			this.setHealedPlayer(nbt.getUUID("HealedPlayer"));
		}
	}

	@Override
	public boolean canAttack(LivingEntity livingEntity) {
		if(this.isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER && this.getHealedPlayer().equals(livingEntity.getUUID())) {
			return false;
		}
		return super.canAttack(livingEntity);
	}

	@Override
	public boolean isPlayerHealed() {
		return (this.getEntityData().get(DATA_FLAGS_ID) & 1) != 0;
	}

	@Override
	public void setPlayerHealed(boolean healed) {
		SynchedEntityData entityData = this.getEntityData();
		byte b0 = entityData.get(DATA_FLAGS_ID);
		if (healed) {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
		} else {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
		}
	}

	@Override
	public UUID getHealedPlayer() {
		return this.healedPlayer;
	}

	@Override
	public void setHealedPlayer(@Nullable UUID player) {
		this.healedPlayer = Objects.requireNonNullElse(player, Util.NIL_UUID);
	}
}
