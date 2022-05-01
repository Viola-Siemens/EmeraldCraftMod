package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.crafting.MineralTableMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MineralTableBlockEntity extends AbstractFurnaceBlockEntity {
	public MineralTableBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.MINERAL_TABLE.get(), pos, state, ECRecipes.MINERAL_TABLE_TYPE);
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.mineral_table");
	}

	@Override
	protected int getBurnDuration(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return itemStack.is(Items.BLAZE_POWDER) ? MineralTableRecipe.BURN_TIME * 20 : 0;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new MineralTableMenu(id, inventory, this, this.dataAccess);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		if (index == MineralTableMenu.RESULT_SLOT) {
			return false;
		}
		if (index != MineralTableMenu.FUEL_SLOT) {
			return true;
		}
		return itemStack.is(Items.BLAZE_POWDER);
	}
}
