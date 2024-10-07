package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.CookstoveBlock;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.common.util.PartialRecipeCachedCheck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

public class CookstoveBlockEntity extends BlockEntity implements Container, StackedContentsCompatible, Tank {
	public static final int SLOT_INPUT_START = 0;
	public static final int COUNT_SLOTS = 8;
	public static final int MAX_TANK_CAPABILITY = 100;
	public static final int TANK_INPUT = 0;
	public static final int COUNT_TANKS = 1;
	public static final int MAX_FUEL = 2000;
	public static final int[] DEFAULT_PLACE_ORDER = {0, 3, 6, 1, 4, 7, 2, 5};

	private final NonNullList<ItemStack> items = NonNullList.withSize(COUNT_SLOTS, ItemStack.EMPTY);
	private final SimpleContainer shadowedContainer = new SimpleContainer(COUNT_SLOTS);
	@Nullable
	private Ingredient container = null;
	private ItemStack result = ItemStack.EMPTY;
	@Nullable
	private CookstoveDisplay display;
	private int fuel = 0;
	public int animateTick = 0;
	private final FluidTank tank = new FluidTank(MAX_TANK_CAPABILITY);

	int progressTicks = 0;
	int totalTicks = 0;

	private final RecipeManager.CachedCheck<CookstoveBlockEntity, CookstoveRecipe> quickCheck;
	private final PartialRecipeCachedCheck<Container, CookstoveRecipe> partialQuickCheck;

	@Nullable
	private CookstoveRecipe currentRecipe = null;

