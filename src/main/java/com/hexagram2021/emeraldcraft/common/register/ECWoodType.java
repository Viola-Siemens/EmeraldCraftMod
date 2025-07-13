package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.mixin.accessor.WoodTypeAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

@SuppressWarnings("SameParameterValue")
public class ECWoodType {
    public static final WoodType GINKGO = register("ec_ginkgo", ECBlockSetTypes.GINKGO);
    public static final WoodType PALM = register("ec_palm", ECBlockSetTypes.PALM);
    public static final WoodType PEACH = register("ec_peach", ECBlockSetTypes.PEACH);
    public static final WoodType PURPURACEUS = register(
            "ec_purpuraceus", ECBlockSetTypes.PURPURACEUS,
            SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN,
            SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN
    );

    private static WoodType register(String name, BlockSetType setType) {
        return WoodTypeAccess.ec$registerSignType(WoodTypeAccess.ec$initSignType(name, setType));
    }

    private static WoodType register(
            String name,
            BlockSetType setType,
            SoundType soundType,
            SoundType hangingSoundType,
            SoundEvent closeSound,
            SoundEvent openSound
    ) {
        return WoodTypeAccess.ec$registerSignType(WoodTypeAccess.ec$initSignType(name, setType, soundType, hangingSoundType, closeSound, openSound));
    }

    public static void init() {
    }
}
