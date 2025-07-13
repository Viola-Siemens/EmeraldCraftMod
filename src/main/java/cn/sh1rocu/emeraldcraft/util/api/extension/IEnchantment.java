package cn.sh1rocu.emeraldcraft.util.api.extension;

import net.minecraft.world.item.ItemStack;

public interface IEnchantment {
    default boolean canApplyAtEnchantingTable(ItemStack stack) {
        return true;
    }
}
