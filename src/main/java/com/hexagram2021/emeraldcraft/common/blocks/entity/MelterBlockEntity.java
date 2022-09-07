package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.MelterBlock;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.MelterMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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

import java.util.List;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity.FLUID_LEVEL_BUCKET;

public class MelterBlockEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
	public static final int MAX_FLUID_LEVEL = 1000;

	private static final int[] SLOTS_FOR_UP = new int[]{2, 0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
	private static final int[] SLOTS_FOR_SIDES = new int[]{2, 1};

	protected NonNullList<ItemStack> items = NonNullList.withSize(MelterMenu.SLOT_COUNT, ItemStack.EMPTY);
	int litTime;
	int litDuration;
	int meltingProgress;
	int meltingTotalTime;
	int fluidTypeID;
	int fluidAmount;
	protected final IIntArray dataAccess = new IIntArray() {
		public int get(int index) {
			switch (index) {
				case 0:
					return MelterBlockEntity.this.litTime;
				case 1:
					return MelterBlockEntity.this.litDuration;
				case 2:
					return MelterBlockEntity.this.meltingProgress;
				case 3:
					return MelterBlockEntity.this.meltingTotalTime;
				case 4:
					return MelterBlockEntity.this.fluidTypeID;
				case 5:
					return MelterBlockEntity.this.fluidAmount;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
				case 0:
					MelterBlockEntity.this.litTime = value;
					break;
				case 1:
					MelterBlockEntity.this.litDuration = value;
					break;
				case 2:
					MelterBlockEntity.this.meltingProgress = value;
					break;
				case 3:
					MelterBlockEntity.this.meltingTotalTime = value;
					break;
				case 4:
					MelterBlockEntity.this.fluidTypeID = value;
					break;
				case 5:
					MelterBlockEntity.this.fluidAmount = value;
					break;
			}
		}

		public int getCount() {
			return MelterMenu.DATA_COUNT;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

	public MelterBlockEntity() {
		super(ECBlockEntity.MELTER.get());
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public void tick() {
		boolean flag = this.isLit();
		boolean flag1 = false;
		if (this.isLit()) {
			--this.litTime;
		}

		ItemStack fuelItemStack = this.items.get(MelterMenu.FUEL_SLOT);
		if (this.isLit() || !fuelItemStack.isEmpty() && !this.items.get(MelterMenu.INGREDIENT_SLOT).isEmpty()) {
			MelterRecipe recipe = this.level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE, this, this.level).orElse(null);
			if (!this.isLit() && this.canBurn(recipe, this.items)) {
				this.litTime = this.getBurnDuration(fuelItemStack);
				this.litDuration = this.litTime;
				if (this.isLit()) {
					flag1 = true;
					if (fuelItemStack.hasContainerItem()) {
						this.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getContainerItem());
					} else if (!fuelItemStack.isEmpty()) {
						fuelItemStack.shrink(1);
						if (fuelItemStack.isEmpty()) {
							this.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getContainerItem());
						}
					}
				}
			}

			if (this.isLit() && this.canBurn(recipe, this.items)) {
				++this.meltingProgress;
				if (this.meltingProgress >= this.meltingTotalTime) {
					this.meltingProgress = 0;
					this.meltingTotalTime = getTotalMeltTime(this.level, this);
					this.burn(recipe, this.items);

					flag1 = true;
				}
			} else {
				this.meltingProgress = 0;
			}
		} else if (!this.isLit() && this.meltingProgress > 0) {
			this.meltingProgress = MathHelper.clamp(this.meltingProgress - 2, 0, this.meltingTotalTime);
		}

		if (flag != this.isLit()) {
			flag1 = true;
			BlockState blockState = this.level.getBlockState(this.worldPosition).setValue(MelterBlock.LIT, this.isLit());
			this.level.setBlock(this.worldPosition, blockState, 3);
		}

		if (flag1) {
			this.setChanged();
		}

		ItemStack resultInput = this.items.get(MelterMenu.RESULT_INPUT_SLOT);
		ItemStack resultOutput = this.items.get(MelterMenu.RESULT_OUTPUT_SLOT);
		if(!resultInput.isEmpty()) {
			if(resultInput.getItem() == FluidTypes.getFluidBucketItemWithID(this.fluidTypeID)) {
				if(this.fluidAmount <= MAX_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(resultOutput.isEmpty()) {
						resultInput.shrink(1);
						this.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(resultOutput.getItem() == Items.BUCKET && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
						resultInput.shrink(1);
						resultOutput.grow(1);
					} else {
						return;
					}
					this.fluidAmount += FLUID_LEVEL_BUCKET;
				}
			} else if(resultInput.getItem() == Items.BUCKET) {
				if(this.fluidAmount >= FLUID_LEVEL_BUCKET) {
					if(resultOutput.isEmpty()) {
						resultInput.shrink(1);
						this.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(FluidTypes.getFluidBucketItemWithID(this.fluidTypeID)));
					} else if(resultOutput.getItem() == FluidTypes.getFluidBucketItemWithID(this.fluidTypeID) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
						resultInput.shrink(1);
						resultOutput.grow(1);
					} else {
						return;
					}
					this.fluidAmount -= FLUID_LEVEL_BUCKET;
				}
			} else if(this.fluidAmount <= 0 && MelterMenu.isFluidBucket(resultInput)) {
				this.fluidTypeID = FluidTypes.getIDFromBucketItem(resultInput.getItem());
				if(resultOutput.isEmpty()) {
					resultInput.shrink(1);
					this.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
				} else if(resultOutput.getItem() == Items.BUCKET && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
					resultInput.shrink(1);
					resultOutput.grow(1);
				} else {
					return;
				}
				this.fluidAmount = FLUID_LEVEL_BUCKET;
			}
		}
	}

	private boolean canBurn(MelterRecipe recipe, NonNullList<ItemStack> container) {
		if (recipe == null || container.get(0).isEmpty()) {
			return false;
		}
		if (FluidTypes.getID(recipe.getFluidType()) != this.fluidTypeID) {
			return this.fluidAmount == 0;
		}
		return recipe.getFluidAmount() + this.fluidAmount <= MAX_FLUID_LEVEL;
	}

	private boolean burn(@Nullable MelterRecipe recipe, NonNullList<ItemStack> container) {
		if (this.canBurn(recipe, container)) {
			ItemStack itemstack = container.get(0);
			this.fluidTypeID = FluidTypes.getID(recipe.getFluidType());
			this.fluidAmount += recipe.getFluidAmount();

			itemstack.shrink(1);
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
		this.litTime = nbt.getInt("BurnTime");
		this.litDuration = this.getBurnDuration(this.items.get(1));
		this.meltingProgress = nbt.getInt("MeltTime");
		this.meltingTotalTime = nbt.getInt("MeltTimeTotal");
		this.fluidTypeID = nbt.getInt("FluidType");
		this.fluidAmount = nbt.getInt("FluidAmount");
		CompoundNBT compoundtag = nbt.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}

	}

	@Override @Nonnull
	public CompoundNBT save(@Nonnull CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.meltingProgress);
		nbt.putInt("CookTimeTotal", this.meltingTotalTime);
		nbt.putInt("FluidType", this.fluidTypeID);
		nbt.putInt("FluidAmount", this.fluidAmount);
		ItemStackHelper.saveAllItems(nbt, this.items);
		CompoundNBT compoundtag = new CompoundNBT();
		this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
		nbt.put("RecipesUsed", compoundtag);
		return nbt;
	}

	@Override @Nonnull
	protected TranslationTextComponent getDefaultName() {
		return new TranslationTextComponent("container.melter");
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
	public void setItem(int index, ItemStack itemStack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !itemStack.isEmpty() && itemStack.sameItem(itemstack) && ItemStack.tagMatches(itemStack, itemstack);
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}

		if (index == 0 && !flag) {
			this.meltingTotalTime = getTotalMeltTime(this.level, this);
			this.meltingProgress = 0;
			this.setChanged();
		}
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (index == MelterMenu.RESULT_INPUT_SLOT || index == MelterMenu.RESULT_OUTPUT_SLOT) {
			return item == Items.BUCKET || MelterMenu.isFluidBucket(itemStack);
		}
		if (index != MelterMenu.FUEL_SLOT) {
			return true;
		}
		ItemStack fuelItemStack = this.items.get(MelterMenu.FUEL_SLOT);
		return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack) > 0 || item == Items.BUCKET && fuelItemStack.getItem() != Items.BUCKET;
	}

	protected int getBurnDuration(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack);
	}

	private static int getTotalMeltTime(World level, IInventory container) {
		return level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE, container, level).map(MelterRecipe::getMeltingTime).orElse(MelterRecipe.MELTING_TIME);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipesUsed.addTo(resourcelocation, 1);
		}
	}

	@Override @Nullable
	public IRecipe<?> getRecipeUsed() {
		return null;
	}

	@Override
	public void awardUsedRecipes(@Nonnull PlayerEntity player) {
		List<IRecipe<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			this.level.getRecipeManager().byKey(entry.getKey()).ifPresent(list::add);
		}
		player.awardRecipes(list);

		this.recipesUsed.clear();
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
		Item item = itemStack.getItem();
		if (direction == Direction.DOWN && index == 1) {
			return item == Items.WATER_BUCKET || item == Items.BUCKET;
		}
		return true;
	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @Nonnull
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
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
		return new MelterMenu(id, inventory, this, this.dataAccess);
	}
}