	public CookstoveBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ECBlockEntity.COOKSTOVE.get(), blockPos, blockState);
		this.quickCheck = RecipeManager.createCheck(ECRecipes.COOKSTOVE_TYPE.get());
		this.partialQuickCheck = PartialRecipeCachedCheck.createCheck(ECRecipes.COOKSTOVE_TYPE.get());
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, CookstoveBlockEntity blockEntity) {
		ItemStack result = blockEntity.getResult();
		if(blockEntity.currentRecipe == null) {
			if(blockEntity.result.isEmpty()) {
				RecipeHolder<CookstoveRecipe> recipeHolder = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
				if (recipeHolder == null) {
					return;
				}
				blockEntity.container = null;
				blockEntity.display = null;
				blockEntity.currentRecipe = recipeHolder.value();
			}
		} else if(!blockEntity.currentRecipe.matches(blockEntity, level)) {
			if (blockEntity.progressTicks != 0 || blockEntity.totalTicks != 0) {
				blockEntity.progressTicks = 0;
				blockEntity.totalTicks = 0;
				if (!level.isClientSide) {
					blockEntity.setChanged();
				}
			}
			blockEntity.container = blockEntity.currentRecipe.container();
			blockEntity.display = blockEntity.currentRecipe.display();
			blockEntity.currentRecipe = null;
			if (!level.isClientSide) {
				level.setBlock(blockPos, blockState.setValue(CookstoveBlock.LIT, false), Block.UPDATE_ALL);
			}
			return;
		}
		boolean lit = blockState.getValue(CookstoveBlock.LIT);
		if(result.isEmpty()) {
			if(blockEntity.totalTicks != blockEntity.currentRecipe.cookTime()) {
				blockEntity.totalTicks = blockEntity.currentRecipe.cookTime();
				blockEntity.progressTicks = 0;
				if(level.isClientSide) {
					blockEntity.animateTick = 0;
				} else {
					level.playSound(null, blockPos, ECSounds.VILLAGER_WORK_CHEF, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			if(!lit && blockEntity.fuel > 0) {
				level.setBlock(blockPos, blockState.setValue(CookstoveBlock.LIT, true), Block.UPDATE_ALL);
				lit = true;
			}
			if(lit) {
				blockEntity.fuel -= 1;
				blockEntity.progressTicks += 1;
				if (level.isClientSide) {
					blockEntity.animateTick += 1;
					if(blockEntity.animateTick % 10 == 0) {
						blockEntity.spawnItemParticles(blockPos);
					}
				}
				if(blockEntity.fuel <= 0) {
					blockEntity.fuel = 0;
					level.setBlock(blockPos, blockState.setValue(CookstoveBlock.LIT, false), Block.UPDATE_ALL);
				}
				if (blockEntity.progressTicks >= blockEntity.totalTicks) {
					if (level.isClientSide) {
						blockEntity.animateTick = 0;
					} else {
						ItemStack target = blockEntity.currentRecipe.assemble(blockEntity, level.registryAccess());
						blockEntity.setResult(target);
						for (int i = SLOT_INPUT_START; i < COUNT_SLOTS; ++i) {
							ItemStack input = blockEntity.getItem(i);
							if (!input.isEmpty()) {
								input.shrink(1);
							}
						}
						blockEntity.tank.drain(blockEntity.currentRecipe.fluidStack(), IFluidHandler.FluidAction.EXECUTE);
						blockEntity.setChanged();
					}
					blockEntity.progressTicks = 0;
					blockEntity.totalTicks = 0;
					if (!level.isClientSide) {
						level.setBlock(blockPos, blockState.setValue(CookstoveBlock.LIT, false), Block.UPDATE_ALL);
					}
				}
			}
		}
	}

	private static final int PARTICLE_MIN_AMOUNT = 1;
	private static final int PARTICLE_MAX_AMOUNT = 3;
	private void spawnItemParticles(BlockPos blockPos) {
		if(this.level != null) {
			int bound = this.level.random.nextInt(PARTICLE_MAX_AMOUNT - PARTICLE_MIN_AMOUNT) + PARTICLE_MIN_AMOUNT;
			for (int i = 0; i < bound; ++i) {
				Vec3 speed = new Vec3(
						((double) this.level.random.nextFloat() - 0.5D) * 0.0625D,
						Math.random() * 0.0375D + 0.0625D,
						((double) this.level.random.nextFloat() - 0.5D) * 0.0625D
				);
				double y = (double)this.level.random.nextFloat() * 0.2D + 0.6D;
				Vec3 position = new Vec3(
						((double) this.level.random.nextFloat() - 0.5D) * 0.4D + blockPos.getX() + 0.5D,
						y + blockPos.getY(),
						((double) this.level.random.nextFloat() - 0.5D) * 0.4D + blockPos.getZ() + 0.5D
				);
				this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, position.x, position.y, position.z, speed.x, speed.y + 0.05D, speed.z);
			}
		}
	}

	public static final int ADD_FUEL_INDEX = -2;
	public static final int DONT_ADD_INGREDIENT_INDEX = -1;

	@SuppressWarnings("UnstableApiUsage")
	public boolean interact(Player player, ItemStack item, int index) {
		//add fuel
		if(index == ADD_FUEL_INDEX) {
			int itemBurnTime = ForgeHooks.getBurnTime(item, ECRecipes.COOKSTOVE_TYPE.get());
			if (itemBurnTime > 0 && this.fuel < MAX_FUEL) {
				this.fuel += itemBurnTime;
				this.setChanged();
				return true;
			}
		}
		if(this.container == null) {
			//add/take ingredient
			if(index >= 0) {
				if(item.isEmpty()) {
					if(!this.getItem(index).isEmpty()) {
						player.addItem(this.getItem(index));
						this.setItem(index, ItemStack.EMPTY);
						this.setChanged();
						return true;
					}
				} else {
					ItemStack shadowItem = item.copy();
					shadowItem.setCount(1);
					if(this.getItem(index).isEmpty()) {
						this.shadowedContainer.setItem(index, shadowItem);
					} else {
						index = placeItemInOrder(this.shadowedContainer, shadowItem);
					}
					if(index >= 0) {
						boolean flag = this.partialQuickCheck.getRecipeFor(this.shadowedContainer, player.level()).isPresent();
						this.shadowedContainer.setItem(index, ItemStack.EMPTY);

						if(flag) {
							this.setItem(index, item.split(1));
							this.setChanged();
							return true;
						}
					}
				}
			}
			//add fluid
			//TODO
			return false;
		}
		//take result
		if(this.container.isEmpty() || this.container.test(item)) {
			player.addItem(this.getResult().split(1));
			if(this.getResult().isEmpty()) {
				this.container = null;
				this.display = null;
			}
			this.setChanged();
			return true;
		}
		return false;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items.clear();
		ContainerHelper.loadAllItems(nbt, this.items);
		this.copyItemsToShadowedContainer();
		if(nbt.contains("container", Tag.TAG_COMPOUND)) {
			this.container = Ingredient.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("container")).getOrThrow(false, ECLogger::error);
		} else {
			this.container = null;
		}
		if(nbt.contains("display", Tag.TAG_COMPOUND)) {
			this.display = CookstoveDisplay.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("display")).getOrThrow(false, ECLogger::error);
		} else {
			this.display = null;
		}
		if(nbt.contains("result", Tag.TAG_COMPOUND)) {
			this.result = ItemStack.of(nbt.getCompound("result"));
		} else {
			this.result = ItemStack.EMPTY;
		}
		this.fuel = nbt.getInt("fuel");
		this.tank.readFromNBT(nbt);
		this.progressTicks = nbt.getInt("progressTicks");
		this.totalTicks = nbt.getInt("totalTicks");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, this.items, true);
		if(this.container != null) {
			Ingredient.CODEC.encodeStart(NbtOps.INSTANCE, this.container)
					.resultOrPartial(ECLogger::error)
					.ifPresent(tag -> nbt.put("container", tag));
		}
		if(this.display != null) {
			CookstoveDisplay.CODEC.encodeStart(NbtOps.INSTANCE, this.display)
					.resultOrPartial(ECLogger::error)
					.ifPresent(tag -> nbt.put("display", tag));
		}
		CompoundTag resultTag = new CompoundTag();
		nbt.put("result", this.result.save(resultTag));
		nbt.putInt("fuel", this.fuel);
		this.tank.writeToNBT(nbt);
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
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void clearContent() {
		this.items.clear();
		this.shadowedContainer.clearContent();
	}

	@Override
	public void fillStackedContents(StackedContents contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Contract(pure = true)
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
	public FluidStack getFluidStack(int tank) {
		if(tank >= COUNT_TANKS) {
			throw new IndexOutOfBoundsException(tank);
		}
		return this.tank.getFluid();
	}
	@Override
	public int getTankSize() {
		return COUNT_TANKS;
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		if (index >= 0 && index < this.items.size()) {
			if (itemStack.getCount() > this.getMaxStackSize()) {
				itemStack.setCount(this.getMaxStackSize());
			}
			this.items.set(index, itemStack);
			this.shadowedContainer.setItem(index, itemStack.copy());
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

	public ItemStack getResult() {
		return this.result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}

	private void copyItemsToShadowedContainer() {
		for(int i = 0; i < this.items.size(); ++i) {
			this.shadowedContainer.setItem(i, this.items.get(i));
		}
	}

	/**
	 * @param container	the container to put item into
	 * @param itemStack	the item to be put into the container
	 * @return			>= 0 if success.
	 * 					< 0 if failed, and contents in container will not change.
	 */
	public static int placeItemInOrder(Container container, ItemStack itemStack) {
		for(int index: DEFAULT_PLACE_ORDER) {
			if(container.getItem(index).isEmpty()) {
				container.setItem(index, itemStack);
				return index;
			}
		}
		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	@Nullable
	public CookstoveDisplay getDisplay() {
		return this.display;
	}

	//Forge Compat
	IItemHandlerModifiable itemHandler = new InvWrapper(this);
	LazyOptional<? extends IItemHandler> itemHandlerWrapper = LazyOptional.of(() -> this.itemHandler);

	private final LazyOptional<IFluidHandler> fluidHandlerWrapper = LazyOptional.of(() -> this.tank);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove) {
			if (capability == ForgeCapabilities.FLUID_HANDLER) {
				return this.fluidHandlerWrapper.cast();
			}
			if(capability == ForgeCapabilities.ITEM_HANDLER) {
				return this.itemHandlerWrapper.cast();
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.itemHandlerWrapper.invalidate();
		this.fluidHandlerWrapper.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.itemHandler = new InvWrapper(this);
		this.itemHandlerWrapper = LazyOptional.of(() -> this.itemHandler);
	}

	public record CookstoveDisplay(Background background, Ingredient ingredient) {
		public static final Codec<CookstoveDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Background.CODEC.fieldOf("background").forGetter(CookstoveDisplay::background),
				Ingredient.CODEC.fieldOf("ingredient").forGetter(CookstoveDisplay::ingredient)
		).apply(instance, CookstoveDisplay::new));

		public static CookstoveDisplay fromNetwork(FriendlyByteBuf buf) {
			Background background = Background.fromNetwork(buf);
			Ingredient ingredient = Ingredient.fromNetwork(buf);
			return new CookstoveDisplay(background, ingredient);
		}

		public void toNetwork(FriendlyByteBuf buf) {
			this.background.toNetwork(buf);
			this.ingredient.toNetwork(buf);
		}

		public record Background(int color, ResourceLocation shape) {
			public static final Codec<Background> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.INT.fieldOf("color").forGetter(Background::color),
					ResourceLocation.CODEC.fieldOf("shape").forGetter(Background::shape)
			).apply(instance, Background::new));

			public static Background fromNetwork(FriendlyByteBuf buf) {
				int color = buf.readVarInt();
				ResourceLocation shape = buf.readResourceLocation();
				return new Background(color, shape);
			}

			public void toNetwork(FriendlyByteBuf buf) {
				buf.writeVarInt(this.color);
				buf.writeResourceLocation(this.shape);
			}
		}
	}
}
