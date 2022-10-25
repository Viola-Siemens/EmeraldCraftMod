package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPiglin.class)
public class AbstractPiglinEntityMixin implements PlayerHealable {
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BYTE);

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	protected void defineFlagsData(CallbackInfo ci) {
		((AbstractPiglin) (Object) this).getEntityData().define(DATA_FLAGS_ID, (byte)0);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		nbt.putBoolean("PlayerHealed", this.isPlayerHealed());
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readPlayerHealed(CompoundTag nbt, CallbackInfo ci) {
		this.setPlayerHealed(nbt.getBoolean("PlayerHealed"));
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
}
