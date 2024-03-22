package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.MineralTableBlock;
import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.MineralTableMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MineralTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible {
	protected static final int SLOT_INPUT = 0;
	protected static final int SLOT_FUEL = 1;
	protected static final int SLOT_RESULT = 2;
	public static final int NUM_SLOTS = 3;
	public static final int DATA_LIT_TIME = 0;
	public static final int DATA_LIT_DURATION = 1;
	public static final int DATA_COOKING_PROGRESS = 2;
	public static final int DATA_COOKING_TOTAL_TIME = 3;
	public static final int NUM_DATA_VALUES = 4;
	private static final int[] SLOTS_FOR_UP = new int[]{SLOT_INPUT};
	private static final int[] SLOTS_FOR_DOWN = new int[]{SLOT_RESULT, SLOT_FUEL};
	private static final int[] SLOTS_FOR_SIDES = new int[]{SLOT_FUEL};
	public static final int BURN_COOL_SPEED = 2;
	protected NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
	int litTime;
	int litDuration;
	int cookingProgress;
	int cookingTotalTime;
	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case DATA_LIT_TIME -> MineralTableBlockEntity.this.litTime;
				case DATA_LIT_DURATION -> MineralTableBlockEntity.this.litDuration;
				case DATA_COOKING_PROGRESS -> MineralTableBlockEntity.this.cookingProgress;
				case DATA_COOKING_TOTAL_TIME -> MineralTableBlockEntity.this.cookingTotalTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_LIT_TIME -> MineralTableBlockEntity.this.litTime = value;
				case DATA_LIT_DURATION -> MineralTableBlockEntity.this.litDuration = value;
				case DATA_COOKING_PROGRESS -> MineralTableBlockEntity.this.cookingProgress = value;
				case DATA_COOKING_TOTAL_TIME -> MineralTableBlockEntity.this.cookingTotalTime = value;
			}

		}

		public int getCount() {
			return NUM_DATA_VALUES;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final RecipeManager.CachedCheck<Container, MineralTableRecipe> quickCheck;
	
	public MineralTableBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.MINERAL_TABLE.get(), pos, state);
		this.quickCheck = RecipeManager.createCheck(ECRecipes.MINERAL_TABLE_TYPE.get());
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.mineral_table");
	}


	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public void load(CompoundTag nbt) {
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
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.cookingProgress);
		nbt.putInt("CookTimeTotal", this.cookingTotalTime);
		ContainerHelper.saveAllItems(nbt, this.items);
		CompoundTag compoundtag = new CompoundTag();
		this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
		nbt.put("RecipesUsed", compoundtag);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState blockState, MineralTableBlockEntity blockEntity) {
		boolean isBurning = blockEntity.isLit();
		boolean changed = false;
		if (blockEntity.isLit()) {
			--blockEntity.litTime;
		}

		ItemStack fuelItemStack = blockEntity.items.get(SLOT_FUEL);
		boolean inputExists = !blockEntity.items.get(SLOT_INPUT).isEmpty();
		boolean fuelExists = !fuelItemStack.isEmpty();
		if (blockEntity.isLit() || fuelExists && inputExists) {
			RecipeHolder<MineralTableRecipe> recipeholder;
			if (inputExists) {
				recipeholder = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
			} else {
				recipeholder = null;
			}
			int maxStackSize = blockEntity.getMaxStackSize();
			if (!blockEntity.isLit() && blockEntity.canBurn(level.registryAccess(), recipeholder, blockEntity.items, maxStackSize)) {
				blockEntity.litTime = blockEntity.getBurnDuration(fuelItemStack);
				blockEntity.litDuration = blockEntity.litTime;
				if (blockEntity.isLit()) {
					changed = true;
					if (fuelItemStack.hasCraftingRemainingItem()) {
						blockEntity.items.set(SLOT_FUEL, fuelItemStack.getCraftingRemainingItem());
					} else if (fuelExists) {
						fuelItemStack.shrink(1);
						if (fuelItemStack.isEmpty()) {
							blockEntity.items.set(SLOT_FUEL, fuelItemStack.getCraftingRemainingItem());
						}
					}
				}
			}

			if (blockEntity.isLit() && blockEntity.canBurn(level.registryAccess(), recipeholder, blockEntity.items, maxStackSize)) {
				++blockEntity.cookingProgress;
				if (blockEntity.cookingProgress >= blockEntity.cookingTotalTime) {
					blockEntity.cookingProgress = 0;
					blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
					if (blockEntity.burn(level.registryAccess(), recipeholder, blockEntity.items, maxStackSize)) {
						blockEntity.setRecipeUsed(recipeholder);
					}

					changed = true;
				}
			} else {
				blockEntity.cookingProgress = 0;
			}
		} else if (!blockEntity.isLit() && blockEntity.cookingProgress > 0) {
			blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - BURN_COOL_SPEED, 0, blockEntity.cookingTotalTime);
		}

		if (isBurning != blockEntity.isLit()) {
			changed = true;
			blockState = blockState.setValue(MineralTableBlock.LIT, blockEntity.isLit());
			level.setBlock(pos, blockState, 3);
		}

		if (changed) {
			setChanged(level, pos, blockState);
		}

	}

	@Contract("_,null,_,_->false")
	private boolean canBurn(RegistryAccess registryAccess, @Nullable RecipeHolder<MineralTableRecipe> recipe, NonNullList<ItemStack> container, int maxCount) {
		if (!container.get(SLOT_INPUT).isEmpty() && recipe != null) {
			ItemStack result = recipe.value().assemble(this, registryAccess);
			if (result.isEmpty()) {
				return false;
			}
			ItemStack resultSlot = container.get(SLOT_RESULT);
			if (resultSlot.isEmpty()) {
				return true;
			}
			if (!ItemStack.isSameItem(resultSlot, result)) {
				return false;
			}
			if (resultSlot.getCount() + result.getCount() <= maxCount && resultSlot.getCount() + result.getCount() <= resultSlot.getMaxStackSize()) {
				return true;
			}
			return resultSlot.getCount() + result.getCount() <= result.getMaxStackSize();
		}
		return false;
	}

	private boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<MineralTableRecipe> recipe, NonNullList<ItemStack> container, int maxCount) {
		if (this.canBurn(registryAccess, recipe, container, maxCount)) {
			ItemStack inputSlot = container.get(SLOT_INPUT);
			ItemStack result = recipe.value().assemble(this, registryAccess);
			ItemStack resultSlot = container.get(SLOT_RESULT);
			if (resultSlot.isEmpty()) {
				container.set(SLOT_RESULT, result.copy());
			} else if (resultSlot.is(result.getItem())) {
				resultSlot.grow(result.getCount());
			}

			inputSlot.shrink(1);
			return true;
		}
		return false;
	}

	protected int getBurnDuration(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return itemStack.is(Items.MAGMA_CREAM) ? MineralTableRecipe.BURN_TIME * 20 : 0;
	}

	private static int getTotalCookTime(Level level, MineralTableBlockEntity blockEntity) {
		return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(recipeHolder -> recipeHolder.value().getCookingTime()).orElse(MineralTableRecipe.BURN_TIME);
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new MineralTableMenu(id, inventory, this, this.dataAccess);
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		} else {
			return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
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
		boolean flag = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, itemstack);
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}

		if (index == SLOT_INPUT && !flag) {
			this.cookingTotalTime = getTotalCookTime(Objects.requireNonNull(this.level), this);
			this.cookingProgress = 0;
			this.setChanged();
		}

	}

	@Override
	public boolean stillValid(Player player) {
		if (Objects.requireNonNull(this.level).getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		if (index == MineralTableMenu.RESULT_SLOT) {
			return false;
		}
		if (index != MineralTableMenu.FUEL_SLOT) {
			return true;
		}
		return itemStack.is(Items.MAGMA_CREAM);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.id();
			this.recipesUsed.addTo(resourcelocation, 1);
		}
	}

	@Override @Nullable
	public RecipeHolder<MineralTableRecipe> getRecipeUsed() {
		return null;
	}

	@Override
	public void awardUsedRecipes(Player player, List<ItemStack> items) {
	}

	public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
		List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience(player.serverLevel(), player.position());
		player.awardRecipes(list);
		this.recipesUsed.clear();
	}

	public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
		List<RecipeHolder<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipeHolder -> {
				list.add(recipeHolder);
				createExperience(level, pos, entry.getIntValue(), ((MineralTableRecipe)recipeHolder.value()).getExperience());
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
	public void fillStackedContents(StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}

	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
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
