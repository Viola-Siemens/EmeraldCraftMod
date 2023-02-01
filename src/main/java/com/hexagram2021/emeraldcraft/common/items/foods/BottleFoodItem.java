package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BottleFoodItem extends Item {
	private final int drinkDuration;

	public BottleFoodItem(int drinkDuration, Properties props) {
		super(props);
		this.drinkDuration = drinkDuration;
	}

	@Override @NotNull
	public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
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
	public int getUseDuration(@NotNull ItemStack itemStack) {
		return drinkDuration;
	}

	@Override @NotNull
	public UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
		return UseAnim.DRINK;
	}

	@Override @NotNull
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override @NotNull
	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override @NotNull
	public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}
