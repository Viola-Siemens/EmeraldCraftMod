package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(AbstractPiglin.class)
public class AbstractPiglinEntityMixin implements PlayerHealable {
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BYTE);

	@NotNull
	private UUID healedPlayer = Util.NIL_UUID;

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	protected void defineFlagsData(CallbackInfo ci) {
		((AbstractPiglin) (Object) this).getEntityData().define(DATA_FLAGS_ID, (byte)0);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		nbt.putBoolean("PlayerHealed", this.isPlayerHealed());
		nbt.putUUID("HealedPlayer", this.healedPlayer);
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		if(nbt.contains("PlayerHealed", Tag.TAG_BYTE)) {
			this.setPlayerHealed(nbt.getBoolean("PlayerHealed"));
		}
		if(nbt.hasUUID("PlayerHealed")) {
			this.setHealedPlayer(nbt.getUUID("HealedPlayer"));
		}
	}

	@Override
	public boolean isPlayerHealed() {
		return (((AbstractPiglin) (Object) this).getEntityData().get(DATA_FLAGS_ID) & 1) != 0;
	}

	@Override
	public void setPlayerHealed(boolean healed) {
		SynchedEntityData entityData = ((AbstractPiglin) (Object) this).getEntityData();
		byte b0 = entityData.get(DATA_FLAGS_ID);
		if (healed) {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
		} else {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
		}
	}

	@Override @NotNull
	public UUID getHealedPlayer() {
		return this.healedPlayer;
	}

	@Override
	public void setHealedPlayer(@Nullable UUID player) {
		this.healedPlayer = Objects.requireNonNullElse(player, Util.NIL_UUID);
	}
}
