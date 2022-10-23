package com.hexagram2021.emeraldcraft.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SoupItem;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ChorusFlowerEggdropSoupItem extends SoupItem {
	public ChorusFlowerEggdropSoupItem(Properties props) {
		super(props);
	}

	@Override @Nonnull
	public ItemStack finishUsingItem(@Nonnull ItemStack itemStack, @Nonnull World level, @Nonnull LivingEntity entity) {
		if(!level.isClientSide) {
			entity.removeEffect(Effects.HUNGER);
			entity.removeEffect(Effects.POISON);
			entity.removeEffect(Effects.WITHER);
		}

		return super.finishUsingItem(itemStack, level, entity);
	}
}
