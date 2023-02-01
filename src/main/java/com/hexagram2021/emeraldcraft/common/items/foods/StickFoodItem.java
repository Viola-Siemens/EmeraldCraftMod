package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class StickFoodItem extends Item {
	public StickFoodItem(Properties props) {
		super(props);
	}

	@Override @NotNull
	public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
		super.finishUsingItem(itemStack, level, entity);
		if(itemStack.isEmpty()) {
			return new ItemStack(Items.STICK);
		}
		if (entity instanceof Player player && !player.getAbilities().instabuild) {
			ItemStack itemstack = new ItemStack(Items.STICK);
			if (!player.getInventory().add(itemstack)) {
				player.drop(itemstack, false);
			}
		}

		return itemStack;
	}
}
