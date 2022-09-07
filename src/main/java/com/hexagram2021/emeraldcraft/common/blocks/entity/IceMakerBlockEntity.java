package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.IceMakerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.*;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity.FLUID_LEVEL_BUCKET;

public class IceMakerBlockEntity extends LockableTileEntity implements ISidedInventory, IRecipeHelperPopulator, ITickableTileEntity {
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

	protected final IIntArray dataAccess = new IIntArray() {
		public int get(int index) {
			switch (index) {
				case 0:
					return IceMakerBlockEntity.this.inputFluidID;
				case 1:
					return IceMakerBlockEntity.this.inputFluidAmount;
				case 2:
					return IceMakerBlockEntity.this.freezingProgress;
				case 3:
					return IceMakerBlockEntity.this.freezingTotalTime;
				case 4:
					return IceMakerBlockEntity.this.condensateFluidAmount;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
				case 0:
					IceMakerBlockEntity.this.inputFluidID = value;
					break;
				case 1:
					IceMakerBlockEntity.this.inputFluidAmount = value;
					break;
				case 2:
					IceMakerBlockEntity.this.freezingProgress = value;
					break;
				case 3:
					IceMakerBlockEntity.this.freezingTotalTime = value;
					break;
				case 4:
					IceMakerBlockEntity.this.condensateFluidAmount = value;
					break;
			}

		}

		public int getCount() {
			return IceMakerMenu.DATA_COUNT;
		}
	};

	public IceMakerBlockEntity() {
		super(ECBlockEntity.ICE_MAKER.get());
	}

	@Override @Nonnull
	protected TranslationTextComponent getDefaultName() {
		return new TranslationTextComponent("container.ice_maker");
	}

	public int getInputFluidTypeIndex() {
		return this.inputFluidID;
	}

	private boolean isLit() {
		return this.condensateFluidAmount > 0;
	}

	@Override
	public void tick() {
		boolean flag = this.isLit() && this.inputFluidAmount > 0;
		boolean flag1 = false;

		ItemStack condensateItemStack = this.items.get(IceMakerMenu.CONDENSATE_SLOT);

		if (!condensateItemStack.isEmpty() &&
				condensateItemStack.getItem() == Items.WATER_BUCKET &&
				this.condensateFluidAmount <= MAX_CONDENSATE_FLUID_LEVEL - WATER_BUCKET_CONDENSATE_LEVEL * 2) {
			this.condensateFluidAmount += WATER_BUCKET_CONDENSATE_LEVEL * 2;
			this.items.set(IceMakerMenu.CONDENSATE_SLOT, new ItemStack(Items.BUCKET));
		}

		if (this.isLit() && this.inputFluidAmount > 0) {
			IceMakerRecipe recipe = this.level.getRecipeManager().getRecipeFor(ECRecipes.ICE_MAKER_TYPE, this, this.level).orElse(null);

			if (this.canFreeze(recipe, this.items, this.getMaxStackSize())) {
				++this.freezingProgress;
				--this.condensateFluidAmount;
				if (this.freezingProgress >= this.freezingTotalTime) {
					this.freezingProgress = 0;
					this.freezingTotalTime = getTotalFreezeTime(this.level, this);
					this.freeze(recipe, this.items, this.getMaxStackSize());

					flag1 = true;
				}
			} else {
				this.freezingProgress = 0;
			}
		} else if (!this.isLit() && this.freezingProgress > 0) {
			this.freezingProgress = MathHelper.clamp(this.freezingProgress - 2, 0, this.freezingTotalTime);
		}

		boolean nextFlag = this.isLit() && this.inputFluidAmount > 0;
		if (flag != nextFlag) {
			flag1 = true;
			BlockState blockState = this.level.getBlockState(this.worldPosition).setValue(IceMakerBlock.LIT, nextFlag);
			this.level.setBlock(this.worldPosition, blockState, 3);
		}

		if (flag1) {
			this.setChanged();
		}

		ItemStack ingredientInput = this.items.get(IceMakerMenu.INGREDIENT_INPUT_SLOT);
		ItemStack ingredientOutput = this.items.get(IceMakerMenu.INGREDIENT_OUTPUT_SLOT);
		if(!ingredientInput.isEmpty()) {
			if(ingredientInput.getItem() == FluidTypes.getFluidBucketItemWithID(this.inputFluidID)) {
				if(this.inputFluidAmount <= MAX_INGREDIENT_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						ingredientInput.shrink(1);
						this.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(ingredientOutput.getItem() == Items.BUCKET && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientInput.shrink(1);
						ingredientOutput.grow(1);
					} else {
						return;
					}
					this.inputFluidAmount += FLUID_LEVEL_BUCKET;
				}
			} else if(ingredientInput.getItem() == Items.BUCKET) {
				if(this.inputFluidAmount >= FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						ingredientInput.shrink(1);
						this.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(FluidTypes.getFluidBucketItemWithID(this.inputFluidID)));
					} else if(ingredientOutput.getItem() == FluidTypes.getFluidBucketItemWithID(this.inputFluidID) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientInput.shrink(1);
						ingredientOutput.grow(1);
					} else {
						return;
					}
					this.inputFluidAmount -= FLUID_LEVEL_BUCKET;
				}
			} else if(this.inputFluidAmount <= 0 && IceMakerMenu.isFluidBucket(ingredientInput)) {
				this.inputFluidID = FluidTypes.getIDFromBucketItem(ingredientInput.getItem());
				this.freezingTotalTime = getTotalFreezeTime(this.level, this);
				if(ingredientOutput.isEmpty()) {
					ingredientInput.shrink(1);
					this.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
				} else if(ingredientOutput.getItem() == Items.BUCKET && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
					ingredientInput.shrink(1);
					ingredientOutput.grow(1);
				} else {
					return;
				}
				this.inputFluidAmount = FLUID_LEVEL_BUCKET;
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
			} else if (itemstack.sameItem(result)) {
				itemstack.grow(result.getCount());
			}

