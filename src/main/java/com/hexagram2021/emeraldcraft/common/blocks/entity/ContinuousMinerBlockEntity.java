package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.continuous_miner.ContinuousMinerCustomLoot;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.ContinuousMinerBlock;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.menu.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECFluids;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

@SuppressWarnings("unused")
public class ContinuousMinerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, Tank, ISynchronizableContainer {
	protected static final int SLOT_INPUT = 0;
	protected static final int SLOT_RESULT = 1;
	public static final int DATA_MINE_TIME = 0;
	public static final int TOTAL_MINE_TIME = 120;
	public static final int TANK_INPUT = 0;
	public static final int COUNT_TANKS = 1;
	public static final int MAX_FLUID_LEVEL = FluidType.BUCKET_VOLUME * 5 / 2;
	private static final int[] SLOTS_FOR_UP = new int[]{0};
	private static final int[] SLOTS_FOR_SIDES = new int[]{0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{1};
	protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

	final ContinuousMinerTank tank = new ContinuousMinerTank(MAX_FLUID_LEVEL);
	int mineTime;

	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return index == DATA_MINE_TIME ? ContinuousMinerBlockEntity.this.mineTime : 0;
		}

		public void set(int index, int value) {
			if (index == DATA_MINE_TIME) {
				ContinuousMinerBlockEntity.this.mineTime = value;
			}
		}

