package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.common.util.PlayerHealable;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPiglinEntity.class)
public class AbstractPiglinEntityMixin implements PlayerHealable {
	private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(AbstractPiglinEntity.class, DataSerializers.BYTE);

	@Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
	protected void defineFlagsData(CallbackInfo ci) {
		((AbstractPiglinEntity) (Object) this).getEntityData().define(DATA_FLAGS_ID, (byte)0);
	}

	@Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
	public void addPlayerHealed(CompoundNBT nbt, CallbackInfo ci) {
		nbt.putBoolean("PlayerHealed", this.isPlayerHealed());
	}

	@Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
	public void readPlayerHealed(CompoundNBT nbt, CallbackInfo ci) {
		this.setPlayerHealed(nbt.getBoolean("PlayerHealed"));
	}

	@Override
	public boolean isPlayerHealed() {
		return (((AbstractPiglinEntity) (Object) this).getEntityData().get(DATA_FLAGS_ID) & 1) != 0;
	}

	@Override
	public void setPlayerHealed(boolean healed) {
		EntityDataManager entityData = ((AbstractPiglinEntity) (Object) this).getEntityData();
		byte b0 = entityData.get(DATA_FLAGS_ID);
		if (healed) {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
		} else {
			entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
		}
	}
}
