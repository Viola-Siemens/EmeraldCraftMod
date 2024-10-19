package com.hexagram2021.emeraldcraft.common.crafting.menu;

import com.google.common.collect.Sets;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ISynchronizableContainer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.MelterBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.SimpleContainerWithTank;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Set;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.isFuel;

public class MelterMenu extends AbstractContainerMenu implements IFluidSyncMenu {
	public static final int INGREDIENT_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int RESULT_INPUT_SLOT = 2;
	public static final int RESULT_OUTPUT_SLOT = 3;
	public static final int SLOT_COUNT = 4;
	public static final int INV_SLOT_START = 4;
	public static final int INV_SLOT_END = 31;
	public static final int USE_ROW_SLOT_START = 31;
	public static final int USE_ROW_SLOT_END = 40;
	public static final int DATA_COUNT = 4;

	private final Container melter;
	private final ContainerData melterData;
	private final Slot ingredientSlot;
	private final Slot resultInputSlot;
	protected final Level level;

	public MelterMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainerWithTank(SLOT_COUNT, MelterBlockEntity.MAX_FLUID_LEVEL), new SimpleContainerData(DATA_COUNT));
	}

	public MelterMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.MELTER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.melter = container;
		this.melterData = data;
		this.level = inventory.player.level();
		this.ingredientSlot = this.addSlot(new MelterMenu.IngredientSlot(this, container, INGREDIENT_SLOT, 41, 17));
		this.addSlot(new MelterMenu.MelterFuelSlot(container, FUEL_SLOT, 41, 53));

		this.resultInputSlot = this.addSlot(new Slot(container, RESULT_INPUT_SLOT, 132, 18) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, RESULT_OUTPUT_SLOT, 132, 52) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
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

	public boolean isLit() {
		return this.melterData.get(0) > 0;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.melter.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
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

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		if(this.melter instanceof ISynchronizableContainer synchronizableContainer && synchronizableContainer.isDirty()) {
			synchronizableContainer.clearDirty();
			for(ServerPlayer serverPlayer: this.usingPlayers) {
				EmeraldCraft.sendMessageToPlayer(synchronizableContainer.getSyncPacket(), serverPlayer.connection.getConnection());
			}
		}
	}

	private final Set<ServerPlayer> usingPlayers = Sets.newIdentityHashSet();
	@Override
	public void addUsingPlayer(ServerPlayer serverPlayer) {
		this.usingPlayers.add(serverPlayer);
	}

	@Override
	public void removeUsingPlayer(ServerPlayer serverPlayer) {
		this.usingPlayers.remove(serverPlayer);
	}

	@Override
	public ClientboundFluidSyncPacket getSyncPacket() {
		if(this.melter instanceof ISynchronizableContainer synchronizableContainer) {
			return synchronizableContainer.getSyncPacket();
		}
		throw new IllegalStateException("Caught getSyncPacket() called from a wrong thread. Container = " + this.melter);
	}

	@Override
	public Container getContainer() {
		return this.melter;
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

	static class IngredientSlot extends Slot {
		private final MelterMenu menu;
		public IngredientSlot(MelterMenu menu, Container container, int slot, int x, int y) {
			super(container, slot, x, y);
			this.menu = menu;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack) {
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

		public boolean mayPlace(ItemStack itemStack) {
			return isFuel(itemStack);
		}

		public int getMaxStackSize(ItemStack itemStack) {
			return super.getMaxStackSize(itemStack);
		}
	}
}