		public int getCount() {
			return ContinuousMinerMenu.DATA_COUNT;
		}
	};

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.continuous_miner");
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

	public ContinuousMinerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntity.CONTINUOUS_MINER.get(), pos, state);
	}

	private boolean isMining() {
		return this.mineTime > 0;
	}

	public int getFluidLevel() {
		return this.tank.amount;
	}

	public void setFluidLevel(int newFluidLevel) {
		this.tank.setFluidAmount(newFluidLevel);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.setFluidLevel(nbt.getInt("Fluid"));
		this.mineTime = nbt.getInt("MineTime");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("Fluid", this.tank.amount);
		nbt.putInt("MineTime", this.mineTime);
		ContainerHelper.saveAllItems(nbt, this.items);
	}

	public static List<ItemStack> byState(BlockState blockState, ServerLevel level, RandomSource random) {
		double p = ECCommonConfig.POSSIBILITY_CONTINUOUS_MINER_DROP.get();
		if(random.nextDouble() > p) {
			return List.of(new ItemStack(Items.STRUCTURE_VOID));
		}
		ResourceLocation rl;
		if (blockState.is(BlockTags.OAK_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/oak_logs");
		} else if (blockState.is(BlockTags.SPRUCE_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/spruce_logs");
		} else if (blockState.is(BlockTags.BIRCH_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/birch_logs");
		} else if (blockState.is(BlockTags.JUNGLE_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/jungle_logs");
		} else if (blockState.is(BlockTags.ACACIA_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/acacia_logs");
		} else if (blockState.is(BlockTags.DARK_OAK_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/dark_oak_logs");
		} else if (blockState.is(BlockTags.MANGROVE_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/mangrove_logs");
		} else if (blockState.is(BlockTags.BAMBOO_BLOCKS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/bamboo_blocks");
		} else if (blockState.is(BlockTags.CHERRY_LOGS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/cherry_logs");
		} else if (blockState.is(BlockTags.CRIMSON_STEMS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/crimson_stems");
		} else if (blockState.is(BlockTags.WARPED_STEMS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/warped_stems");
		} else if (blockState.is(Blocks.CRIMSON_NYLIUM)) {
			rl = new ResourceLocation(MODID, "continuous_miner/nylium/crimson_nylium");
		} else if (blockState.is(Blocks.WARPED_NYLIUM)) {
			rl = new ResourceLocation(MODID, "continuous_miner/nylium/warped_nylium");
		} else if (blockState.is(BlockTags.BASE_STONE_NETHER) || blockState.is(Blocks.SOUL_SAND) || blockState.is(Blocks.SOUL_SOIL)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/nether");
		} else if (blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(Blocks.COBBLESTONE) || blockState.is(Blocks.COBBLED_DEEPSLATE)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/overworld");
		} else if (blockState.is(Blocks.GRAVEL) || blockState.is(Blocks.MAGMA_BLOCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/flint");
		} else if (blockState.is(Blocks.OBSIDIAN) || blockState.is(Blocks.CRYING_OBSIDIAN) || blockState.is(Blocks.BEDROCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/obsidian");
		} else if (blockState.is(BlockTags.DIRT)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/dirt");
		} else if (blockState.is(Blocks.WATER) || blockState.is(Blocks.WATER_CAULDRON) || (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED))) {
			//TODO: Different biomes product different fishes.
			rl = new ResourceLocation(MODID, "continuous_miner/fishing");
		} else {
			rl = ContinuousMinerCustomLoot.getBlockLoot(blockState);
			if(rl == null) {
				return List.of(new ItemStack(Items.AIR));
			}
		}
		List<ItemStack> ret = Lists.newArrayList();
		do {
			LootTable lootTable = level.getServer().getLootData().getLootTable(rl);
			List<ItemStack> list = lootTable.getRandomItems(
					new LootParams.Builder(level).create(LootContextParamSets.EMPTY)
			);
			ret.add(list.isEmpty() ? new ItemStack(Items.STRUCTURE_VOID) : list.get(0));
			p -= 1.0D;
		} while(random.nextDouble() < p);
		return ret;
	}

	public void dispenseFrom(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random, boolean needFluid) {
		final double velo = 0.1D;

		Direction facing = blockState.getValue(ContinuousMinerBlock.FACING);
		BlockState front = level.getBlockState(pos.relative(facing));
		List<ItemStack> itemstacks = byState(front, level, random);
		if(itemstacks.get(0).is(Items.AIR)) {
			return;
		}
		if (needFluid) {
			this.tank.drain(10, IFluidHandler.FluidAction.EXECUTE);
			this.markDirty();
		}
		this.mineTime = TOTAL_MINE_TIME;
		level.playSound(null, pos, ECSounds.VILLAGER_WORK_GEOLOGIST, SoundSource.BLOCKS, 1.0F, 1.0F);
		for(ItemStack itemstack: itemstacks) {
			if (itemstack.is(Items.STRUCTURE_VOID) || itemstack.is(Items.AIR)) {
				continue;
			}

			Direction opposite = facing.getOpposite();
			BlockPos resultPos = pos.relative(opposite);
			Container container = HopperBlockEntity.getContainerAt(level, resultPos);
			if (container == null) {
				ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
				itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double) opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double) opposite.getStepZ() * velo);
				level.addFreshEntity(itemEntity);
			} else {
				ItemStack addItemstack = HopperBlockEntity.addItem(null, container, itemstack.copy().split(1), facing.getOpposite());
				if (!addItemstack.isEmpty()) {
					ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
					itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double) opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double) opposite.getStepZ() * velo);
					level.addFreshEntity(itemEntity);
				}
			}
		}
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ContinuousMinerBlockEntity blockEntity) {
		if(blockState.getValue(ContinuousMinerBlock.TRIGGERED)) {
			if(blockEntity.isMining()) {
				blockEntity.mineTime -= 1;
			}
			if(!blockEntity.isMining() && blockEntity.getFluidLevel() > 0) {
				blockEntity.dispenseFrom(blockState, (ServerLevel)level, blockPos, level.getRandom(), true);
			}
		}
		ItemStack ingredient = blockEntity.items.get(0);
		ItemStack result = blockEntity.items.get(1);
		if(!ingredient.isEmpty()) {
			if(ingredient.is(ECFluids.MELTED_EMERALD.getBucket())) {
				if(blockEntity.tank.amount <= MAX_FLUID_LEVEL - FluidType.BUCKET_VOLUME) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						blockEntity.items.set(1, new ItemStack(Items.BUCKET));
					} else if(result.is(Items.BUCKET)) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					blockEntity.tank.fill(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
					blockEntity.markDirty();
				}
			} else if(ingredient.is(Items.BUCKET)) {
				if(blockEntity.tank.amount >= FluidType.BUCKET_VOLUME) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						blockEntity.items.set(1, new ItemStack(ECFluids.MELTED_EMERALD.getBucket()));
					} else if(result.is(ECFluids.MELTED_EMERALD.getBucket()) && result.getCount() < result.getMaxStackSize()) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					blockEntity.tank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
					blockEntity.markDirty();
				}
			}
		}
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
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
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
		return itemStack.is(Items.BUCKET) || itemStack.is(ECFluids.MELTED_EMERALD.getBucket());
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
		return true;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new ContinuousMinerMenu(id, inventory, this, this.dataAccess);
	}

	@Override
	public FluidStack getFluidStack(int tank) {
		if(tank >= COUNT_TANKS) {
			throw new IndexOutOfBoundsException(tank);
		}
		return this.tank.getFluid();
	}

	@Override
	public void setFluidStack(int tank, FluidStack fluidStack) {
		if(tank >= COUNT_TANKS) {
			throw new IndexOutOfBoundsException(tank);
		}
		this.tank.setFluid(fluidStack);
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
		return new ClientboundFluidSyncPacket(List.of(this.tank.getFluid()));
	}

	//Forge Compat
	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	private final LazyOptional<IFluidHandler> fluidHandlerWrapper = LazyOptional.of(() -> this.tank);

	@Override
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

	public class ContinuousMinerTank implements IFluidHandler, IFluidTank {
		int amount = 0;
		final int capacity;

		ContinuousMinerTank(int capacity) {
			this.capacity = capacity;
		}

		@Override
		public FluidStack getFluid() {
			return new FluidStack(ECFluids.MELTED_EMERALD.getStill(), this.amount);
		}

		public void setFluid(FluidStack fluidStack) {
			if(this.isFluidValid(fluidStack)) {
				this.amount = Math.min(fluidStack.getAmount(), this.capacity);
				ContinuousMinerBlockEntity.this.markDirty();
			} else {
				ECLogger.warn(new IllegalArgumentException("Trying to set fluid %s to continuous miner.".formatted(getRegistryName(fluidStack.getFluid()))));
			}
		}

		@Override
		public int getFluidAmount() {
			return this.amount;
		}

		public void setFluidAmount(int amount) {
			this.amount = amount;
			ContinuousMinerBlockEntity.this.markDirty();
		}

		@Override
		public int getCapacity() {
			return this.capacity;
		}

		@Override
		public boolean isFluidValid(FluidStack stack) {
			return stack.getFluid().isSame(ECFluids.MELTED_EMERALD.getStill());
		}

		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(int tank) {
			return this.getFluid();
		}

		@Override
		public int getTankCapacity(int tank) {
			return this.getCapacity();
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			return this.isFluidValid(stack);
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			if (resource.isEmpty() || !this.isFluidValid(resource)) {
				return 0;
			}
			if (action.simulate()) {
				return Math.min(this.capacity - this.amount, resource.getAmount());
			}
			int filled = this.capacity - this.amount;
			if (resource.getAmount() < filled) {
				this.amount += resource.getAmount();
				filled = resource.getAmount();
			} else {
				this.amount = this.capacity;
			}
			if (filled > 0) {
				ContinuousMinerBlockEntity.this.markDirty();
			}
			return filled;
		}

		@SuppressWarnings("UnusedReturnValue")
		public int fill(int maxFill, FluidAction action) {
			int filled = this.capacity - this.amount;
			if (maxFill < filled) {
				filled = maxFill;
			}
			if (action.execute() && filled > 0) {
				this.amount += filled;
				ContinuousMinerBlockEntity.this.markDirty();
			}
			return filled;
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (resource.isEmpty() || !this.isFluidValid(resource)) {
				return FluidStack.EMPTY;
			}
			return this.drain(resource.getAmount(), action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			int drained = maxDrain;
			if (this.amount < drained) {
				drained = this.amount;
			}
			FluidStack stack = new FluidStack(ECFluids.MELTED_EMERALD.getStill(), drained);
			if (action.execute() && drained > 0) {
				this.amount -= drained;
				ContinuousMinerBlockEntity.this.markDirty();
			}
			return stack;
		}
	}
}
