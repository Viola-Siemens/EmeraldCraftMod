package com.hexagram2021.emeraldcraft.common.crafting.menu;

import com.hexagram2021.emeraldcraft.common.blocks.entity.RabbleFurnaceBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.RabbleFurnaceBlockEntity.*;

@SuppressWarnings("UnstableApiUsage")
public class RabbleFurnaceMenu extends RecipeBookMenu<Container> {
	private final Container container;
	private final ContainerData data;
	protected final Level level;

	public static final int INV_SLOT_START = 5;
	private static final int INV_SLOT_END = 32;
	private static final int USE_ROW_SLOT_START = 32;
	private static final int USE_ROW_SLOT_END = 41;
	public static final int SLOT_COUNT = 5;
	public static final int DATA_COUNT = 4;

	public RabbleFurnaceMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public RabbleFurnaceMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.RABBLE_FURNACE_MENU.get(), id);
		this.container = container;
		this.data = data;
		this.level = inventory.player.level();

		this.addSlot(new Slot(this.container, SLOT_INPUT, 56, 17));
		this.addSlot(new Slot(this.container, SLOT_MIX1, 18, 21));
		this.addSlot(new Slot(this.container, SLOT_MIX2, 18, 39));
		this.addSlot(new RabbleFurnaceMenu.RabbleFurnaceFuelSlot(this, this.container, SLOT_FUEL, 56, 53));
		this.addSlot(new RabbleFurnaceMenu.RabbleFurnaceResultSlot(inventory.player, this.container, SLOT_RESULT, 116, 35));

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

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack origin = slot.getItem();
			itemstack = origin.copy();
			if (index == SLOT_RESULT) {
				if (!this.moveItemStackTo(origin, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(origin, itemstack);
			} else if (index != SLOT_FUEL && index != SLOT_INPUT && index != SLOT_MIX1 && index != SLOT_MIX2) {
				if (this.canSmelt(origin, SLOT_INPUT)) {
					if (!this.moveItemStackTo(origin, SLOT_INPUT, SLOT_INPUT + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.canSmelt(origin, SLOT_MIX1)) {
					if (!this.moveItemStackTo(origin, SLOT_MIX1, SLOT_MIX1 + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.canSmelt(origin, SLOT_MIX2)) {
					if (!this.moveItemStackTo(origin, SLOT_MIX2, SLOT_MIX2 + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(origin)) {
					if (!this.moveItemStackTo(origin, SLOT_FUEL, SLOT_RESULT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
					if (!this.moveItemStackTo(origin, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END &&
						!this.moveItemStackTo(origin, INV_SLOT_START, INV_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(origin, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}

			if (origin.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (origin.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, origin);
		}

		return itemstack;
	}

	protected boolean canSmelt(ItemStack itemStack, int index) {
		return this.level.getRecipeManager().getAllRecipesFor(ECRecipes.RABBLE_FURNACE_TYPE.get()).stream()
				.anyMatch(recipeHolder -> {
					RabbleFurnaceRecipe recipe = recipeHolder.value();
					Container simple = new SimpleContainer(this.slots.stream().map(Slot::getItem).toArray(ItemStack[]::new));
					simple.setItem(index, itemStack);
					return recipe.matchesAllowEmpty(simple);
				});
	}

	public boolean isFuel(ItemStack itemStack) {
		return ForgeHooks.getBurnTime(itemStack, ECRecipes.RABBLE_FURNACE_TYPE.get()) > 0;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedContents contents) {
		if (this.container instanceof StackedContentsCompatible) {
			((StackedContentsCompatible)this.container).fillStackedContents(contents);
		}
	}

	@Override
	public void clearCraftingContent() {
		this.getSlot(SLOT_INPUT).set(ItemStack.EMPTY);
		this.getSlot(SLOT_MIX1).set(ItemStack.EMPTY);
		this.getSlot(SLOT_MIX2).set(ItemStack.EMPTY);
		this.getSlot(SLOT_RESULT).set(ItemStack.EMPTY);
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
	public boolean recipeMatches(RecipeHolder<? extends Recipe<Container>> recipeHolder) {
		return recipeHolder.value().matches(this.container, this.level);
	}

	@Override
	public int getResultSlotIndex() {
		return SLOT_RESULT;
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

	@Override
	public RecipeBookType getRecipeBookType() {
		return ECRecipes.RABBLE_FURNACE;
	}

	@Override
	public boolean shouldMoveToInventory(int flag) {
		return flag != 1;
	}

	static class RabbleFurnaceFuelSlot extends Slot {
		private final RabbleFurnaceMenu menu;

		public RabbleFurnaceFuelSlot(RabbleFurnaceMenu menu, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		public boolean mayPlace(ItemStack itemStack) {
			return this.menu.isFuel(itemStack);
		}

		public int getMaxStackSize(ItemStack itemStack) {
			return super.getMaxStackSize(itemStack);
		}
	}

	static class RabbleFurnaceResultSlot extends Slot {
		private final Player player;
		private int removeCount;

		public RabbleFurnaceResultSlot(Player player, Container container, int slot, int x, int y) {
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

		public void onTake(Player player, ItemStack itemStack) {
			this.checkTakeAchievements(itemStack);
			super.onTake(player, itemStack);
		}

		protected void onQuickCraft(ItemStack itemStack, int count) {
			this.removeCount += count;
			this.checkTakeAchievements(itemStack);
		}

		protected void checkTakeAchievements(ItemStack itemStack) {
			itemStack.onCraftedBy(this.player.level(), this.player, this.removeCount);
			if (this.player instanceof ServerPlayer && this.container instanceof RabbleFurnaceBlockEntity rabbleFurnaceBlockEntity) {
				rabbleFurnaceBlockEntity.awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
			}

			this.removeCount = 0;
			net.minecraftforge.event.ForgeEventFactory.firePlayerSmeltedEvent(this.player, itemStack);
		}
	}
}
