package com.hexagram2021.emeraldcraft.mixin.accessor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WoodType.class)
public interface WoodTypeAccess {
    @Invoker("<init>")
    static WoodType ec$initSignType(String name, BlockSetType setType) {
        throw new AssertionError();
    }

    @Invoker("<init>")
    static WoodType ec$initSignType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
        throw new AssertionError();
    }

    @Invoker("register")
    static WoodType ec$registerSignType(WoodType type) {
        throw new AssertionError();
    }
}