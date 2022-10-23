package com.hexagram2021.emeraldcraft.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class StickFoodItem extends Item {
	public StickFoodItem(Properties props) {
		super(props);
	}

	@Override @Nonnull
	public ItemStack finishUsingItem(@Nonnull ItemStack itemStack, @Nonnull World level, @Nonnull LivingEntity entity) {
		super.finishUsingItem(itemStack, level, entity);
		if(itemStack.isEmpty()) {
			return new ItemStack(Items.STICK);
		}
		if (entity instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)entity;
			if(!player.abilities.instabuild) {
				ItemStack itemstack = new ItemStack(Items.STICK);
				if (!player.inventory.add(itemstack)) {
					player.drop(itemstack, false);
				}
			}
		}

		return itemStack;
	}
}
