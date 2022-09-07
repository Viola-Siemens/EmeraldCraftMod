package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class IceMakerMenu extends Container {
	public static final int INGREDIENT_INPUT_SLOT = 0;
	public static final int INGREDIENT_OUTPUT_SLOT = 1;
	public static final int CONDENSATE_SLOT = 2;
	public static final int RESULT_SLOT = 3;
	public static final int SLOT_COUNT = 4;
	public static final int INV_SLOT_START = 4;
	public static final int INV_SLOT_END = 31;
	public static final int USE_ROW_SLOT_START = 31;
	public static final int USE_ROW_SLOT_END = 40;
	public static final int DATA_COUNT = 5;
	private final IInventory iceMaker;
	private final IIntArray iceMakerData;
	private final Slot ingredientInputSlot;
	private final Slot condensateSlot;
	protected final World level;

	public IceMakerMenu(int id, PlayerInventory inventory) {
		this(id, inventory, new Inventory(SLOT_COUNT), new IntArray(DATA_COUNT));
	}

	public IceMakerMenu(int id, PlayerInventory inventory, IInventory container, IIntArray data) {
		super(ECContainerTypes.ICE_MAKER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.iceMaker = container;
		this.iceMakerData = data;
		this.level = inventory.player.level;
		this.addSlot(new IceMakerMenu.IceMakerResultSlot(inventory.player, container, RESULT_SLOT, 134, 35));
		this.ingredientInputSlot = this.addSlot(new Slot(container, INGREDIENT_INPUT_SLOT, 50, 18) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack itemStack) {
				return itemStack.getItem() == Items.BUCKET || isFluidBucket(itemStack);
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, INGREDIENT_OUTPUT_SLOT, 50, 52) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack itemStack) {
				return itemStack.getItem() == Items.BUCKET || isFluidBucket(itemStack);
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.condensateSlot = this.addSlot(new Slot(container, CONDENSATE_SLOT, 16, 26) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack itemStack) {
				return itemStack.getItem() == Items.WATER_BUCKET;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
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

	public static boolean isFluidBucket(ItemStack itemStack) {
		Item item = itemStack.getItem();
		return	item == Items.WATER_BUCKET ||
				item == Items.LAVA_BUCKET ||
				item == ECItems.MELTED_EMERALD_BUCKET.get() ||
				item == ECItems.MELTED_IRON_BUCKET.get() ||
				item == ECItems.MELTED_GOLD_BUCKET.get() ||
				FluidTypes.isExtraFluidBucket(item);
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return this.iceMaker.stillValid(player);
	}

	@Override @Nonnull
	public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == RESULT_SLOT || index == CONDENSATE_SLOT || index == INGREDIENT_OUTPUT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index != INGREDIENT_INPUT_SLOT) {
				if (ingredientInputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INGREDIENT_INPUT_SLOT, INGREDIENT_OUTPUT_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (condensateSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, CONDENSATE_SLOT, RESULT_SLOT, false)) {
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

	public int getFluidTypeIndex() {
		return this.iceMakerData.get(0);
	}

	public FluidType getFluidType() {
		return FluidTypes.getFluidTypeWithID(this.iceMakerData.get(0));
	}

	public int getIngredientFluidLevel() {
		return this.iceMakerData.get(1);
	}

	public int getCondensateFluidLevel() {
		return this.iceMakerData.get(4);
	}

	public int getFreezeProgress() {
		int i = this.iceMakerData.get(2);
		int j = this.iceMakerData.get(3);
		return j != 0 && i != 0 ? (i * 24 / j) : 0;
	}

	static class IceMakerResultSlot extends Slot {
		private final PlayerEntity player;
		private int removeCount;

		public IceMakerResultSlot(PlayerEntity player, IInventory container, int slot, int x, int y) {
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

			this.removeCount = 0;
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, itemStack);
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}
}
