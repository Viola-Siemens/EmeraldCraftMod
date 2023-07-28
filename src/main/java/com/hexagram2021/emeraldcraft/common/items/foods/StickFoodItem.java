package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StickFoodItem extends Item {
	public StickFoodItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
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
