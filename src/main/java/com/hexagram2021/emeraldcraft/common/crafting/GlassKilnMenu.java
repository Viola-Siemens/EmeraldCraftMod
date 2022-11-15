package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.GlassKilnBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public class GlassKilnMenu extends RecipeBookMenu<Container> {
	private final Container container;
	private final ContainerData data;
	protected final Level level;

	public static final int INGREDIENT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int RESULT_SLOT = 2;
	private static final int INV_SLOT_START = 3;
	private static final int INV_SLOT_END = 30;
	private static final int USE_ROW_SLOT_START = 30;
	private static final int USE_ROW_SLOT_END = 39;
	public static final int SLOT_COUNT = 3;
	public static final int DATA_COUNT = 4;

	public GlassKilnMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public GlassKilnMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.GLASS_KILN_MENU.get(), id);
		this.container = container;
		this.data = data;
		this.level = inventory.player.level;

		this.addSlot(new Slot(this.container, INGREDIENT_SLOT, 56, 17));
		this.addSlot(new GlassKilnMenu.GlassKilnFuelSlot(this, this.container, FUEL_SLOT, 56, 53));
		this.addSlot(new GlassKilnMenu.GlassKilnResultSlot(inventory.player, this.container, RESULT_SLOT, 116, 35));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}

		this.addDataSlots(this.data);
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
			} else if (index != FUEL_SLOT && index != INGREDIENT_SLOT) {
				if (this.canSmelt(itemstack1)) {
					if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT, FUEL_SLOT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(itemstack1)) {
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

	protected boolean canSmelt(ItemStack itemStack) {
		return this.level.getRecipeManager().getRecipeFor(ECRecipes.GLASS_KILN_TYPE, new SimpleContainer(itemStack), this.level).isPresent();
	}

	public boolean isFuel(ItemStack itemStack) {
		return ForgeHooks.getBurnTime(itemStack, ECRecipes.GLASS_KILN_TYPE) > 0;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public void fillCraftSlotsStackedContents(@NotNull StackedContents contents) {
		if (this.container instanceof StackedContentsCompatible) {
			((StackedContentsCompatible)this.container).fillStackedContents(contents);
		}
	}

	@Override
	public void clearCraftingContent() {
		this.getSlot(INGREDIENT_SLOT).set(ItemStack.EMPTY);
		this.getSlot(RESULT_SLOT).set(ItemStack.EMPTY);
	}

	public int getBurnProgress() {
		int i = this.data.get(2);
		int j = this.data.get(3);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public int getLitProgress() {
		int i = this.data.get(1);
		if (i == 0) {
			i = 200;
		}

		return this.data.get(0) * 13 / i;
	}

	public boolean isLit() {
		return this.data.get(0) > 0;
	}

	@Override
	public boolean recipeMatches(Recipe<? super Container> recipe) {
		return recipe.matches(this.container, this.level);
	}

	@Override
	public int getResultSlotIndex() {
		return RESULT_SLOT;
	}

	@Override
	public int getGridWidth() {
		return 1;
	}

	@Override
	public int getGridHeight() {
		return 1;
	}

	@Override
	public int getSize() {
		return SLOT_COUNT;
	}

	@Override @NotNull
	public RecipeBookType getRecipeBookType() {
		return ECRecipeBookTypes.GLASS_KILN;
	}

	@Override
	public boolean shouldMoveToInventory(int flag) {
		return flag != 1;
	}

	static class GlassKilnFuelSlot extends Slot {
		private final GlassKilnMenu menu;

		public GlassKilnFuelSlot(GlassKilnMenu menu, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		public boolean mayPlace(@NotNull ItemStack itemStack) {
			return this.menu.isFuel(itemStack);
		}

		public int getMaxStackSize(@NotNull ItemStack itemStack) {
			return super.getMaxStackSize(itemStack);
		}
	}

	static class GlassKilnResultSlot extends Slot {
		private final Player player;
		private int removeCount;

		public GlassKilnResultSlot(Player player, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.player = player;
		}

		public boolean mayPlace(@NotNull ItemStack itemStack) {
			return false;
		}

		@NotNull
		public ItemStack remove(int count) {
			if (this.hasItem()) {
				this.removeCount += Math.min(count, this.getItem().getCount());
			}

			return super.remove(count);
		}

		public void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {
			this.checkTakeAchievements(itemStack);
			super.onTake(player, itemStack);
		}

		protected void onQuickCraft(@NotNull ItemStack itemStack, int count) {
			this.removeCount += count;
			this.checkTakeAchievements(itemStack);
		}

		protected void checkTakeAchievements(ItemStack itemStack) {
			itemStack.onCraftedBy(this.player.level, this.player, this.removeCount);
			if (this.player instanceof ServerPlayer && this.container instanceof GlassKilnBlockEntity) {
				((GlassKilnBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
			}

			this.removeCount = 0;
			net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, itemStack);
		}
	}
}
