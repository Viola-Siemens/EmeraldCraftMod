package com.hexagram2021.emeraldcraft.mixin.accessor;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.impl.transfer.fluid.EmptyBucketStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("UnstableApiUsage")
@Mixin(remap = false, value = EmptyBucketStorage.class)
public interface EmptyBucketStorageAccess {
    @Accessor("context")
    ContainerItemContext getContext();
}
