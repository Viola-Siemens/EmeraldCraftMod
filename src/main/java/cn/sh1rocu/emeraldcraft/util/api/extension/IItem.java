package cn.sh1rocu.emeraldcraft.util.api.extension;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IItem {
    default FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return stack.getItem().getFoodProperties();
    }
}
