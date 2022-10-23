package com.hexagram2021.emeraldcraft.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BottleFoodItem extends Item {
	private final int drinkDuration;

	public BottleFoodItem(int drinkDuration, Properties props) {
		super(props);
		this.drinkDuration = drinkDuration;
	}

	@Override @Nonnull
	public ItemStack finishUsingItem(@Nonnull ItemStack itemStack, @Nonnull World level, @Nonnull LivingEntity entity) {
		super.finishUsingItem(itemStack, level, entity);

		this.additionalEffects(level, entity);

		if (itemStack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)entity;
				if(!player.abilities.instabuild) {
					ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
					if (!player.inventory.add(itemstack)) {
						player.drop(itemstack, false);
					}
				}
			}

			return itemStack;
		}
	}

	protected void additionalEffects(World level, LivingEntity entity) {
	}

	@Override
	public int getUseDuration(@Nonnull ItemStack itemStack) {
		return drinkDuration;
	}

	@Override @Nonnull
	public UseAction getUseAnimation(@Nonnull ItemStack itemStack) {
		return UseAction.DRINK;
	}

	@Override @Nonnull
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override @Nonnull
	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override @Nonnull
	public ActionResult<ItemStack> use(@Nonnull World level, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
		return DrinkHelper.useDrink(level, player, hand);
	}
}
