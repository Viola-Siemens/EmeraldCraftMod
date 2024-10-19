package com.hexagram2021.emeraldcraft.common.crafting.menu;

import com.google.common.collect.Sets;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ISynchronizableContainer;
import com.hexagram2021.emeraldcraft.common.register.ECContainerTypes;
import com.hexagram2021.emeraldcraft.common.register.ECFluids;
import com.hexagram2021.emeraldcraft.common.util.SimpleContainerWithTank;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public class ContinuousMinerMenu extends AbstractContainerMenu implements IFluidSyncMenu {
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
	final Slot inputSlot;

	public ContinuousMinerMenu(int id, Inventory inventory) {
		this(id, inventory, new SimpleContainerWithTank(SLOT_COUNT, ContinuousMinerBlockEntity.MAX_FLUID_LEVEL), new SimpleContainerData(DATA_COUNT));
	}

	public ContinuousMinerMenu(int id, Inventory inventory, Container container, ContainerData data) {
		super(ECContainerTypes.CONTINUOUS_MINER_MENU.get(), id);
		checkContainerSize(container, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.continuousMiner = container;
		this.continuousMinerData = data;

		this.inputSlot = this.addSlot(new Slot(container, INPUT_SLOT, 43, 19) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || itemStack.is(ECFluids.MELTED_EMERALD.getBucket());
			}

			@Override
			public int getMaxStackSize() {
				return 16;
			}
		});
		this.addSlot(new Slot(container, RESULT_SLOT, 43, 53) {
			@Override
			public boolean mayPlace(ItemStack itemStack) {
				return itemStack.is(Items.BUCKET) || itemStack.is(ECFluids.MELTED_EMERALD.getBucket());
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
	public boolean stillValid(Player player) {
		return this.continuousMiner.stillValid(player);
	}

	@Override
	public MenuType<?> getType() {
		return ECContainerTypes.CONTINUOUS_MINER_MENU.get();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
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

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		if(this.continuousMiner instanceof ISynchronizableContainer synchronizableContainer && synchronizableContainer.isDirty()) {
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
		if(this.continuousMiner instanceof ISynchronizableContainer synchronizableContainer) {
			return synchronizableContainer.getSyncPacket();
		}
		throw new IllegalStateException("Caught getSyncPacket() called from a wrong thread. Container = " + this.continuousMiner);
	}

	@Override
	public Container getContainer() {
		return this.continuousMiner;
	}
}
