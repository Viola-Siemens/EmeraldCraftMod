package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChorusFlowerEggdropSoupItem extends BowlFoodItem {
	public ChorusFlowerEggdropSoupItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
		if(!level.isClientSide) {
			entity.removeEffect(MobEffects.HUNGER);
			entity.removeEffect(MobEffects.POISON);
			entity.removeEffect(MobEffects.WITHER);
		}

		return super.finishUsingItem(itemStack, level, entity);
	}
}
