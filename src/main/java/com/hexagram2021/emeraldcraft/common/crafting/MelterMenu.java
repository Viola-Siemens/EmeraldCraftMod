package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
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
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.isFuel;

public class MelterMenu extends AbstractContainerMenu {
	public static final int INGREDIENT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int RESULT_INPUT_SLOT = 2;
	public static final int RESULT_OUTPUT_SLOT = 3;
	public static final int SLOT_COUNT = 4;
	public static final int INV_SLOT_START = 4;
	public static final int INV_SLOT_END = 31;
	public static final int USE_ROW_SLOT_START = 31;
	public static final int USE_ROW_SLOT_END = 40;
	public static final int DATA_COUNT = 6;
	private final Container melter;
	private final ContainerData melterData;
	private final Slot ingredientSlot;
	private final Slot resultInputSlot;
	protected final Level level;

	public MelterMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public MelterMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.MELTER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.melter = container;
		this.melterData = data;
		this.level = inventory.player.level;
		this.ingredientSlot = this.addSlot(new MelterMenu.IngredientSlot(this, container, INGREDIENT_SLOT, 41, 17));
		this.addSlot(new MelterMenu.MelterFuelSlot(container, FUEL_SLOT, 41, 53));

		this.resultInputSlot = this.addSlot(new Slot(container, RESULT_INPUT_SLOT, 132, 18) {
			@Override
			public boolean mayPlace(@NotNull ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || isFluidBucket(itemStack);
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, RESULT_OUTPUT_SLOT, 132, 52) {
			@Override
			public boolean mayPlace(@NotNull ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || isFluidBucket(itemStack);
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

	public static boolean isFluidBucket(ItemStack itemStack) {
		return	itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.LAVA_BUCKET) ||
				itemStack.is(ECItems.MELTED_EMERALD_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_IRON_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_GOLD_BUCKET.get()) ||
				itemStack.is(ECItems.MELTED_COPPER_BUCKET.get()) ||
				FluidTypes.isExtraFluidBucket(itemStack);
	}

	public boolean isLit() {
		return this.melterData.get(0) > 0;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.melter.stillValid(player);
	}

	@Override @NotNull
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index == RESULT_OUTPUT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index == RESULT_INPUT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index != FUEL_SLOT && index != INGREDIENT_SLOT) {
				if(resultInputSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, RESULT_INPUT_SLOT, RESULT_OUTPUT_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (ingredientSlot.mayPlace(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT, FUEL_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isFuel(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, FUEL_SLOT, RESULT_INPUT_SLOT, false)) {
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
		int i = this.melterData.get(2);
		int j = this.melterData.get(3);
		return j != 0 && i != 0 ? (i * 24 / j) : 0;
	}

	public int getLitProgress() {
		int i = this.melterData.get(1);
		if (i == 0) {
			i = MelterRecipe.MELTING_TIME;
		}

		return this.melterData.get(0) * 13 / i;
	}

	public int getFluidTypeIndex() {
		return this.melterData.get(4);
	}

	public FluidType getFluidType() {
		return FluidTypes.getFluidTypeWithID(this.melterData.get(4));
	}

	public int getFluidLevel() {
		return this.melterData.get(5);
	}

	static class IngredientSlot extends Slot {
		private final MelterMenu menu;
		public IngredientSlot(MelterMenu menu, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		@Override
		public boolean mayPlace(@NotNull ItemStack itemStack) {
			return menu.level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE.get(), new SimpleContainer(itemStack), menu.level).isPresent();
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}
	}

	static class MelterFuelSlot extends Slot {

		public MelterFuelSlot(Container container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		public boolean mayPlace(@NotNull ItemStack itemStack) {
			return isFuel(itemStack);
		}

		public int getMaxStackSize(@NotNull ItemStack itemStack) {
			return super.getMaxStackSize(itemStack);
		}
	}
}
