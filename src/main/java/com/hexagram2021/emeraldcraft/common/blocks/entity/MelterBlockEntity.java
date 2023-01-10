package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.MelterBlock;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.menu.MelterMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.List;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity.FLUID_LEVEL_BUCKET;

public class MelterBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
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
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> MelterBlockEntity.this.litTime;
				case 1 -> MelterBlockEntity.this.litDuration;
				case 2 -> MelterBlockEntity.this.meltingProgress;
				case 3 -> MelterBlockEntity.this.meltingTotalTime;
				case 4 -> MelterBlockEntity.this.fluidTypeID;
				case 5 -> MelterBlockEntity.this.fluidAmount;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> MelterBlockEntity.this.litTime = value;
				case 1 -> MelterBlockEntity.this.litDuration = value;
				case 2 -> MelterBlockEntity.this.meltingProgress = value;
				case 3 -> MelterBlockEntity.this.meltingTotalTime = value;
				case 4 -> MelterBlockEntity.this.fluidTypeID = value;
				case 5 -> MelterBlockEntity.this.fluidAmount = value;
			}

		}

		public int getCount() {
			return MelterMenu.DATA_COUNT;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

	public MelterBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.MELTER.get(), pos, state);
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, MelterBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit();
		boolean flag1 = false;
		if (blockEntity.isLit()) {
			--blockEntity.litTime;
		}

		ItemStack fuelItemStack = blockEntity.items.get(MelterMenu.FUEL_SLOT);
		if (blockEntity.isLit() || !fuelItemStack.isEmpty() && !blockEntity.items.get(MelterMenu.INGREDIENT_SLOT).isEmpty()) {
			MelterRecipe recipe = level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE.get(), blockEntity, level).orElse(null);
			if (!blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
				blockEntity.litTime = blockEntity.getBurnDuration(fuelItemStack);
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					flag1 = true;
					if (fuelItemStack.hasCraftingRemainingItem()) {
							blockEntity.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getCraftingRemainingItem());
					} else if (!fuelItemStack.isEmpty()) {
						fuelItemStack.shrink(1);
						if (fuelItemStack.isEmpty()) {
							blockEntity.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getCraftingRemainingItem());
						}
					}
				}
			}

			if (blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
				++blockEntity.meltingProgress;
				if (blockEntity.meltingProgress >= blockEntity.meltingTotalTime) {
					blockEntity.meltingProgress = 0;
					blockEntity.meltingTotalTime = getTotalMeltTime(level, blockEntity);
					blockEntity.burn(recipe, blockEntity.items);

					flag1 = true;
				}
			} else {
				blockEntity.meltingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.meltingProgress > 0) {
			blockEntity.meltingProgress = Mth.clamp(blockEntity.meltingProgress - 2, 0, blockEntity.meltingTotalTime);
		}

		if (flag != blockEntity.isLit()) {
			flag1 = true;
			blockState = blockState.setValue(MelterBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, blockState, 3);
		}

		if (flag1) {
			setChanged(level, pos, blockState);
		}

		ItemStack resultInput = blockEntity.items.get(MelterMenu.RESULT_INPUT_SLOT);
		ItemStack resultOutput = blockEntity.items.get(MelterMenu.RESULT_OUTPUT_SLOT);
		if(!resultInput.isEmpty()) {
			if(resultInput.is(FluidTypes.getFluidBucketItemWithID(blockEntity.fluidTypeID))) {
				if(blockEntity.fluidAmount <= MAX_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(resultOutput.isEmpty()) {
						resultInput.shrink(1);
						blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(resultOutput.is(Items.BUCKET) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
						resultInput.shrink(1);
						resultOutput.grow(1);
					} else {
						return;
					}
					blockEntity.fluidAmount += FLUID_LEVEL_BUCKET;
				}
			} else if(resultInput.is(Items.BUCKET)) {
				if(blockEntity.fluidAmount >= FLUID_LEVEL_BUCKET) {
					if(resultOutput.isEmpty()) {
						resultInput.shrink(1);
						blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(FluidTypes.getFluidBucketItemWithID(blockEntity.fluidTypeID)));
					} else if(resultOutput.is(FluidTypes.getFluidBucketItemWithID(blockEntity.fluidTypeID)) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
						resultInput.shrink(1);
						resultOutput.grow(1);
					} else {
						return;
					}
					blockEntity.fluidAmount -= FLUID_LEVEL_BUCKET;
				}
			} else if(blockEntity.fluidAmount <= 0 && MelterMenu.isFluidBucket(resultInput)) {
				blockEntity.fluidTypeID = FluidTypes.getIDFromBucketItem(resultInput.getItem());
				if(resultOutput.isEmpty()) {
					resultInput.shrink(1);
					blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
				} else if(resultOutput.is(Items.BUCKET) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
					resultInput.shrink(1);
					resultOutput.grow(1);
				} else {
					return;
				}
				blockEntity.fluidAmount = FLUID_LEVEL_BUCKET;
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
		this.litTime = nbt.getInt("BurnTime");
		this.litDuration = this.getBurnDuration(this.items.get(1));
		this.meltingProgress = nbt.getInt("MeltTime");
		this.meltingTotalTime = nbt.getInt("MeltTimeTotal");
		this.fluidTypeID = nbt.getInt("FluidType");
		this.fluidAmount = nbt.getInt("FluidAmount");
		CompoundTag compoundtag = nbt.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}

	}

	@Override
	public void saveAdditional(@NotNull CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.meltingProgress);
		nbt.putInt("CookTimeTotal", this.meltingTotalTime);
		nbt.putInt("FluidType", this.fluidTypeID);
		nbt.putInt("FluidAmount", this.fluidAmount);
		ContainerHelper.saveAllItems(nbt, this.items);
		CompoundTag compoundtag = new CompoundTag();
		this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
		nbt.put("RecipesUsed", compoundtag);
	}

	@Override @NotNull
	protected Component getDefaultName() {
		return Component.translatable("container.melter");
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
	public boolean canPlaceItem(int index, @NotNull ItemStack itemStack) {
		if (index == MelterMenu.RESULT_INPUT_SLOT || index == MelterMenu.RESULT_OUTPUT_SLOT) {
			return itemStack.is(Items.BUCKET) || MelterMenu.isFluidBucket(itemStack);
		}
		if (index != MelterMenu.FUEL_SLOT) {
			return true;
		}
		ItemStack fuelItemStack = this.items.get(MelterMenu.FUEL_SLOT);
		return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, null) > 0 || itemStack.is(Items.BUCKET) && !fuelItemStack.is(Items.BUCKET);
	}

	protected int getBurnDuration(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, null);
	}

	private static int getTotalMeltTime(Level level, Container container) {
		return level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE.get(), container, level).map(MelterRecipe::getMeltingTime).orElse(MelterRecipe.MELTING_TIME);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void setRecipeUsed(@Nullable Recipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipesUsed.addTo(resourcelocation, 1);
		}
	}

	@Override @Nullable
	public Recipe<?> getRecipeUsed() {
		return null;
	}

	@Override
	public void awardUsedRecipes(@NotNull Player player) {
		List<Recipe<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			this.level.getRecipeManager().byKey(entry.getKey()).ifPresent(list::add);
		}
		player.awardRecipes(list);

		this.recipesUsed.clear();
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
		if (direction == Direction.DOWN && index == 1) {
			return itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
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
		return new MelterMenu(id, inventory, this, this.dataAccess);
	}
}
