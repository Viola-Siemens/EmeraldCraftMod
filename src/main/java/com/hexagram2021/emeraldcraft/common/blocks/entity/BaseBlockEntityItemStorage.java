package com.hexagram2021.emeraldcraft.common.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;

@SuppressWarnings("UnstableApiUsage")
public class BaseBlockEntityItemStorage extends SingleItemStorage {
    @Override
    protected long getCapacity(ItemVariant itemVariant) {
        return itemVariant.getItem().getMaxStackSize();
    }
}