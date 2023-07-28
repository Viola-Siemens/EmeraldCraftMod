package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.crafting.menu.GlassKilnMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlassKilnBlockEntity extends AbstractFurnaceBlockEntity {

	public GlassKilnBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.GLASS_KILN.get(), pos, state, ECRecipes.GLASS_KILN_TYPE.get());
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.glass_kiln");
	}

	@Override
	protected int getBurnDuration(ItemStack itemStack) {
		return super.getBurnDuration(itemStack) / 2;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new GlassKilnMenu(id, inventory, this, this.dataAccess);
	}
}
