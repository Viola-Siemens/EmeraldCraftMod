package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.common.blocks.workstation.ContinuousMinerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECLogger;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ContinuousMinerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible {
	protected static final int SLOT_INPUT = 0;
	protected static final int SLOT_RESULT = 1;
	public static final int DATA_FLUID = 0;
	public static final int DATA_MINE_TIME = 1;
	public static final int TOTAL_MINE_TIME = 120;
	public static final int FLUID_LEVEL_BUCKET = 100;
	public static final int MAX_FLUID_LEVEL = 250;
	private static final int[] SLOTS_FOR_UP = new int[]{0};
	private static final int[] SLOTS_FOR_SIDES = new int[]{0};
	private static final int[] SLOTS_FOR_DOWN = new int[]{1};
	protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

	int fluid;
	int mineTime;

	protected final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			return switch (index) {
				case DATA_FLUID -> ContinuousMinerBlockEntity.this.fluid;
				case DATA_MINE_TIME -> ContinuousMinerBlockEntity.this.mineTime;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_FLUID -> ContinuousMinerBlockEntity.this.fluid = value;
				case DATA_MINE_TIME -> ContinuousMinerBlockEntity.this.mineTime = value;
			}

		}

		public int getCount() {
			return ContinuousMinerMenu.DATA_COUNT;
		}
	};

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.continuous_miner");
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
		return fluid;
	}

	public void setFluidLevel(int newFluidLevel) {
		fluid = newFluidLevel;
	}

	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.items);
		this.fluid = nbt.getInt("Fluid");
		this.mineTime = nbt.getInt("MineTime");
	}

	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putInt("Fluid", this.fluid);
		nbt.putInt("MineTime", this.mineTime);
		ContainerHelper.saveAllItems(nbt, this.items);
		return nbt;
	}

	public static ItemStack byState(BlockState blockState, ServerLevel level, BlockPos pos, Random random) {
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
		} else if(blockState.is(BlockTags.CRIMSON_STEMS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/crimson_stems");
		} else if(blockState.is(BlockTags.WARPED_STEMS)) {
			rl = new ResourceLocation(MODID, "continuous_miner/wood/warped_stems");
		} else if(blockState.is(Blocks.CRIMSON_NYLIUM)) {
			rl = new ResourceLocation(MODID, "continuous_miner/nylium/crimson_nylium");
		} else if(blockState.is(Blocks.WARPED_NYLIUM)) {
			rl = new ResourceLocation(MODID, "continuous_miner/nylium/warped_nylium");
		} else if(blockState.is(BlockTags.BASE_STONE_NETHER) || blockState.is(Blocks.SOUL_SAND) || blockState.is(Blocks.SOUL_SOIL)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/nether");
		} else if(blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(Blocks.COBBLESTONE) || blockState.is(Blocks.COBBLED_DEEPSLATE)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/overworld");
		} else if(blockState.is(Blocks.GRAVEL) || blockState.is(Blocks.MAGMA_BLOCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/flint");
		} else if(blockState.is(Blocks.OBSIDIAN) || blockState.is(Blocks.CRYING_OBSIDIAN) || blockState.is(Blocks.BEDROCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/obsidian");
		} else {
			return new ItemStack(Items.AIR);
		}
		List<ItemStack> list = level.getServer().getLootTables().get(rl).getRandomItems(
				new LootContext.Builder(level).withRandom(random).create(LootContextParamSets.EMPTY)
		);
		return list.isEmpty() ? new ItemStack(Items.AIR) : list.get(0);
	}

	protected void dispenseFrom(BlockState blockState, ServerLevel level, BlockPos pos, Random random) {
		final double velo = 0.1D;

		Direction facing = blockState.getValue(ContinuousMinerBlock.FACING);
		BlockState front = level.getBlockState(pos.relative(facing));
		ItemStack itemstack = byState(front, level, pos, random);
		if(!itemstack.is(Items.AIR)) {
			this.fluid -= 1;
			this.mineTime = TOTAL_MINE_TIME;
			level.playSound(null, pos, ECSounds.VILLAGER_WORK_GEOLOGIST, SoundSource.BLOCKS, 1.0F, 1.0F);
			if(itemstack.is(Items.STRUCTURE_VOID)) {
				return;
			}

			Direction opposite = facing.getOpposite();
			BlockPos resultPos = pos.relative(opposite);
			Container container = HopperBlockEntity.getContainerAt(level, resultPos);
			if(container == null) {
				ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
				itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double)opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double)opposite.getStepZ() * velo);
				level.addFreshEntity(itemEntity);
			} else {
				ItemStack addItemstack = HopperBlockEntity.addItem(null, container, itemstack.copy().split(1), facing.getOpposite());
				if(!addItemstack.isEmpty()) {
					ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
					itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double)opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double)opposite.getStepZ() * velo);
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
				blockEntity.dispenseFrom(blockState, (ServerLevel)level, blockPos, level.getRandom());
			}
		}
		ItemStack ingredient = blockEntity.items.get(0);
		ItemStack result = blockEntity.items.get(1);
		if(!ingredient.isEmpty()) {
			if(ingredient.is(ECItems.MELTED_EMERALD_BUCKET.get())) {
				if(blockEntity.fluid <= MAX_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						blockEntity.items.set(1, new ItemStack(Items.BUCKET));
					} else if(result.is(Items.BUCKET)) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					blockEntity.fluid += FLUID_LEVEL_BUCKET;
				}
			} else if(ingredient.is(Items.BUCKET)) {
				if(blockEntity.fluid >= FLUID_LEVEL_BUCKET) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						blockEntity.items.set(1, new ItemStack(ECItems.MELTED_EMERALD_BUCKET.get()));
					} else if(result.is(ECItems.MELTED_EMERALD_BUCKET.get()) && result.getCount() < result.getMaxStackSize()) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					blockEntity.fluid -= FLUID_LEVEL_BUCKET;
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
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
		return itemStack.is(Items.BUCKET) || itemStack.is(ECItems.MELTED_EMERALD_BUCKET.get());
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
		return true;
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
		return new ContinuousMinerMenu(id, inventory, this, this.dataAccess);
	}
}
