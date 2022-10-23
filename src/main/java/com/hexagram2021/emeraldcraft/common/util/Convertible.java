package com.hexagram2021.emeraldcraft.common.util;

import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Convertible {
	int getConversionProgress();
	int getConversionRemainTime();
	void setConversionRemainTime(int time);
	void decreaseConversionRemainTime(int dec);
	void startConverting(@Nullable UUID player, int time);
	void finishConversion(ServerWorld level);
	boolean isConverting();

	DataParameter<Boolean> DATA_PIGLIN_CONVERTING_ID = EntityDataManager.defineId(ZombifiedPiglinEntity.class, DataSerializers.BOOLEAN);
	DataParameter<Boolean> DATA_PHANTOM_CONVERTING_ID = EntityDataManager.defineId(PhantomEntity.class, DataSerializers.BOOLEAN);
}
