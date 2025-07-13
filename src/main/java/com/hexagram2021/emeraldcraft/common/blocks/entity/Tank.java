package com.hexagram2021.emeraldcraft.common.blocks.entity;


import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public interface Tank {
    FluidStack getFluidStack(int tank);

    void setFluidStack(int tank, FluidStack stack);

    int getTankSize();

    SingleVariantStorage<FluidVariant> getFluidStorage(int tank);

    SingleVariantStorage<ItemVariant> getItemStorage(@NotNull Direction direction);

    int getTanks();

    FluidStack getFluidInTank(int tank);
}
