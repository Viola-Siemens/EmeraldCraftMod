package com.hexagram2021.emeraldcraft.common.fluids;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class ECFluidAttributes implements FluidVariantAttributeHandler {
    @Override
    public Component getName(FluidVariant fluidVariant) {
        return Component.translatable("fluid." + BuiltInRegistries.FLUID.getKey(fluidVariant.getFluid()).toLanguageKey());
    }

    @Override
    public Optional<SoundEvent> getFillSound(FluidVariant variant) {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    public Optional<SoundEvent> getEmptySound(FluidVariant variant) {
        return Optional.of(SoundEvents.BUCKET_EMPTY);
    }
}
