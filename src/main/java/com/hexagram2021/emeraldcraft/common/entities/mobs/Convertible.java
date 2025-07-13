package com.hexagram2021.emeraldcraft.common.entities.mobs;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Convertible {
    int emeraldcraft$getConversionProgress();

    int emeraldcraft$getConversionRemainTime();

    void emeraldcraft$setConversionRemainTime(int time);

    void emeraldcraft$decreaseConversionRemainTime(int dec);

    void emeraldcraft$startConverting(@Nullable UUID player, int time);

    void emeraldcraft$finishConversion(ServerLevel level);

    boolean emeraldcraft$isConverting();

    @SuppressWarnings("NotNullFieldNotInitialized")
    final class Ids {
        public static EntityDataAccessor<Boolean> DATA_PIGLIN_CONVERTING_ID;
        public static EntityDataAccessor<Boolean> DATA_PHANTOM_CONVERTING_ID;

        private Ids() {
        }
    }
}
