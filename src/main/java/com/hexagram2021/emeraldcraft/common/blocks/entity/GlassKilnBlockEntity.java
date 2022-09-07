package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.GlassKilnBlock;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnMenu;
import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GlassKilnBlockEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
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
	protected final IIntArray dataAccess = new IIntArray() {
		public int get(int index) {
			switch (index) {
				case DATA_LIT_TIME:
					return GlassKilnBlockEntity.this.litTime;
				case DATA_LIT_DURATION:
					return GlassKilnBlockEntity.this.litDuration;
				case DATA_COOKING_PROGRESS:
					return GlassKilnBlockEntity.this.cookingProgress;
				case DATA_COOKING_TOTAL_TIME:
					return GlassKilnBlockEntity.this.cookingTotalTime;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_LIT_TIME:
					GlassKilnBlockEntity.this.litTime = value;
					break;
				case DATA_LIT_DURATION:
					GlassKilnBlockEntity.this.litDuration = value;
					break;
				case DATA_COOKING_PROGRESS:
					GlassKilnBlockEntity.this.cookingProgress = value;
					break;
				case DATA_COOKING_TOTAL_TIME:
					GlassKilnBlockEntity.this.cookingTotalTime = value;
					break;
			}
		}

		public int getCount() {
			return NUM_DATA_VALUES;
		}
	};
	private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
	private final IRecipeType<GlassKilnRecipe> recipeType;


	public GlassKilnBlockEntity() {
		super(ECBlockEntity.GLASS_KILN.get());
		this.recipeType = ECRecipes.GLASS_KILN_TYPE;
	}

	@Override @Nonnull
	protected TranslationTextComponent getDefaultName() {
		return new TranslationTextComponent("container.glass_kiln");
	}


	@Override @Nonnull
	protected Container createMenu(int id, @Nonnull PlayerInventory inventory) {
		return new GlassKilnMenu(id, inventory, this, this.dataAccess);
	}

	private boolean isLit() {
		return this.litTime > 0;
	}

	@Override
	public void load(@Nonnull BlockState blockState, @Nonnull CompoundNBT nbt) {
		super.load(blockState, nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.items);
		this.litTime = nbt.getInt("BurnTime");
		this.cookingProgress = nbt.getInt("CookTime");
		this.cookingTotalTime = nbt.getInt("CookTimeTotal");
		this.litDuration = this.getBurnDuration(this.items.get(SLOT_FUEL));
		CompoundNBT compoundtag = nbt.getCompound("RecipesUsed");

		for(String s : compoundtag.getAllKeys()) {
			this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
		}
	}

	@Override @Nonnull
	public CompoundNBT save(@Nonnull CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("BurnTime", this.litTime);
		nbt.putInt("CookTime", this.cookingProgress);
		nbt.putInt("CookTimeTotal", this.cookingTotalTime);
		ItemStackHelper.saveAllItems(nbt, this.items);
		CompoundNBT compoundtag = new CompoundNBT();
		this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
		nbt.put("RecipesUsed", compoundtag);
		return nbt;
	}

	@Override
	public void tick() {
		boolean flag = this.isLit();
		boolean flag1 = false;
		if (this.isLit()) {
			--this.litTime;
		}

		ItemStack itemstack = this.items.get(SLOT_FUEL);
		if (this.isLit() || !itemstack.isEmpty() && !this.items.get(SLOT_INPUT).isEmpty()) {
			GlassKilnRecipe recipe = this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).orElse(null);
			int i = this.getMaxStackSize();
			if (!this.isLit() && this.canBurn(recipe, this.items, i)) {
				this.litTime = this.getBurnDuration(itemstack);
				this.litDuration = this.litTime;
				if (this.isLit()) {
					flag1 = true;
					if (itemstack.hasContainerItem())
						this.items.set(SLOT_FUEL, itemstack.getContainerItem());
					else
					if (!itemstack.isEmpty()) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							this.items.set(SLOT_FUEL, itemstack.getContainerItem());
						}
					}
				}
			}

			if (this.isLit() && this.canBurn(recipe, this.items, i)) {
				++this.cookingProgress;
				if (this.cookingProgress >= this.cookingTotalTime) {
					this.cookingProgress = 0;
					this.cookingTotalTime = getTotalCookTime(this.level, this.recipeType, this);
					if (this.burn(recipe, this.items, i)) {
						this.setRecipeUsed(recipe);
					}

					flag1 = true;
				}
			} else {
				this.cookingProgress = 0;
			}
		} else if (!this.isLit() && this.cookingProgress > 0) {
			this.cookingProgress = MathHelper.clamp(this.cookingProgress - BURN_COOL_SPEED, 0, this.cookingTotalTime);
		}

		if (flag != this.isLit()) {
			flag1 = true;
			BlockState blockState = this.level.getBlockState(this.worldPosition).setValue(GlassKilnBlock.LIT, this.isLit());
			this.level.setBlock(this.worldPosition, blockState, 3);
		}

		if (flag1) {
			this.setChanged();
		}
	}

	private boolean canBurn(@Nullable GlassKilnRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (!container.get(SLOT_INPUT).isEmpty() && recipe != null) {
			ItemStack itemstack = recipe.assemble(this);
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = container.get(SLOT_RESULT);
				if (itemstack1.isEmpty()) {
					return true;
				} else if (!itemstack1.sameItem(itemstack)) {
					return false;
				} else if (itemstack1.getCount() + itemstack.getCount() <= maxCount && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
					return true;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	private boolean burn(@Nullable GlassKilnRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
		if (recipe != null && this.canBurn(recipe, container, maxCount)) {
			ItemStack itemstack = container.get(SLOT_INPUT);
			ItemStack itemstack1 = recipe.assemble(this);
			ItemStack itemstack2 = container.get(SLOT_RESULT);
			if (itemstack2.isEmpty()) {
				container.set(SLOT_RESULT, itemstack1.copy());
			} else if (itemstack2.sameItem(itemstack1)) {
				itemstack2.grow(itemstack1.getCount());
			}

			itemstack.shrink(1);
			return true;
		} else {
			return false;
		}
	}

	protected int getBurnDuration(@Nonnull ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return 0;
		}
		return ForgeHooks.getBurnTime(itemStack) / 2;
	}

	private static int getTotalCookTime(World level, IRecipeType<GlassKilnRecipe> recipeType, IInventory container) {
		return level.getRecipeManager().getRecipeFor(recipeType, container, level).map(GlassKilnRecipe::getCookingTime).orElse(200);
	}

	@Override
	public int[] getSlotsForFace(@Nonnull Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		} else {
			return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack itemStack, @Nonnull Direction direction) {
		if (direction == Direction.DOWN && index == SLOT_FUEL) {
			Item item = itemStack.getItem();
			return item == Items.WATER_BUCKET || item == Items.BUCKET;
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

		if (index == SLOT_INPUT && !flag) {
			this.cookingTotalTime = getTotalCookTime(this.level, this.recipeType, this);
			this.cookingProgress = 0;
			this.setChanged();
		}

	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack itemStack) {
		if (index == SLOT_RESULT) {
			return false;
		}
		if (index != SLOT_FUEL) {
			return true;
		}
		ItemStack fuelItemStack = this.items.get(SLOT_FUEL);
		return ForgeHooks.getBurnTime(itemStack) > 0 || itemStack.getItem() == Items.BUCKET && fuelItemStack.getItem() != Items.BUCKET;
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
	}

	public void awardUsedRecipesAndPopExperience(ServerPlayerEntity player) {
		List<IRecipe<?>> list = this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position());
		player.awardRecipes(list);
		this.recipesUsed.clear();
	}

	public List<IRecipe<?>> getRecipesToAwardAndPopExperience(ServerWorld level, Vector3d pos) {
		List<IRecipe<?>> list = Lists.newArrayList();

		for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
			this.level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				createExperience(level, pos, entry.getIntValue(), ((GlassKilnRecipe)recipe).getExperience());
			});
		}

		return list;
	}

	private static void createExperience(ServerWorld level, Vector3d pos, int count, float exp) {
		int i = MathHelper.floor((float)count * exp);
		float f = MathHelper.frac((float)count * exp);
		if (f != 0.0F && Math.random() < (double)f) {
			++i;
		}

		while(i > 0) {
			int j = ExperienceOrbEntity.getExperienceValue(i);
			i -= j;
			level.addFreshEntity(new ExperienceOrbEntity(level, pos.x, pos.y, pos.z, j));
		}
	}

	@Override
	public void fillStackedContents(@Nonnull RecipeItemHelper contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}

	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
