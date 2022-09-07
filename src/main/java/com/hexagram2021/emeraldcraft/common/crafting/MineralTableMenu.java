package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.MineralTableBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MineralTableMenu extends Container {
	public static final int INGREDIENT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int RESULT_SLOT = 2;
	public static final int SLOT_COUNT = 3;
	public static final int INV_SLOT_START = 3;
	public static final int INV_SLOT_END = 30;
	public static final int USE_ROW_SLOT_START = 30;
	public static final int USE_ROW_SLOT_END = 39;
	public static final int DATA_COUNT = 4;
	private final IInventory mineralTable;
	private final IIntArray mineralTableData;
	private final Slot ingredientSlot;
	protected final World level;

	public MineralTableMenu(int id, PlayerInventory inventory) {
		this(id, inventory, new Inventory(SLOT_COUNT), new IntArray(DATA_COUNT));
	}

	public MineralTableMenu(int id, PlayerInventory inventory, IInventory container, IIntArray data) {
		super(ECContainerTypes.MINERAL_TABLE_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.mineralTable = container;
		this.mineralTableData = data;
		this.level = inventory.player.level;
		this.addSlot(new MineralTableMenu.MineralResultSlot(inventory.player, container, RESULT_SLOT, 79, 58));
		this.ingredientSlot = this.addSlot(new MineralTableMenu.IngredientSlot(this, container, INGREDIENT_SLOT, 79, 17));
		this.addSlot(new MineralTableMenu.FuelSlot(container, FUEL_SLOT, 17, 17));
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
	public boolean stillValid(PlayerEntity player) {
		return this.mineralTable.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
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
			} else if (index != FUEL_SLOT && index != INGREDIENT_SLOT) {
				if (ingredientSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT, FUEL_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (FuelSlot.mayPlaceItem(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, FUEL_SLOT, RESULT_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
					if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END &&
						!this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	public int getBurnProgress() {
		int i = this.mineralTableData.get(2);
		int j = this.mineralTableData.get(3);
		return j != 0 && i != 0 ? ((j - i - 1) * 400 / j) : 0;
	}

	public int getLitProgress() {
		int i = this.mineralTableData.get(1);
		if (i == 0) {
			i = MineralTableRecipe.BURN_TIME;
		}

		return this.mineralTableData.get(0) * 20 / i;
	}

	static class FuelSlot extends Slot {
		public FuelSlot(IInventory container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack itemStack) {
			return mayPlaceItem(itemStack);
		}

		public static boolean mayPlaceItem(ItemStack itemStack) {
			return itemStack.getItem() == Items.BLAZE_POWDER;
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}

	static class IngredientSlot extends Slot {
		private final MineralTableMenu menu;
		public IngredientSlot(MineralTableMenu menu, IInventory container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack itemStack) {
			return menu.level.getRecipeManager().getRecipeFor(ECRecipes.MINERAL_TABLE_TYPE, new Inventory(itemStack), menu.level).isPresent();
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}

	static class MineralResultSlot extends Slot {
		private final PlayerEntity player;
		private int removeCount;

		public MineralResultSlot(PlayerEntity player, IInventory container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.player = player;
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack itemStack) {
			return false;
		}

		@Override @Nonnull
		public ItemStack remove(int count) {
			if (this.hasItem()) {
				this.removeCount += Math.min(count, this.getItem().getCount());
			}

			return super.remove(count);
		}

		@Override @Nonnull
		public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack itemStack) {
			this.checkTakeAchievements(itemStack);
			return super.onTake(player, itemStack);
		}

		@Override
		protected void onQuickCraft(@Nonnull ItemStack itemStack, int count) {
			this.removeCount += count;
			this.checkTakeAchievements(itemStack);
		}

		@Override
		protected void checkTakeAchievements(ItemStack itemStack) {
			itemStack.onCraftedBy(this.player.level, this.player, this.removeCount);
			if (this.player instanceof ServerPlayerEntity && this.container instanceof MineralTableBlockEntity) {
				((MineralTableBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayerEntity)this.player);
			}

			this.removeCount = 0;
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, itemStack);
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}
}
