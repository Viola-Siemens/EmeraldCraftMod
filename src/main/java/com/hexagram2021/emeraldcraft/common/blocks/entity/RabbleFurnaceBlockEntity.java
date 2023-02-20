package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.RabbleFurnaceBlock;
import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.RabbleFurnaceMenu;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class RabbleFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_MIX1 = 1;
	public static final int SLOT_MIX2 = 2;
	public static final int SLOT_FUEL = 3;
	public static final int SLOT_RESULT = 4;
	public static final int NUM_SLOTS = 5;
	protected static final int DATA_LIT_TIME = 0;
	protected static final int DATA_LIT_DURATION = 1;
	protected static final int DATA_COOKING_PROGRESS = 2;
	protected static final int DATA_COOKING_TOTAL_TIME = 3;
	public static final int NUM_DATA_VALUES = 4;
	private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT, SLOT_MIX1, SLOT_MIX2};
	private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT, SLOT_FUEL};
	private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL, SLOT_MIX1, SLOT_MIX2};
	public static final int BURN_COOL_SPEED = 2;
	protected NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
	int litTime;
	int litDuration;
	int cookingProgress;
	int cookingTotalTime;
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case DATA_LIT_TIME -> RabbleFurnaceBlockEntity.this.litTime;
				case DATA_LIT_DURATION -> RabbleFurnaceBlockEntity.this.litDuration;
				case DATA_COOKING_PROGRESS -> RabbleFurnaceBlockEntity.this.cookingProgress;
				case DATA_COOKING_TOTAL_TIME -> RabbleFurnaceBlockEntity.this.cookingTotalTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_LIT_TIME -> RabbleFurnaceBlockEntity.this.litTime = value;
				case DATA_LIT_DURATION -> RabbleFurnaceBlockEntity.this.litDuration = value;
				case DATA_COOKING_PROGRESS -> RabbleFurnaceBlockEntity.this.cookingProgress = value;
				case DATA_COOKING_TOTAL_TIME -> RabbleFurnaceBlockEntity.this.cookingTotalTime = value;
			}

		}

		public int getCount() {
			return NUM_DATA_VALUES;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final RecipeType<RabbleFurnaceRecipe> recipeType;


	public RabbleFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.RABBLE_FURNACE.get(), pos, state);
		this.recipeType = ECRecipes.RABBLE_FURNACE_TYPE.get();
	}

	@Override @NotNull
	protected Component getDefaultName() {
		return Component.translatable("container.rabble_furnace");
	}


	@Override @NotNull
	protected AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
		return new RabbleFurnaceMenu(id, inventory, this, this.dataAccess);
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.litTime = nbt.getInt("BurnTime");
		this.cookingProgress = nbt.getInt("CookTime");
		this.cookingTotalTime = nbt.getInt("CookTimeTotal");
		this.litDuration = this.getBurnDuration(this.items.get(SLOT_FUEL));
		CompoundTag compoundtag = nbt.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.cookingProgress);
		nbt.putInt("CookTimeTotal", this.cookingTotalTime);
		ContainerHelper.saveAllItems(nbt, this.items);
		CompoundTag compoundtag = new CompoundTag();
		this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
		nbt.put("RecipesUsed", compoundtag);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, RabbleFurnaceBlockEntity blockEntity) {
		boolean flag = blockEntity.isLit();
		boolean flag1 = false;
		if (blockEntity.isLit()) {
			--blockEntity.litTime;
		}

		ItemStack itemstack = blockEntity.items.get(SLOT_FUEL);
		if (blockEntity.isLit() || !itemstack.isEmpty() && !blockEntity.items.get(SLOT_INPUT).isEmpty()) {
			RabbleFurnaceRecipe recipe = level.getRecipeManager().getRecipeFor(blockEntity.recipeType, blockEntity, level).orElse(null);
			int i = blockEntity.getMaxStackSize();
			if (!blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items, i)) {
				blockEntity.litTime = blockEntity.getBurnDuration(itemstack);
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					flag1 = true;
					if (itemstack.hasCraftingRemainingItem()) {
						blockEntity.items.set(SLOT_FUEL, itemstack.getCraftingRemainingItem());
					} else if (!itemstack.isEmpty()) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							blockEntity.items.set(SLOT_FUEL, itemstack.getCraftingRemainingItem());
						}
					}
				}
			}

			if (blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items, i)) {
				++blockEntity.cookingProgress;
				if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity.recipeType, blockEntity);
					if (blockEntity.burn(recipe, blockEntity.items, i)) {
						blockEntity.setRecipeUsed(recipe);
					}

					flag1 = true;
				}
			} else {
				blockEntity.cookingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
			blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - BURN_COOL_SPEED, 0, blockEntity.cookingTotalTime);
		}

		if (flag != blockEntity.isLit()) {
			flag1 = true;
			blockState = blockState.setValue(RabbleFurnaceBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, blockState, Block.UPDATE_ALL);
		}

		if (flag1) {
			setChanged(level, pos, blockState);
		}

	}

	private boolean canBurn(@Nullable RabbleFurnaceRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (!container.get(SLOT_INPUT).isEmpty() && recipe != null) {
			ItemStack itemstack = recipe.assemble(this);
			if (itemstack.isEmpty()) {
				return false;
			}
			ItemStack resultSlot = container.get(SLOT_RESULT);
			if (resultSlot.isEmpty()) {
				return true;
			}
			if (!resultSlot.sameItem(itemstack)) {
				return false;
			}
			if (resultSlot.getCount() + itemstack.getCount() <= maxCount && resultSlot.getCount() + itemstack.getCount() <= resultSlot.getMaxStackSize()) {
				return true;
			}
			return resultSlot.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
		}
		return false;
	}

	private boolean burn(@Nullable RabbleFurnaceRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (recipe != null && this.canBurn(recipe, container, maxCount)) {
			ItemStack input = container.get(SLOT_INPUT);
			ItemStack mix1 = container.get(SLOT_MIX1);
			ItemStack mix2 = container.get(SLOT_MIX2);
			ItemStack itemstack1 = recipe.assemble(this);
			ItemStack resultSlot = container.get(SLOT_RESULT);
			if (resultSlot.isEmpty()) {
				container.set(SLOT_RESULT, itemstack1.copy());
			} else if (resultSlot.is(itemstack1.getItem())) {
				resultSlot.grow(itemstack1.getCount());
			}

			input.shrink(1);
			if(!mix1.isEmpty()) {
				mix1.shrink(1);
			}
			if(!mix2.isEmpty()) {
				mix2.shrink(1);
			}
			return true;
		}
		return false;
	}

	protected int getBurnDuration(@NotNull ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return ForgeHooks.getBurnTime(itemStack, this.recipeType) / 2;
	}

	private static int getTotalCookTime(Level level, RecipeType<RabbleFurnaceRecipe> recipeType, Container container) {
		return level.getRecipeManager().getRecipeFor(recipeType, container, level).map(RabbleFurnaceRecipe::getRabblingTime).orElse(200);
	}

	@Override
	public int[] getSlotsForFace(@NotNull Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		} else {
			return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @NotNull ItemStack itemStack, @NotNull Direction direction) {
		if (direction == Direction.DOWN && index == SLOT_FUEL) {
			return itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
		}
		return true;
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
		ItemStack slot = this.items.get(index);
		boolean flag = !itemStack.isEmpty() && itemStack.sameItem(slot) && ItemStack.tagMatches(itemStack, slot);
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}

		if ((index == SLOT_INPUT || index == SLOT_MIX1 || index == SLOT_MIX2) && !flag) {
			this.cookingTotalTime = getTotalCookTime(this.level, this.recipeType, this);
			this.cookingProgress = 0;
			this.setChanged();
		}

	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean canPlaceItem(int index, @NotNull ItemStack itemStack) {
		if (index == SLOT_RESULT) {
			return false;
		}
		if (index != SLOT_FUEL) {
			return true;
		}
		ItemStack fuelItemStack = this.items.get(SLOT_FUEL);
		return ForgeHooks.getBurnTime(itemStack, this.recipeType) > 0 || itemStack.is(Items.BUCKET) && !fuelItemStack.is(Items.BUCKET);
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
	}

	public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
		List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position());
		player.awardRecipes(list);
		this.recipesUsed.clear();
	}

	public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
		List<Recipe<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			this.level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				createExperience(level, pos, entry.getIntValue(), ((RabbleFurnaceRecipe)recipe).getExperience());
			});
		}

		return list;
	}

	private static void createExperience(ServerLevel level, Vec3 pos, int count, float exp) {
		int i = Mth.floor((float)count * exp);
		float f = Mth.frac((float)count * exp);
		if (f != 0.0F && Math.random() < (double)f) {
			++i;
		}

		ExperienceOrb.award(level, pos, i);
	}

	@Override
	public void fillStackedContents(@NotNull StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}

	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
			if (facing == Direction.UP)
				return handlers[0].cast();
			else if (facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		for (LazyOptional<? extends IItemHandler> handler : handlers)
			handler.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}
}
