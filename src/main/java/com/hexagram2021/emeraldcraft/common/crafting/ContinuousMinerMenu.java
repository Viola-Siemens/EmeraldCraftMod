package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ContinuousMinerMenu extends AbstractContainerMenu {
	public static final int INPUT_SLOT = 0;
	public static final int RESULT_SLOT = 1;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;
	public static final int SLOT_COUNT = 2;
	public static final int DATA_COUNT = 2;

	private final Container continuousMiner;
	private final ContainerData continuousMinerData;
	protected final Level level;
	final Slot inputSlot;

	public ContinuousMinerMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public ContinuousMinerMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.continuousMiner = container;
		this.continuousMinerData = data;
		this.level = inventory.player.level;

		this.inputSlot = this.addSlot(new Slot(container, INPUT_SLOT, 43, 19) {
			@Override
			public boolean mayPlace(@NotNull ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || itemStack.is(ECItems.MELTED_EMERALD_BUCKET.get());
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, RESULT_SLOT, 43, 53) {
			@Override
			public boolean mayPlace(@NotNull ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || itemStack.is(ECItems.MELTED_EMERALD_BUCKET.get());
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addDataSlots(data);

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.continuousMiner.stillValid(player);
	}

	@Override @NotNull
	public MenuType<?> getType() {
		return ECContainerTypes.CONTINUOUS_MINER_MENU.get();
	}

	@Override @NotNull
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == RESULT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index == INPUT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
				if(this.inputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INPUT_SLOT, RESULT_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END) {
				if(this.inputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INPUT_SLOT, RESULT_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}

			slot.setChanged();
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
			this.broadcastChanges();
		}

		return itemstack;
	}

	public int getFluidLevel() {
		return this.continuousMinerData.get(ContinuousMinerBlockEntity.DATA_FLUID);
	}
}
