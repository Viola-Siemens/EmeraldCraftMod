package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class CookstoveBlockEntity extends BlockEntity implements Container, StackedContentsCompatible, Tank {
	public static final int SLOT_INPUT_START = 0;
	public static final int COUNT_SLOTS = 8;
	public static final int MAX_TANK_CAPABILITY = 100;
	public static final int TANK_INPUT = 0;
	public static final int COUNT_TANKS = 1;
	private final NonNullList<ItemStack> items = NonNullList.withSize(COUNT_SLOTS, ItemStack.EMPTY);
	private final FluidTank tank = new FluidTank(MAX_TANK_CAPABILITY);

	int progressTicks = 0;
	int totalTicks = 0;

	public CookstoveBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ECBlockEntity.COOKSTOVE.get(), blockPos, blockState);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, CookstoveBlockEntity blockEntity) {

	}

	@Override
	public void load(CompoundTag nbt) {
		this.items.clear();
		ContainerHelper.loadAllItems(nbt, this.items);
		this.tank.readFromNBT(nbt);
		this.progressTicks = nbt.getInt("progressTicks");
		this.totalTicks = nbt.getInt("totalTicks");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		ContainerHelper.saveAllItems(nbt, this.items, true);
		this.tank.writeToNBT(nbt);
		nbt.putInt("progressTicks", this.progressTicks);
		nbt.putInt("totalTicks", this.totalTicks);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void fillStackedContents(StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Override
	public int getContainerSize() {
		return COUNT_SLOTS;
	}
	@Override
	public boolean isEmpty() {
		return this.items.stream().allMatch(ItemStack::isEmpty);
	}
	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}
	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.items, index, count);
	}
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.items, index);
	}

	@Override
	public FluidStack getFluidStack(int tank) {
		if(tank >= COUNT_TANKS) {
			throw new IndexOutOfBoundsException(tank);
		}
		return this.tank.getFluid();
	}
	@Override
	public int getTankSize() {
		return COUNT_TANKS;
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		if (index >= 0 && index < this.items.size()) {
			this.items.set(index, itemStack);
			if (itemStack.getCount() > this.getMaxStackSize()) {
				itemStack.setCount(this.getMaxStackSize());
			}
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	//Forge Compat
	IItemHandlerModifiable itemHandler = new InvWrapper(this);
	LazyOptional<? extends IItemHandler> itemHandlerWrapper = LazyOptional.of(() -> this.itemHandler);

	private final LazyOptional<IFluidHandler> fluidHandlerWrapper = LazyOptional.of(() -> this.tank);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove) {
			if (capability == ForgeCapabilities.FLUID_HANDLER) {
				return this.fluidHandlerWrapper.cast();
			}
			if(capability == ForgeCapabilities.ITEM_HANDLER) {
				return this.itemHandlerWrapper.cast();
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.itemHandlerWrapper.invalidate();
		this.fluidHandlerWrapper.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.itemHandler = new InvWrapper(this);
		this.itemHandlerWrapper = LazyOptional.of(() -> this.itemHandler);
	}
}
