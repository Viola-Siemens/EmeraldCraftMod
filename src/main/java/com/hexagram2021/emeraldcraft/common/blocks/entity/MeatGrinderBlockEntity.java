package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.crafting.MeatGrinderRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MeatGrinderBlockEntity extends BlockEntity implements Container, WorldlyContainer, StackedContentsCompatible {
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_RESULT = 1;
	protected static final int COUNT_SLOTS = 2;
	private final NonNullList<ItemStack> items = NonNullList.withSize(COUNT_SLOTS, ItemStack.EMPTY);

	private static final int[] SLOTS_FOR_UP = new int[]{0};
	private static final int[] SLOTS_FOR_SIDES = new int[]{0, 1};
	private static final int[] SLOTS_FOR_DOWN = new int[]{1};

	int progressTicks = 0;
	int totalTicks = 0;

	private final RecipeManager.CachedCheck<Container, MeatGrinderRecipe> quickCheck;

	public MeatGrinderBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.MEAT_GRINDER.get(), pos, state);
		this.quickCheck = RecipeManager.createCheck(ECRecipes.MEAT_GRINDER_TYPE.get());
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, MeatGrinderBlockEntity blockEntity) {
		ItemStack input = blockEntity.getItem(SLOT_INPUT);
		ItemStack result = blockEntity.getItem(SLOT_RESULT);
		RecipeHolder<MeatGrinderRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
		if (recipeHolder == null || input.isEmpty()) {
			if(blockEntity.progressTicks != 0 || blockEntity.totalTicks != 0) {
				blockEntity.progressTicks = 0;
				blockEntity.totalTicks = 0;
				if(!level.isClientSide) {
					blockEntity.setChanged();
				}
			}
			return;
		}
		MeatGrinderRecipe recipe = recipeHolder.value();
		ItemStack target = recipe.assemble(blockEntity, level.registryAccess());
		boolean emptyResult = result.isEmpty();
		boolean sameResult = ItemStack.isSameItemSameTags(target, result) && target.getCount() + result.getCount() <= blockEntity.getMaxStackSize();
		if(emptyResult || sameResult) {
			if(blockEntity.totalTicks != recipe.getCookingTime()) {
				blockEntity.totalTicks = recipe.getCookingTime();
				blockEntity.progressTicks = 0;
				if(!level.isClientSide) {
					level.playSound(null, blockPos, ECSounds.VILLAGER_WORK_HUNTER, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			blockEntity.progressTicks += 1;
			if(level.isClientSide) {
				blockEntity.spawnItemParticles(input, blockPos);
			}
			if(blockEntity.progressTicks >= blockEntity.totalTicks) {
				if(!level.isClientSide) {
					if (sameResult) {
						result.grow(target.getCount());
					} else {
						blockEntity.setItem(SLOT_RESULT, target);
					}
					input.shrink(1);
					blockEntity.setChanged();
				}
				blockEntity.progressTicks = 0;
				blockEntity.totalTicks = 0;
			}
		}
	}

	private static final int ITEM_PARTICLE_AMOUNT = 4;
	private void spawnItemParticles(ItemStack itemStack, BlockPos blockPos) {
		if(this.level != null) {
			for (int i = 0; i < ITEM_PARTICLE_AMOUNT; ++i) {
				Vec3 speed = new Vec3(((double) this.level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.2D + 0.2D, 0.0D);
				double y = (double)this.level.random.nextFloat() * 0.4D + 0.1D;
				Vec3 position = new Vec3(
						((double) this.level.random.nextFloat() - 0.5D) * 0.3D + blockPos.getX() + 0.5D,
						y + blockPos.getY(),
						((double) this.level.random.nextFloat() - 0.5D) * 0.3D + blockPos.getZ() + 0.5D
				);
				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemStack), position.x, position.y, position.z, speed.x, speed.y + 0.05D, speed.z);
			}
		}
	}

	public boolean isWorking() {
		return this.totalTicks > 0 && !this.getItem(SLOT_INPUT).isEmpty();
	}

	public float getProgress() {
		return 1.0F - (float)this.progressTicks / (float)this.totalTicks;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items.clear();
		ContainerHelper.loadAllItems(nbt, this.items);
		this.progressTicks = nbt.getInt("progressTicks");
		this.totalTicks = nbt.getInt("totalTicks");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, this.items, true);
		nbt.putInt("progressTicks", this.progressTicks);
		nbt.putInt("totalTicks", this.totalTicks);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
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

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		return index == SLOT_INPUT && this.level != null && this.quickCheck.getRecipeFor(new SimpleContainer(itemStack), this.level).isPresent();
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if(direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		if(direction == Direction.UP) {
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
		return true;
	}

	//Forge Compat
	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @NotNull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
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
}
