package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.blocks.entity.GlassKilnBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class GlassKilnMenu extends RecipeBookContainer<IInventory> {
	private final IInventory container;
	private final IIntArray data;
	protected final World level;

	public static final int INGREDIENT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int RESULT_SLOT = 2;
	private static final int INV_SLOT_START = 3;
	private static final int INV_SLOT_END = 30;
	private static final int USE_ROW_SLOT_START = 30;
	private static final int USE_ROW_SLOT_END = 39;
	public static final int SLOT_COUNT = 3;
	public static final int DATA_COUNT = 4;

	public GlassKilnMenu(int id, PlayerInventory inventory) {
		this(id, inventory, new Inventory(SLOT_COUNT), new IntArray(DATA_COUNT));
	}

	public GlassKilnMenu(int id, PlayerInventory inventory, IInventory container, IIntArray data) {
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

	@Override @Nonnull
	public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
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
		return this.level.getRecipeManager().getRecipeFor(ECRecipes.GLASS_KILN_TYPE, new Inventory(itemStack), this.level).isPresent();
	}

	public boolean isFuel(ItemStack itemStack) {
		return ForgeHooks.getBurnTime(itemStack) > 0;
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return this.container.stillValid(player);
	}

	@Override
	public void fillCraftSlotsStackedContents(@Nonnull RecipeItemHelper contents) {
		if (this.container instanceof IRecipeHelperPopulator) {
			((IRecipeHelperPopulator)this.container).fillStackedContents(contents);
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
	public boolean recipeMatches(IRecipe<? super IInventory> recipe) {
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

	@Override @Nonnull
	public RecipeBookCategory getRecipeBookType() {
		return ECRecipeBookTypes.GLASS_KILN;
	}

	static class GlassKilnFuelSlot extends Slot {
		private final GlassKilnMenu menu;

		public GlassKilnFuelSlot(GlassKilnMenu menu, IInventory container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack itemStack) {
			return this.menu.isFuel(itemStack);
		}

		@Override
		public int getMaxStackSize(@Nonnull ItemStack itemStack) {
			return super.getMaxStackSize(itemStack);
		}
	}

	static class GlassKilnResultSlot extends Slot {
		private final PlayerEntity player;
		private int removeCount;

		public GlassKilnResultSlot(PlayerEntity player, IInventory container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.player = player;
		}

		@Override
		public boolean mayPlace(@Nonnull ItemStack itemStack) {
			return false;
		}

		@Nonnull
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
			if (this.player instanceof ServerPlayerEntity && this.container instanceof GlassKilnBlockEntity) {
				((GlassKilnBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayerEntity)this.player);
			}

			this.removeCount = 0;
			net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, itemStack);
		}
	}
}
