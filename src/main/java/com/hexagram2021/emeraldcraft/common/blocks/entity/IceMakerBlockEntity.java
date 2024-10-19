package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.IceMakerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IceMakerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class IceMakerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, Tank, ISynchronizableContainer {
	public static final int MAX_INGREDIENT_FLUID_LEVEL = 1000;
	public static final int MAX_CONDENSATE_FLUID_LEVEL = 800;
	public static final int WATER_BUCKET_CONDENSATE_LEVEL = 100;
	public static final int FLUID_LEVEL_BUCKET = 100;
	public static final int TANK_INPUT = 0;
	public static final int TANK_CONDENSATE = 1;
	public static final int COUNT_TANKS = 2;

	private static final int[] SLOTS_FOR_UP = new int[]{0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
	private static final int[] SLOTS_FOR_SIDES = new int[]{2, 0};

	protected NonNullList<ItemStack> items = NonNullList.withSize(IceMakerMenu.SLOT_COUNT, ItemStack.EMPTY);
	final FluidTank tank = new FluidTank(MAX_INGREDIENT_FLUID_LEVEL) {
		@Override
		protected void onContentsChanged() {
			super.onContentsChanged();
			IceMakerBlockEntity.this.markDirty();
		}
	};
	final FluidTank tankCondensate = new FluidTank(MAX_CONDENSATE_FLUID_LEVEL) {
		@Override
		protected void onContentsChanged() {
			super.onContentsChanged();
			IceMakerBlockEntity.this.markDirty();
		}
	};
	int freezingProgress;
	int freezingTotalTime;

	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case 0 -> IceMakerBlockEntity.this.freezingProgress;
				case 1 -> IceMakerBlockEntity.this.freezingTotalTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case 0 -> IceMakerBlockEntity.this.freezingProgress = value;
				case 1 -> IceMakerBlockEntity.this.freezingTotalTime = value;
			}
		}

		public int getCount() {
			return IceMakerMenu.DATA_COUNT;
		}
	};
	private final RecipeManager.CachedCheck<Container, IceMakerRecipe> quickCheck;

	public IceMakerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.ICE_MAKER.get(), pos, state);
		this.quickCheck = RecipeManager.createCheck(ECRecipes.ICE_MAKER_TYPE.get());
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.ice_maker");
	}

	private boolean hasInput() {
		return this.tank.getFluidAmount() > 0;
	}
	private boolean isLit() {
		return this.tankCondensate.getFluidAmount() > 0;
	}

	@SuppressWarnings({"DataFlowIssue", "ConstantValue"})
	public static void serverTick(Level level, BlockPos pos, BlockState blockState, IceMakerBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit() && blockEntity.hasInput();
		boolean changed = false;

		ItemStack condensateItemStack = blockEntity.items.get(IceMakerMenu.CONDENSATE_SLOT);
		if(blockEntity.tankCondensate.getFluidAmount() <= MAX_CONDENSATE_FLUID_LEVEL - WATER_BUCKET_CONDENSATE_LEVEL * 2 && condensateItemStack.getCount() == 1) {
			condensateItemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(c -> {
				FluidStack itemFluid = c.getFluidInTank(0);
				if(!itemFluid.isEmpty() && (blockEntity.tankCondensate.isEmpty() || blockEntity.tankCondensate.getFluid().isFluidEqual(itemFluid))) {
					blockEntity.tankCondensate.fill(new FluidStack(itemFluid, WATER_BUCKET_CONDENSATE_LEVEL * 2), IFluidHandler.FluidAction.EXECUTE);
					blockEntity.items.set(IceMakerMenu.CONDENSATE_SLOT, new ItemStack(Items.BUCKET));
				}
			});
		}

		boolean inputExists = blockEntity.hasInput();
		if (blockEntity.isLit() && inputExists) {
			RecipeHolder<IceMakerRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);

			if (blockEntity.canFreeze(level.registryAccess(), recipeHolder, blockEntity.items, blockEntity.getMaxStackSize())) {
				++blockEntity.freezingProgress;
				blockEntity.tankCondensate.drain(1, IFluidHandler.FluidAction.EXECUTE);
				if (blockEntity.freezingProgress >= blockEntity.freezingTotalTime) {
					blockEntity.freezingProgress = 0;
					blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
					blockEntity.freeze(level.registryAccess(), recipeHolder, blockEntity.items, blockEntity.getMaxStackSize());

					changed = true;
				}
			} else {
				blockEntity.freezingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.freezingProgress > 0) {
			blockEntity.freezingProgress = Mth.clamp(blockEntity.freezingProgress - 2, 0, blockEntity.freezingTotalTime);
		}

		boolean nextFlag = blockEntity.isLit() && blockEntity.hasInput();
		if (flag != nextFlag) {
			changed = true;
			blockState = blockState.setValue(IceMakerBlock.LIT, nextFlag);
			level.setBlock(pos, blockState, Block.UPDATE_ALL);
		}

		ItemStack ingredientInput = blockEntity.items.get(IceMakerMenu.INGREDIENT_INPUT_SLOT);
		ItemStack ingredientOutput = blockEntity.items.get(IceMakerMenu.INGREDIENT_OUTPUT_SLOT);
		if(!ingredientInput.isEmpty()) {
			FluidStack inputFluidStack = blockEntity.tank.getFluid();
			Item inputBucketItem = inputFluidStack.getFluid().getBucket();
			if(ingredientInput.is(inputBucketItem)) {
				if(inputFluidStack.getAmount() <= MAX_INGREDIENT_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientOutput.grow(1);
					} else {
						return;
					}
					ingredientInput.shrink(1);
					inputFluidStack.grow(FLUID_LEVEL_BUCKET);
					changed = true;
				}
			} else if(ingredientInput.is(Items.BUCKET)) {
				if(inputFluidStack.getAmount() >= FLUID_LEVEL_BUCKET) {
					if(ingredientOutput.isEmpty()) {
						blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(inputBucketItem));
					} else if(ingredientOutput.is(inputBucketItem) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientOutput.grow(1);
					} else {
						return;
					}
					ingredientInput.shrink(1);
					inputFluidStack.shrink(FLUID_LEVEL_BUCKET);
					changed = true;
				}
			} else if(inputFluidStack.getAmount() <= 0) {
				IFluidHandlerItem c = ingredientInput.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
				if(c != null) {
					blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
					if(ingredientOutput.isEmpty()) {
						blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
					} else if(ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
						ingredientOutput.grow(1);
					} else {
						return;
					}
					blockEntity.tank.fill(new FluidStack(c.getFluidInTank(0), FLUID_LEVEL_BUCKET), IFluidHandler.FluidAction.EXECUTE);
					ingredientInput.shrink(1);
					changed = true;
				}
			}
		}

		if (changed) {
			blockEntity.markDirty();
			setChanged(level, pos, blockState);
		}
	}

	@Contract("_,null,_,_->false")
	private boolean canFreeze(RegistryAccess registryAccess, @Nullable RecipeHolder<IceMakerRecipe> recipeHolder, NonNullList<ItemStack> container, int maxCount) {
		if (recipeHolder == null) {
			return false;
		}
		ItemStack result = recipeHolder.value().assemble(this, registryAccess);
		if (result.isEmpty()) {
			return false;
		}
		ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
		if(itemstack.isEmpty()) {
			return true;
		}
		if(!ItemStack.isSameItem(itemstack, result)) {
			return false;
		}
		if(itemstack.getCount() + result.getCount() <= maxCount && itemstack.getCount() + result.getCount() <= itemstack.getMaxStackSize()) {
			return true;
		}
		return itemstack.getCount() + result.getCount() <= result.getMaxStackSize();
	}

	@SuppressWarnings("UnusedReturnValue")
	private boolean freeze(RegistryAccess registryAccess, @Nullable RecipeHolder<IceMakerRecipe> recipeHolder, NonNullList<ItemStack> container, int maxCount) {
		if (this.canFreeze(registryAccess, recipeHolder, container, maxCount)) {
			IceMakerRecipe recipe = recipeHolder.value();
			ItemStack result = recipe.assemble(this, registryAccess);
			ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
			if(itemstack.isEmpty()) {
				container.set(IceMakerMenu.RESULT_SLOT, result.copy());
			} else if (ItemStack.isSameItem(itemstack, result)) {
				itemstack.grow(result.getCount());
			}

			this.tank.drain(recipe.inputFluid(), IFluidHandler.FluidAction.EXECUTE);
			return true;
		}
		return false;
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	private static final String INPUT_FLUID_TAG = "Input";
	private static final String CONDENSATE_FLUID_TAG = "Condensate";
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		if(nbt.contains(INPUT_FLUID_TAG, Tag.TAG_COMPOUND)) {
			this.tank.readFromNBT(nbt.getCompound(INPUT_FLUID_TAG));
		}
		this.freezingProgress = nbt.getInt("FreezingProgress");
		this.freezingTotalTime = nbt.getInt("FreezingTimeTotal");
		if(nbt.contains(CONDENSATE_FLUID_TAG, Tag.TAG_COMPOUND)) {
			this.tank.readFromNBT(nbt.getCompound(CONDENSATE_FLUID_TAG));
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put(INPUT_FLUID_TAG, this.tank.writeToNBT(new CompoundTag()));
		nbt.putInt("FreezingProgress", this.freezingProgress);
		nbt.putInt("FreezingTimeTotal", this.freezingTotalTime);
		nbt.put(CONDENSATE_FLUID_TAG, this.tankCondensate.writeToNBT(new CompoundTag()));
		ContainerHelper.saveAllItems(nbt, this.items);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
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
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		if (index == IceMakerMenu.RESULT_SLOT) {
			return false;
		}
		if (index == IceMakerMenu.CONDENSATE_SLOT) {
			return itemStack.is(Items.BUCKET) || itemStack.is(Items.WATER_BUCKET);
		}
		return itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
	}

	private static int getTotalFreezeTime(Level level, IceMakerBlockEntity blockEntity) {
		return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(recipeHolder -> recipeHolder.value().freezingTime()).orElse(IceMakerRecipe.FREEZING_TIME);
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
		if (direction == Direction.DOWN && index == IceMakerMenu.CONDENSATE_SLOT) {
			return itemStack.is(Items.BUCKET);
		}
		return true;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new IceMakerMenu(id, inventory, this, this.dataAccess);
	}

	@Override
	public FluidStack getFluidStack(int tank) {
		return switch(tank) {
			case TANK_INPUT -> this.tank.getFluid();
			case TANK_CONDENSATE -> this.tankCondensate.getFluid();
			default -> throw new IndexOutOfBoundsException(tank);
		};
	}

	@Override
	public void setFluidStack(int tank, FluidStack fluidStack) {
		switch(tank) {
			case TANK_INPUT -> this.tank.setFluid(fluidStack);
			case TANK_CONDENSATE -> this.tankCondensate.setFluid(fluidStack);
			default -> throw new IndexOutOfBoundsException(tank);
		}
	}

	@Override
	public int getTankSize() {
		return COUNT_TANKS;
	}

	private boolean dirty = false;
	@Override
	public void markDirty() {
		this.dirty = true;
	}

	@Override
	public void clearDirty() {
		this.dirty = false;
	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	@Override
	public ClientboundFluidSyncPacket getSyncPacket() {
		return new ClientboundFluidSyncPacket(List.of(this.tank.getFluid(), this.tankCondensate.getFluid()));
	}

	//Forge Compat
	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	private final LazyOptional<IFluidHandler> fluidHandlerWrapper = LazyOptional.of(() -> this.tank);
	private final LazyOptional<IFluidHandler> condensateFluidHandlerWrapper = LazyOptional.of(() -> this.tankCondensate);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove) {
			if(facing == null) {
				if(capability == ForgeCapabilities.FLUID_HANDLER_ITEM) {
					return this.fluidHandlerWrapper.cast();
				}
			} else {
				if(capability == ForgeCapabilities.ITEM_HANDLER) {
					if (facing == Direction.UP) {
						return this.handlers[0].cast();
					}
					if (facing == Direction.DOWN) {
						return this.handlers[1].cast();
					}
					return this.handlers[2].cast();
				}
				if(capability == ForgeCapabilities.FLUID_HANDLER_ITEM) {
					if (facing == Direction.UP || facing == Direction.DOWN) {
						return this.fluidHandlerWrapper.cast();
					}
					return this.condensateFluidHandlerWrapper.cast();
				}
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
			handler.invalidate();
		}
		this.fluidHandlerWrapper.invalidate();
		this.condensateFluidHandlerWrapper.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}
}
