package com.hexagram2021.emeraldcraft.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class VeinMiningEnchantment extends Enchantment {
	public VeinMiningEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
		super(rarity, EnchantmentCategory.DIGGER, equipmentSlots);
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinCost(int lvl) {
		return 1 + 10 * (lvl - 1);
	}

	@Override
	public int getMaxCost(int lvl) {
		return super.getMinCost(lvl) + 50;
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && other != Enchantments.BLOCK_EFFICIENCY;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.getItem() instanceof PickaxeItem && super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean canEnchant(ItemStack itemStack) {
		return canWorkWhenHolding(itemStack) || super.canEnchant(itemStack);
	}

	public static boolean canWorkWhenHolding(ItemStack itemStack) {
		return itemStack.getItem() instanceof ShearsItem || itemStack.getItem() instanceof DiggerItem;
	}
}
