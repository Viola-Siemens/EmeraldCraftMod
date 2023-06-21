package com.hexagram2021.emeraldcraft.common.crafting.menu;

import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;

public class GlassKilnMenu extends AbstractFurnaceMenu {

	public GlassKilnMenu(int id, Inventory inventory) {
		super(ECContainerTypes.GLASS_KILN_MENU.get(), ECRecipes.GLASS_KILN_TYPE.get(), ECRecipes.GLASS_KILN, id, inventory);
	}

	public GlassKilnMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.GLASS_KILN_MENU.get(), ECRecipes.GLASS_KILN_TYPE.get(), ECRecipes.GLASS_KILN, id, inventory, container, data);
	}
}
