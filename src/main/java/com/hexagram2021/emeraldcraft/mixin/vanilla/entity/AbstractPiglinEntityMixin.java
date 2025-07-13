package com.hexagram2021.emeraldcraft.mixin.vanilla.entity;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(AbstractPiglin.class)
public class AbstractPiglinEntityMixin extends Monster implements PlayerHealable {
    @SuppressWarnings("WrongEntityDataParameterClass")
    @Unique
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BYTE);

    @Unique
    private UUID emeraldcraft$healedPlayer = Util.NIL_UUID;

    protected AbstractPiglinEntityMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    protected void emeraldcraft$defineFlagsData(CallbackInfo ci) {
        this.getEntityData().define(DATA_FLAGS_ID, (byte) 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    public void emeraldcraft$addPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("PlayerHealed", this.emeraldcraft$isPlayerHealed());
        nbt.putUUID("HealedPlayer", this.emeraldcraft$healedPlayer);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    public void emeraldcraft$readPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
        this.emeraldcraft$setPlayerHealed(nbt.getBoolean("PlayerHealed"));
        if (nbt.hasUUID("HealedPlayer")) {
            this.emeraldcraft$setHealedPlayer(nbt.getUUID("HealedPlayer"));
        }
    }

    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        if (this.emeraldcraft$isPlayerHealed() && livingEntity.getType() == EntityType.PLAYER && this.emeraldcraft$getHealedPlayer().equals(livingEntity.getUUID())) {
            return false;
        }
        return super.canAttack(livingEntity);
    }

    @Override
    public boolean emeraldcraft$isPlayerHealed() {
        return (this.getEntityData().get(DATA_FLAGS_ID) & 1) != 0;
    }

    @Override
    public void emeraldcraft$setPlayerHealed(boolean healed) {
        SynchedEntityData entityData = this.getEntityData();
        byte b0 = entityData.get(DATA_FLAGS_ID);
        if (healed) {
            entityData.set(DATA_FLAGS_ID, (byte) (b0 | 1));
        } else {
            entityData.set(DATA_FLAGS_ID, (byte) (b0 & -2));
        }
    }

    @Override
    public UUID emeraldcraft$getHealedPlayer() {
        return this.emeraldcraft$healedPlayer;
    }

    @Override
    public void emeraldcraft$setHealedPlayer(@Nullable UUID player) {
        this.emeraldcraft$healedPlayer = Objects.requireNonNullElse(player, Util.NIL_UUID);
    }
}
