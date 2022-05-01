package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GlassKilnFuelSlot extends Slot {
	private final GlassKilnMenu menu;

	public GlassKilnFuelSlot(GlassKilnMenu menu, Container container, int slot, int x, int y) {
		super(container, slot, x, y);
		this.menu = menu;
	}

	public boolean mayPlace(ItemStack itemStack) {
		return this.menu.isFuel(itemStack) || isBucket(itemStack);
	}

	public int getMaxStackSize(ItemStack itemStack) {
		return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
	}

	public static boolean isBucket(ItemStack p_39530_) {
		return p_39530_.is(Items.BUCKET);
	}
}
