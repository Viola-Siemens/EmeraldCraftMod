package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.IceMakerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IceMakerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity.FLUID_LEVEL_BUCKET;

public class IceMakerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
	public static final int MAX_INGREDIENT_FLUID_LEVEL = 1000;
	public static final int MAX_CONDENSATE_FLUID_LEVEL = 800;
	public static final int WATER_BUCKET_CONDENSATE_LEVEL = 100;

	private static final int[] SLOTS_FOR_UP = new int[]{0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
	private static final int[] SLOTS_FOR_SIDES = new int[]{2, 0};

	protected NonNullList<ItemStack> items = NonNullList.withSize(IceMakerMenu.SLOT_COUNT, ItemStack.EMPTY);
	int inputFluidID;
	int inputFluidAmount;
	int freezingProgress;
	int freezingTotalTime;
	int condensateFluidAmount;

	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> IceMakerBlockEntity.this.inputFluidID;
				case 1 -> IceMakerBlockEntity.this.inputFluidAmount;
				case 2 -> IceMakerBlockEntity.this.freezingProgress;
				case 3 -> IceMakerBlockEntity.this.freezingTotalTime;
				case 4 -> IceMakerBlockEntity.this.condensateFluidAmount;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> IceMakerBlockEntity.this.inputFluidID = value;
				case 1 -> IceMakerBlockEntity.this.inputFluidAmount = value;
				case 2 -> IceMakerBlockEntity.this.freezingProgress = value;
				case 3 -> IceMakerBlockEntity.this.freezingTotalTime = value;
				case 4 -> IceMakerBlockEntity.this.condensateFluidAmount = value;
			}

		}

		public int getCount() {
			return IceMakerMenu.DATA_COUNT;
		}
	};

	public IceMakerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.ICE_MAKER.get(), pos, state);
	}

	@Override @NotNull
	protected Component getDefaultName() {
		return Component.translatable("container.ice_maker");
	}

	public int getInputFluidTypeIndex() {
		return this.inputFluidID;
	}

	private boolean isLit() {
		return this.condensateFluidAmount > 0;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, IceMakerBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit() && blockEntity.inputFluidAmount > 0;
		boolean flag1 = false;

		if (!blockEntity.items.get(IceMakerMenu.CONDENSATE_SLOT).isEmpty() &&
				blockEntity.items.get(IceMakerMenu.CONDENSATE_SLOT).is(Items.WATER_BUCKET) &&
				blockEntity.condensateFluidAmount <= MAX_CONDENSATE_FLUID_LEVEL - WATER_BUCKET_CONDENSATE_LEVEL * 2) {
			blockEntity.condensateFluidAmount += WATER_BUCKET_CONDENSATE_LEVEL * 2;
			blockEntity.items.set(IceMakerMenu.CONDENSATE_SLOT, new ItemStack(Items.BUCKET));
		}

		if (blockEntity.isLit() && blockEntity.inputFluidAmount > 0) {
			IceMakerRecipe recipe = level.getRecipeManager().getRecipeFor(ECRecipes.ICE_MAKER_TYPE.get(), blockEntity, level).orElse(null);

			if (blockEntity.canFreeze(recipe, blockEntity.items, blockEntity.getMaxStackSize())) {
				++blockEntity.freezingProgress;
				--blockEntity.condensateFluidAmount;
				if (blockEntity.freezingProgress >= blockEntity.freezingTotalTime) {
					blockEntity.freezingProgress = 0;
					blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
					blockEntity.freeze(recipe, blockEntity.items, blockEntity.getMaxStackSize());

					flag1 = true;
				}
			} else {
				blockEntity.freezingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.freezingProgress > 0) {
			blockEntity.freezingProgress = Mth.clamp(blockEntity.freezingProgress - 2, 0, blockEntity.freezingTotalTime);
		}

		boolean nextFlag = blockEntity.isLit() && blockEntity.inputFluidAmount > 0;
		if (flag != nextFlag) {
			flag1 = true;
			blockState = blockState.setValue(IceMakerBlock.LIT, nextFlag);
			level.setBlock(pos, blockState, Block.UPDATE_ALL);
		}

		if (flag1) {
			setChanged(level, pos, blockState);
		}

		ItemStack ingredientInput = blockEntity.items.get(IceMakerMenu.INGREDIENT_INPUT_SLOT);
		ItemStack ingredientOutput = blockEntity.items.get(IceMakerMenu.INGREDIENT_OUTPUT_SLOT);
		if(!ingredientInput.isEmpty()) {
			if(ingredientInput.is(FluidTypes.getFluidBucketItemWithID(blockEntity.inputFluidID))) {
				if(blockEntity.inputFluidAmount <= MAX_INGREDIENT_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						ingredientInput.shrink(1);
						blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientInput.shrink(1);
						ingredientOutput.grow(1);
					} else {
						return;
					}
					blockEntity.inputFluidAmount += FLUID_LEVEL_BUCKET;
				}
			} else if(ingredientInput.is(Items.BUCKET)) {
				if(blockEntity.inputFluidAmount >= FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						ingredientInput.shrink(1);
						blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(FluidTypes.getFluidBucketItemWithID(blockEntity.inputFluidID)));
					} else if(ingredientOutput.is(FluidTypes.getFluidBucketItemWithID(blockEntity.inputFluidID)) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientInput.shrink(1);
						ingredientOutput.grow(1);
					} else {
						return;
					}
					blockEntity.inputFluidAmount -= FLUID_LEVEL_BUCKET;
				}
			} else if(blockEntity.inputFluidAmount <= 0 && IceMakerMenu.isFluidBucket(ingredientInput)) {
				blockEntity.inputFluidID = FluidTypes.getIDFromBucketItem(ingredientInput.getItem());
				blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
				if(ingredientOutput.isEmpty()) {
					ingredientInput.shrink(1);
					blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
				} else if(ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
					ingredientInput.shrink(1);
					ingredientOutput.grow(1);
				} else {
					return;
				}
				blockEntity.inputFluidAmount = FLUID_LEVEL_BUCKET;
			}
		}
	}

	private boolean canFreeze(@Nullable IceMakerRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (recipe == null) {
			return false;
		}
		ItemStack result = recipe.assemble(this);
		if (result.isEmpty()) {
			return false;
		}
		ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
		if(itemstack.isEmpty()) {
			return true;
		}
		if(!itemstack.sameItem(result)) {
			return false;
		}
		if(itemstack.getCount() + result.getCount() <= maxCount && itemstack.getCount() + result.getCount() <= itemstack.getMaxStackSize()) {
			return true;
		}
		return itemstack.getCount() + result.getCount() <= result.getMaxStackSize();
	}

	private boolean freeze(@Nullable IceMakerRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (this.canFreeze(recipe, container, maxCount)) {
			ItemStack result = recipe.assemble(this);
			ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
			if(itemstack.isEmpty()) {
				container.set(IceMakerMenu.RESULT_SLOT, result.copy());
			} else if (itemstack.is(result.getItem())) {
				itemstack.grow(result.getCount());
			}

			this.inputFluidAmount -= recipe.getFluidAmount();
			return true;
		}
		return false;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.inputFluidID = nbt.getInt("InputFluidType");
		this.inputFluidAmount = nbt.getInt("InputFluidAmount");
		this.freezingProgress = nbt.getInt("FreezingProgress");
		this.freezingTotalTime = nbt.getInt("FreezingTimeTotal");
		this.condensateFluidAmount = nbt.getInt("CondensateFluidAmount");
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("InputFluidType", this.inputFluidID);
		nbt.putInt("InputFluidAmount", this.inputFluidAmount);
		nbt.putInt("FreezingProgress", this.freezingProgress);
		nbt.putInt("FreezingTimeTotal", this.freezingTotalTime);
		nbt.putInt("CondensateFluidAmount", this.condensateFluidAmount);
		ContainerHelper.saveAllItems(nbt, this.items);
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override @NotNull
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override @NotNull
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.items, index, count);
	}

	@Override @NotNull
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.items, index);
	}

	@Override
	public void setItem(int index, @NotNull ItemStack itemStack) {
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int index, @NotNull ItemStack itemStack) {
		if (index == IceMakerMenu.RESULT_SLOT) {
			return false;
		}
		if (index == IceMakerMenu.CONDENSATE_SLOT) {
			return itemStack.is(Items.BUCKET) || itemStack.is(Items.WATER_BUCKET);
		}
		return itemStack.is(Items.BUCKET) || IceMakerMenu.isFluidBucket(itemStack);
	}

	private static int getTotalFreezeTime(Level level, Container container) {
		return level.getRecipeManager().getRecipeFor(ECRecipes.ICE_MAKER_TYPE.get(), container, level).map(IceMakerRecipe::getFreezingTime).orElse(IceMakerRecipe.FREEZING_TIME);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void fillStackedContents(@NotNull StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Override
	public int[] getSlotsForFace(@NotNull Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		if (direction == Direction.UP) {
			return SLOTS_FOR_UP;
		}
		return SLOTS_FOR_SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @NotNull ItemStack itemStack, @NotNull Direction direction) {
		if (direction == Direction.DOWN && index == IceMakerMenu.CONDENSATE_SLOT) {
			return itemStack.is(Items.BUCKET);
		}
		return true;
	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
			if (facing == Direction.UP) {
				return handlers[0].cast();
			} else if (facing == Direction.DOWN) {
				return handlers[1].cast();
			} else {
				return handlers[2].cast();
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (LazyOptional<? extends IItemHandler> handler : handlers) {
			handler.invalidate();
		}
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}

	@Override @NotNull
	protected AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
		return new IceMakerMenu(id, inventory, this, this.dataAccess);
	}
}
