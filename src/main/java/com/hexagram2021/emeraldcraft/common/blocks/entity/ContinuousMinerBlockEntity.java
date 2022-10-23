package com.hexagram2021.emeraldcraft.common.blocks.entity;

import com.hexagram2021.emeraldcraft.api.continuous_miner.ContinuousMinerCustomLoot;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.ContinuousMinerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class ContinuousMinerBlockEntity extends LockableTileEntity implements ISidedInventory, IRecipeHelperPopulator, ITickableTileEntity {
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

	protected final IIntArray dataAccess = new IIntArray() {
		public int get(int index) {
			switch (index) {
				case DATA_FLUID:
					return ContinuousMinerBlockEntity.this.fluid;
				case DATA_MINE_TIME:
					return ContinuousMinerBlockEntity.this.mineTime;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
				case DATA_FLUID:
					ContinuousMinerBlockEntity.this.fluid = value;
					break;
				case DATA_MINE_TIME:
					ContinuousMinerBlockEntity.this.mineTime = value;
					break;
			}

		}

		public int getCount() {
			return ContinuousMinerMenu.DATA_COUNT;
		}
	};

	@Override @Nonnull
	protected TranslationTextComponent getDefaultName() {
		return new TranslationTextComponent("container.continuous_miner");
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

	public ContinuousMinerBlockEntity() {
		super(ECBlockEntity.CONTINUOUS_MINER.get());
	}

	private boolean isMining() {
		return this.mineTime > 0;
	}

	public int getFluidLevel() {
		return this.fluid;
	}

	public void setFluidLevel(int newFluidLevel) {
		this.fluid = newFluidLevel;
	}

	@Override
	public void load(@Nonnull BlockState blockState, @Nonnull CompoundNBT nbt) {
		super.load(blockState, nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.items);
		this.fluid = nbt.getInt("Fluid");
		this.mineTime = nbt.getInt("MineTime");
	}

	@Override @Nonnull
	public CompoundNBT save(@Nonnull CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("Fluid", this.fluid);
		nbt.putInt("MineTime", this.mineTime);
		ItemStackHelper.saveAllItems(nbt, this.items);
		return nbt;
	}

	public static ItemStack byState(BlockState blockState, ServerWorld level, Random random) {
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
		} else if(blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(Blocks.COBBLESTONE) || blockState.is(ECBlocks.Decoration.COBBLED_DEEPSLATE.get())) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/overworld");
		} else if(blockState.is(Blocks.GRAVEL) || blockState.is(Blocks.MAGMA_BLOCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/flint");
		} else if(blockState.is(Blocks.OBSIDIAN) || blockState.is(Blocks.CRYING_OBSIDIAN) || blockState.is(Blocks.BEDROCK)) {
			rl = new ResourceLocation(MODID, "continuous_miner/ores/obsidian");
		} else if (blockState.is(Blocks.WATER) ||
				(blockState.is(Blocks.CAULDRON) && blockState.getValue(CauldronBlock.LEVEL) > 0) ||
				(blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED))) {
			rl = new ResourceLocation(MODID, "continuous_miner/fishing");
		} else {
			rl = ContinuousMinerCustomLoot.getBlockLoot(blockState);
			if(rl == null) {
				return new ItemStack(Items.AIR);
			}
		}
		List<ItemStack> list = level.getServer().getLootTables().get(rl).getRandomItems(
				new LootContext.Builder(level).withRandom(random).create(LootParameterSets.EMPTY)
		);
		return list.isEmpty() ? new ItemStack(Items.AIR) : list.get(0);
	}

	protected void dispenseFrom(BlockState blockState, ServerWorld level, BlockPos pos, Random random) {
		final double velo = 0.1D;

		Direction facing = blockState.getValue(ContinuousMinerBlock.FACING);
		BlockState front = level.getBlockState(pos.relative(facing));
		ItemStack itemstack = byState(front, level, random);
		Item item = itemstack.getItem();
		if(item != Items.AIR) {
			this.fluid -= 1;
			this.mineTime = TOTAL_MINE_TIME;
			level.playSound(null, pos, ECSounds.VILLAGER_WORK_GEOLOGIST, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if(item == Items.STRUCTURE_VOID) {
				return;
			}

			Direction opposite = facing.getOpposite();
			BlockPos resultPos = pos.relative(opposite);
			IInventory container = HopperTileEntity.getContainerAt(level, resultPos);
			if(container == null) {
				ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
				itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double)opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double)opposite.getStepZ() * velo);
				level.addFreshEntity(itemEntity);
			} else {
				ItemStack addItemstack = HopperTileEntity.addItem(null, container, itemstack.copy().split(1), facing.getOpposite());
				if(!addItemstack.isEmpty()) {
					ItemEntity itemEntity = new ItemEntity(level, resultPos.getX() + 0.5D, resultPos.getY() + 1.2D, resultPos.getZ() + 0.5D, itemstack);
					itemEntity.setDeltaMovement(random.nextGaussian() * 0.001D + (double)opposite.getStepX() * velo, random.nextGaussian() * 0.001D + 0.2D, random.nextGaussian() * 0.001D + (double)opposite.getStepZ() * velo);
					level.addFreshEntity(itemEntity);
				}
			}
		}
	}

	@Override
	public void tick() {
		BlockState blockState = this.level.getBlockState(this.worldPosition);
		if(blockState.getValue(ContinuousMinerBlock.TRIGGERED)) {
			if(this.isMining()) {
				this.mineTime -= 1;
			}
			if(!this.isMining() && this.getFluidLevel() > 0) {
				this.dispenseFrom(blockState, (ServerWorld)this.level, this.worldPosition, this.level.getRandom());
			}
		}
		ItemStack ingredient = this.items.get(0);
		ItemStack result = this.items.get(1);
		if(!ingredient.isEmpty()) {
			if(ingredient.getItem() == ECItems.MELTED_EMERALD_BUCKET.get()) {
				if(this.fluid <= MAX_FLUID_LEVEL - FLUID_LEVEL_BUCKET) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						this.items.set(1, new ItemStack(Items.BUCKET));
					} else if(result.getItem() == Items.BUCKET) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					this.fluid += FLUID_LEVEL_BUCKET;
				}
			} else if(ingredient.getItem() == Items.BUCKET) {
				if(this.fluid >= FLUID_LEVEL_BUCKET) {
					if(result.isEmpty()) {
						ingredient.shrink(1);
						this.items.set(1, new ItemStack(ECItems.MELTED_EMERALD_BUCKET.get()));
					} else if(result.getItem() == ECItems.MELTED_EMERALD_BUCKET.get() && result.getCount() < result.getMaxStackSize()) {
						ingredient.shrink(1);
						result.grow(1);
					} else {
						return;
					}
					this.fluid -= FLUID_LEVEL_BUCKET;
				}
			}
		}
	}

	@Override
	public int[] getSlotsForFace(@Nonnull Direction direction) {
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
	public void fillStackedContents(@Nonnull RecipeItemHelper contents) {
		for(ItemStack itemstack : this.items) {
			contents.accountStack(itemstack);
		}
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		}
		return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
	public void setItem(int index, @Nonnull ItemStack itemStack) {
		this.items.set(index, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack itemStack) {
		Item item = itemStack.getItem();
		return item == Items.BUCKET || item == ECItems.MELTED_EMERALD_BUCKET.get();
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack itemStack, Direction direction) {
		return this.canPlaceItem(index, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack itemStack, @Nonnull Direction direction) {
		return true;
	}

	LazyOptional<? extends IItemHandler>[] handlers =
			SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override @Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
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
		for (LazyOptional<? extends IItemHandler> handler : handlers) {
			handler.invalidate();
		}
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	}

	@Override @Nonnull
	protected Container createMenu(int id, @Nonnull PlayerInventory inventory) {
		return new ContinuousMinerMenu(id, inventory, this, this.dataAccess);
	}
}
