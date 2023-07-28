package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BottleFoodItem extends Item {
	private final int drinkDuration;

	public BottleFoodItem(int drinkDuration, Properties props) {
		super(props);
		this.drinkDuration = drinkDuration;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
		super.finishUsingItem(itemStack, level, entity);

		this.additionalEffects(level, entity);

		if (itemStack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (entity instanceof Player player && !player.getAbilities().instabuild) {
				ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
				if (!player.getInventory().add(itemstack)) {
					player.drop(itemstack, false);
				}
			}

			return itemStack;
		}
	}

	protected void additionalEffects(Level level, LivingEntity entity) {
	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return drinkDuration;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.DRINK;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}
