package com.hexagram2021.emeraldcraft.common.entities.mobs;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Convertible {
	int getConversionProgress();
	int getConversionRemainTime();
	void setConversionRemainTime(int time);
	void decreaseConversionRemainTime(int dec);
	void startConverting(@Nullable UUID player, int time);
	void finishConversion(ServerLevel level);
	boolean isConverting();

	EntityDataAccessor<Boolean> DATA_PIGLIN_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedPiglin.class, EntityDataSerializers.BOOLEAN);
	EntityDataAccessor<Boolean> DATA_PHANTOM_CONVERTING_ID = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.BOOLEAN);
}