			this.inputFluidAmount -= recipe.getFluidAmount();
			return true;
		}
		return false;
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void load(@Nonnull BlockState blockState, @Nonnull CompoundNBT nbt) {
		super.load(blockState, nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.items);
		this.inputFluidID = nbt.getInt("InputFluidType");
		this.inputFluidAmount = nbt.getInt("InputFluidAmount");
		this.freezingProgress = nbt.getInt("FreezingProgress");
		this.freezingTotalTime = nbt.getInt("FreezingTimeTotal");
		this.condensateFluidAmount = nbt.getInt("CondensateFluidAmount");
	}

	@Override @Nonnull
	public CompoundNBT save(@Nonnull CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("InputFluidType", this.inputFluidID);
		nbt.putInt("InputFluidAmount", this.inputFluidAmount);
		nbt.putInt("FreezingProgress", this.freezingProgress);
		nbt.putInt("FreezingTimeTotal", this.freezingTotalTime);
		nbt.putInt("CondensateFluidAmount", this.condensateFluidAmount);
		ItemStackHelper.saveAllItems(nbt, this.items);
		return nbt;
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

	@Override @Nonnull
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override @Nonnull
	public ItemStack removeItem(int index, int count) {
		return ItemStackHelper.removeItem(this.items, index, count);
	}

	@Override @Nonnull
	public ItemStack removeItemNoUpdate(int index) {
		return ItemStackHelper.takeItem(this.items, index);
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack itemStack) {
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack itemStack) {
		if (index == IceMakerMenu.RESULT_SLOT) {
			return false;
		}
		Item item = itemStack.getItem();
		if (index == IceMakerMenu.CONDENSATE_SLOT) {
			return item == Items.BUCKET || item == Items.WATER_BUCKET;
		}
		return item == Items.BUCKET || IceMakerMenu.isFluidBucket(itemStack);
	}

	private static int getTotalFreezeTime(World level, IInventory container) {
		return level.getRecipeManager().getRecipeFor(ECRecipes.ICE_MAKER_TYPE, container, level).map(IceMakerRecipe::getFreezingTime).orElse(IceMakerRecipe.FREEZING_TIME);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void fillStackedContents(@Nonnull RecipeItemHelper contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Override
	public int[] getSlotsForFace(@Nonnull Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		if (direction == Direction.UP) {
			return SLOTS_FOR_UP;
		}
		return SLOTS_FOR_SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack itemStack, @Nonnull Direction direction) {
		if (direction == Direction.DOWN && index == IceMakerMenu.CONDENSATE_SLOT) {
			return itemStack.getItem() == Items.BUCKET;
		}
		return true;
	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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

	@Override @Nonnull
	protected Container createMenu(int id, @Nonnull PlayerInventory inventory) {
		return new IceMakerMenu(id, inventory, this, this.dataAccess);
	}
}
