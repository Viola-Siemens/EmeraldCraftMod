package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.MelterBlock;
import com.hexagram2021.emeraldcraft.common.crafting.FluidType;
import com.hexagram2021.emeraldcraft.common.crafting.MelterMenu;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity.FLUID_LEVEL_BUCKET;

public class MelterBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
	public static final int MAX_FLUID_LEVEL = 1000;

	private static final int[] SLOTS_FOR_UP = new int[]{2, 0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
	private static final int[] SLOTS_FOR_SIDES = new int[]{2, 1};

	protected NonNullList<ItemStack> items = NonNullList.withSize(MelterMenu.SLOT_COUNT, ItemStack.EMPTY);
	int litTime;
	int litDuration;
	int meltingProgress;
	int meltingTotalTime;
	int fluidType;
	int fluidAmount;
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> MelterBlockEntity.this.litTime;
				case 1 -> MelterBlockEntity.this.litDuration;
				case 2 -> MelterBlockEntity.this.meltingProgress;
				case 3 -> MelterBlockEntity.this.meltingTotalTime;
				case 4 -> MelterBlockEntity.this.fluidType;
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
				case 4 -> MelterBlockEntity.this.fluidType = value;
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
			MelterRecipe recipe = level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE, blockEntity, level).orElse(null);
			if (!blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
				blockEntity.litTime = blockEntity.getBurnDuration(fuelItemStack);
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					flag1 = true;
					if (fuelItemStack.hasContainerItem()) {
						blockEntity.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getContainerItem());
					} else if (!fuelItemStack.isEmpty()) {
						fuelItemStack.shrink(1);
						if (fuelItemStack.isEmpty()) {
							blockEntity.items.set(MelterMenu.FUEL_SLOT, fuelItemStack.getContainerItem());
						}
					}
				}
			}

			if (blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
				++blockEntity.meltingProgress;
				if (blockEntity.meltingProgress == blockEntity.meltingTotalTime) {
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
			if(resultInput.is(FluidType.getFluidBucketItem(FluidType.FLUID_TYPES[blockEntity.fluidType]))) {
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
						blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(FluidType.getFluidBucketItem(FluidType.FLUID_TYPES[blockEntity.fluidType])));
					} else if(resultOutput.is(FluidType.getFluidBucketItem(FluidType.FLUID_TYPES[blockEntity.fluidType])) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
						resultInput.shrink(1);
						resultOutput.grow(1);
					} else {
						return;
					}
					blockEntity.fluidAmount -= FLUID_LEVEL_BUCKET;
				}
			} else if(blockEntity.fluidAmount <= 0 && MelterMenu.isFluidBucket(resultInput)) {
				blockEntity.fluidType = FluidType.getFluidFromBucketItem(resultInput.getItem()).getID();
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
		if (recipe.getFluidType().getID() != this.fluidType) {
			return this.fluidAmount == 0;
		}
		return recipe.getFluidAmount() + this.fluidAmount <= MAX_FLUID_LEVEL;
	}

	private boolean burn(@Nullable MelterRecipe recipe, NonNullList<ItemStack> container) {
		if (this.canBurn(recipe, container)) {
			ItemStack itemstack = container.get(0);
			if (recipe.getFluidType().getID() != this.fluidType) {
				this.fluidType = recipe.getFluidType().getID();
			}
			this.fluidAmount += recipe.getFluidAmount();

			itemstack.shrink(1);
			return true;
		}
		return false;
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.litTime = nbt.getInt("BurnTime");
		this.litDuration = this.getBurnDuration(this.items.get(1));
		this.meltingProgress = nbt.getInt("MeltTime");
		this.meltingTotalTime = nbt.getInt("MeltTimeTotal");
		this.fluidType = nbt.getInt("FluidType");
		this.fluidAmount = nbt.getInt("FluidAmount");
		CompoundTag compoundtag = nbt.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}

	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.meltingProgress);
		nbt.putInt("CookTimeTotal", this.meltingTotalTime);
		nbt.putInt("FluidType", this.fluidType);
		nbt.putInt("FluidAmount", this.fluidAmount);
		ContainerHelper.saveAllItems(nbt, this.items);
		CompoundTag compoundtag = new CompoundTag();
		this.recipesUsed.forEach((p_58382_, p_58383_) -> {
			compoundtag.putInt(p_58382_.toString(), p_58383_);
		});
		nbt.put("RecipesUsed", compoundtag);
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.melter");
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
	public boolean canPlaceItem(int index, ItemStack itemStack) {
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
		return level.getRecipeManager().getRecipeFor(ECRecipes.MELTER_TYPE, container, level).map(MelterRecipe::getMeltingTime).orElse(MelterRecipe.MELTING_TIME);
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
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		if (direction == Direction.UP) {
			return SLOTS_FOR_UP;
		}
		return SLOTS_FOR_SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
		if (direction == Direction.DOWN && index == 1) {
			return itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
		} else {
			return true;
		}
	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
			net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
		for (net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler> handler : handlers) {
			handler.invalidate();
		}
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new MelterMenu(id, inventory, this, this.dataAccess);
	}
}
