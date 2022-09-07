package com.hexagram2021.emeraldcraft.common.crafting;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class CarpentryTableMenu extends Container {
	public static final int INPUT_SLOT = 0;
	public static final int RESULT_SLOT = 1;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;

	private final IWorldPosCallable access;
	private final IntReferenceHolder selectedRecipeIndex = IntReferenceHolder.standalone();
	private final World level;
	private List<CarpentryTableRecipe> recipes = Lists.newArrayList();
	private ItemStack input = ItemStack.EMPTY;
	long lastSoundTime;
	final Slot inputSlot;
	final Slot resultSlot;
	Runnable slotUpdateListener = () -> {
	};
	public final IInventory container = new Inventory(1) {
		public void setChanged() {
			super.setChanged();
			CarpentryTableMenu.this.slotsChanged(this);
			CarpentryTableMenu.this.slotUpdateListener.run();
		}
	};
	final CraftResultInventory resultContainer = new CraftResultInventory();

	public CarpentryTableMenu(int id, PlayerInventory inventory) {
		this(id, inventory, IWorldPosCallable.NULL);
	}

	public CarpentryTableMenu(int id, PlayerInventory inventory, final IWorldPosCallable access) {
		super(ECContainerTypes.CARPENTRY_TABLE_MENU.get(), id);
		this.access = access;
		this.level = inventory.player.level;
		this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
		this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
			@Override
			public boolean mayPlace(@Nonnull ItemStack itemStack) {
				return false;
			}

			@Override @Nonnull
			public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack itemStack) {
				itemStack.onCraftedBy(player.level, player, itemStack.getCount());
				CarpentryTableMenu.this.resultContainer.awardUsedRecipes(player);
				ItemStack itemstack = CarpentryTableMenu.this.inputSlot.remove(1);
				if (!itemstack.isEmpty()) {
					CarpentryTableMenu.this.setupResultSlot();
				}

				access.execute((level, blockPos) -> {
					long l = level.getGameTime();
					if (l - CarpentryTableMenu.this.lastSoundTime > 20) {
						level.playSound(null, blockPos, ECSounds.VILLAGER_WORK_CARPENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
						CarpentryTableMenu.this.lastSoundTime = l;
					}

				});

				return super.onTake(player, itemStack);
			}
		});

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
		}

		this.addDataSlot(this.selectedRecipeIndex);
	}

	public int getSelectedRecipeIndex() {
		return this.selectedRecipeIndex.get();
	}

	public List<CarpentryTableRecipe> getRecipes() {
		return this.recipes;
	}

	public int getNumRecipes() {
		return this.recipes.size();
	}

	public boolean hasInputItem() {
		return this.inputSlot.hasItem() && !this.recipes.isEmpty();
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return stillValid(this.access, player, ECBlocks.WorkStation.CARPENTRY_TABLE.get());
	}

	@Override
	public boolean clickMenuButton(@Nonnull PlayerEntity player, int index) {
		if (this.isValidRecipeIndex(index)) {
			this.selectedRecipeIndex.set(index);
			this.setupResultSlot();
		}

		return true;
	}

	private boolean isValidRecipeIndex(int index) {
		return index >= 0 && index < this.recipes.size();
	}

	@Override
	public void slotsChanged(@Nonnull IInventory container) {
		ItemStack itemstack = this.inputSlot.getItem();
		if (!itemstack.sameItem(this.input)) {
			this.input = itemstack.copy();
			this.setupRecipeList(container, itemstack);
		}

	}

	private void setupRecipeList(IInventory container, ItemStack itemStack) {
		this.recipes.clear();
		this.selectedRecipeIndex.set(-1);
		this.resultSlot.set(ItemStack.EMPTY);
		if (!itemStack.isEmpty()) {
			this.recipes = this.level.getRecipeManager().getRecipesFor(ECRecipes.CARPENTRY_TABLE_TYPE, container, this.level);
		}

	}

	void setupResultSlot() {
		if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
			CarpentryTableRecipe carpentrytableRecipe = this.recipes.get(this.selectedRecipeIndex.get());
			this.resultContainer.setRecipeUsed(carpentrytableRecipe);
			this.resultSlot.set(carpentrytableRecipe.assemble(this.container));
		} else {
			this.resultSlot.set(ItemStack.EMPTY);
		}

		this.broadcastChanges();
	}

	@Override @Nonnull
	public ContainerType<?> getType() {
		return ECContainerTypes.CARPENTRY_TABLE_MENU.get();
	}

	public void registerUpdateListener(Runnable listener) {
		this.slotUpdateListener = listener;
	}

	@Override
	public boolean canTakeItemForPickAll(@Nonnull ItemStack itemStack, Slot slot) {
		return slot.container != this.resultContainer && super.canTakeItemForPickAll(itemStack, slot);
	}

	@Override @Nonnull
	public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			Item item = itemstack1.getItem();
			itemstack = itemstack1.copy();
			if (index == RESULT_SLOT) {
				item.onCraftedBy(itemstack1, player.level, player);
				if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index == INPUT_SLOT) {
				if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.level.getRecipeManager()
					.getRecipeFor(ECRecipes.CARPENTRY_TABLE_TYPE, new Inventory(itemstack1), this.level)
					.isPresent()) {
				if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
				if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
				return ItemStack.EMPTY;
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

	@Override
	public void removed(@Nonnull PlayerEntity player) {
		super.removed(player);
		this.resultContainer.removeItemNoUpdate(1);
		this.access.execute((level, blockPos) -> this.clearContainer(player, player.level, this.container));
	}
}
