package com.hexagram2021.emeraldcraft.common.items.capabilities;

import com.hexagram2021.emeraldcraft.common.items.foods.FarciFoodItem;
import com.hexagram2021.emeraldcraft.common.register.ECCapabilities;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackFoodHandler extends ItemComponent {
    public final FarciFoodStorage foodStorage;

    public ItemStackFoodHandler(ItemStack itemStack) {
        super(itemStack, ECCapabilities.FOOD_CAPABILITY);
        this.foodStorage = new FarciFoodStorage(itemStack, (FarciFoodItem) itemStack.getItem());
    }

    @Override
    protected ListTag getList(String key, int type) {
        if (key.equals(FarciFoodStorage.TAG_FILLINGS)) {
            return this.foodStorage.serializeNBT();
        }
        return super.getList(key, type);
    }

    @Override
    protected void putList(String key, ListTag value) {
        super.putList(key, value);
        if (key.equals(FarciFoodStorage.TAG_FILLINGS)) {
            this.foodStorage.deserializeNBT(value);
        }
    }
}
