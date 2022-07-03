package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class IceMakerMenu extends AbstractContainerMenu {
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
	private final Container iceMaker;
	private final ContainerData iceMakerData;
	private final Slot ingredientInputSlot;
	private final Slot condensateSlot;
	protected final Level level;

	public IceMakerMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public IceMakerMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.ICE_MAKER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.iceMaker = container;
		this.iceMakerData = data;
		this.level = inventory.player.level;
		this.addSlot(new IceMakerMenu.IceMakerResultSlot(inventory.player, container, RESULT_SLOT, 134, 35));
		this.ingredientInputSlot = this.addSlot(new Slot(container, INGREDIENT_INPUT_SLOT, 50, 18) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || isFluidBucket(itemStack);
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, INGREDIENT_OUTPUT_SLOT, 50, 52) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || isFluidBucket(itemStack);
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.condensateSlot = this.addSlot(new Slot(container, CONDENSATE_SLOT, 16, 26) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.WATER_BUCKET);
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
		return	itemStack.is(Items.WATER_BUCKET) ||
				itemStack.is(Items.LAVA_BUCKET) ||
				itemStack.is(ECItems.MELTED_EMERALD_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_IRON_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_GOLD_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_COPPER_BUCKET.get());
	}

	@Override
	public boolean stillValid(Player player) {
		return this.iceMaker.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
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
		return FluidType.FLUID_TYPES[this.iceMakerData.get(0)];
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
		private final Player player;
		private int removeCount;

		public IceMakerResultSlot(Player player, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.player = player;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack) {
			return false;
		}

		@Override
		public ItemStack remove(int count) {
			if (this.hasItem()) {
				this.removeCount += Math.min(count, this.getItem().getCount());
			}

			return super.remove(count);
		}

		@Override
		public void onTake(Player player, ItemStack itemStack) {
			this.checkTakeAchievements(itemStack);
			super.onTake(player, itemStack);
		}

		@Override
		protected void onQuickCraft(ItemStack itemStack, int count) {
			this.removeCount += count;
			this.checkTakeAchievements(itemStack);
		}

		@Override
		protected void checkTakeAchievements(ItemStack itemStack) {
			itemStack.onCraftedBy(this.player.level, this.player, this.removeCount);

			this.removeCount = 0;
			net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, itemStack);
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}
}
