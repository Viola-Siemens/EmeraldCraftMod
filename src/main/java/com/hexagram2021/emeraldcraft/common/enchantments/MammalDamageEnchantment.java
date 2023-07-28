package com.hexagram2021.emeraldcraft.common.enchantments;

import com.hexagram2021.emeraldcraft.common.register.ECMobTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MammalDamageEnchantment extends Enchantment {
	public MammalDamageEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
		super(rarity, EnchantmentCategory.WEAPON, equipmentSlots);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinCost(int lvl) {
		return 5 + 8 * (lvl - 1);
	}

	@Override
	public int getMaxCost(int lvl) {
		return super.getMinCost(lvl) + 20;
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && !(other instanceof DamageEnchantment);
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getDamageBonus(int level, MobType mobType) {
		if(isMammal(mobType)) {
			return (float)level * 2.0F;
		}
		return super.getDamageBonus(level, mobType);
	}

	@Override
	public boolean canEnchant(ItemStack itemStack) {
		return itemStack.getItem() instanceof AxeItem || super.canEnchant(itemStack);
	}

	public static boolean isMammal(MobType mobType) {
		return mobType == MobType.ILLAGER || mobType == ECMobTypes.MAMMAL;
	}
}
