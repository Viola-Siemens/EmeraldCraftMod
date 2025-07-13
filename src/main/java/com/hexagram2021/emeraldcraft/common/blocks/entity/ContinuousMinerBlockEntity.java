package com.hexagram2021.emeraldcraft.common.blocks.entity;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.continuous_miner.ContinuousMinerCustomLoot;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.ContinuousMinerBlock;
import com.hexagram2021.emeraldcraft.common.config.ECCommonConfig;
import com.hexagram2021.emeraldcraft.common.crafting.menu.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECFluids;
import com.hexagram2021.emeraldcraft.common.util.ECSounds;
import com.hexagram2021.emeraldcraft.network.ClientboundContinuousMinerFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

import javax.annotation.Nullable;
import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ContinuousMinerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, Tank, ISynchronizableContainer {
    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_RESULT = 1;
    public static final int DATA_MINE_TIME = 0;
    public static final int TOTAL_MINE_TIME = 120;
    public static final int TANK_INPUT = 0;
    public static final int COUNT_TANKS = 1;
    public static final long MAX_FLUID_LEVEL = FluidConstants.BUCKET * 5 / 2;
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{1};
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    final SingleFluidStorage tank = new ContinuousMinerTank(MAX_FLUID_LEVEL, this::markDirty);
    final SingleItemStorage[] itemStorages = new SingleItemStorage[]{
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage()
    };

    private FluidStack fluidStack = FluidStack.EMPTY;

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
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ContinuousMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntity.CONTINUOUS_MINER, pos, state);
    }

    private boolean isMining() {
        return this.mineTime > 0;
    }

    public int getMineTime() {
        return this.mineTime;
    }

    public long getFluidLevel() {
        return this.tank.amount;
    }

    public void setFluidLevel(long newFluidLevel) {
        this.tank.amount = newFluidLevel;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.setFluidLevel(nbt.getLong("Fluid"));
        this.mineTime = nbt.getInt("MineTime");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("Fluid", this.tank.amount);
        nbt.putInt("MineTime", this.mineTime);
        ContainerHelper.saveAllItems(nbt, this.items);
    }

    public static List<ItemStack> byState(BlockState blockState, ServerLevel level, RandomSource random) {
        double p = ECCommonConfig.POSSIBILITY_CONTINUOUS_MINER_DROP.get();
        if (random.nextDouble() > p) {
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
            if (rl == null) {
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
        } while (random.nextDouble() < p);
        return ret;
    }

    public void dispenseFrom(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random, boolean needFluid) {
        final double velo = 0.1D;

        Direction facing = blockState.getValue(ContinuousMinerBlock.FACING);
        BlockState front = level.getBlockState(pos.relative(facing));
        List<ItemStack> itemstacks = byState(front, level, random);
        if (itemstacks.get(0).is(Items.AIR)) {
            return;
        }
        if (needFluid) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.tank.extract(FluidVariant.of(ECFluids.MELTED_EMERALD.getSource()), FluidStack.convertMbToDroplets(10), transaction);
                this.fluidStack.setFluidVariant(this.tank.variant);
                this.fluidStack.setAmount(this.tank.amount);
                this.markDirty();
                transaction.commit();
            }
        }
        this.mineTime = TOTAL_MINE_TIME;
        level.playSound(null, pos, ECSounds.VILLAGER_WORK_GEOLOGIST, SoundSource.BLOCKS, 1.0F, 1.0F);
        for (ItemStack itemstack : itemstacks) {
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
        if (blockState.getValue(ContinuousMinerBlock.TRIGGERED)) {
            if (blockEntity.isMining()) {
                blockEntity.mineTime -= 1;
            }
            if (!blockEntity.isMining() && blockEntity.getFluidLevel() > 0) {
                blockEntity.dispenseFrom(blockState, (ServerLevel) level, blockPos, level.getRandom(), true);
            }
        }
        ItemStack ingredient = blockEntity.items.get(0);
        ItemStack result = blockEntity.items.get(1);
        if (!ingredient.isEmpty()) {
            long maxCapacity = FluidConstants.BUCKET;
            if (ingredient.is(ECFluids.MELTED_EMERALD_BUCKET.get())) {
                if (blockEntity.tank.amount <= MAX_FLUID_LEVEL - maxCapacity) {
                    if (result.isEmpty()) {
                        ingredient.shrink(1);
                        blockEntity.items.set(1, new ItemStack(Items.BUCKET));
                    } else if (result.is(Items.BUCKET)) {
                        ingredient.shrink(1);
                        result.grow(1);
                    } else {
                        return;
                    }
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.insert(FluidVariant.of(ECFluids.MELTED_EMERALD), maxCapacity, transaction);
                        blockEntity.fluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.fluidStack.setAmount(blockEntity.tank.amount);
                        blockEntity.markDirty();
                        transaction.commit();
                    }
                }
            } else if (ingredient.is(Items.BUCKET)) {
                if (blockEntity.tank.amount >= maxCapacity) {
                    if (result.isEmpty()) {
                        ingredient.shrink(1);
                        blockEntity.items.set(1, new ItemStack(ECFluids.MELTED_EMERALD_BUCKET.get()));
                    } else if (result.is(ECFluids.MELTED_EMERALD_BUCKET.get()) && result.getCount() < result.getMaxStackSize()) {
                        ingredient.shrink(1);
                        result.grow(1);
                    } else {
                        return;
                    }
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.extract(FluidVariant.of(ECFluids.MELTED_EMERALD.getSource()), maxCapacity, transaction);
                        blockEntity.fluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.fluidStack.setAmount(blockEntity.tank.amount);
                        blockEntity.markDirty();
                        transaction.commit();
                    }
                }
            }
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
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void fillStackedContents(StackedContents contents) {
        for (ItemStack itemstack : this.items) {
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
        if (tank >= COUNT_TANKS) {
            throw new IndexOutOfBoundsException(tank);
        }
        this.fluidStack.setFluidVariant(this.tank.variant);
        this.fluidStack.setAmount(this.tank.amount);
        return this.fluidStack.copy();
    }

    @Override
    public void setFluidStack(int tank, FluidStack fluidStack) {
        if (tank >= COUNT_TANKS) {
            throw new IndexOutOfBoundsException(tank);
        }
        this.tank.variant = fluidStack.getFluidVariant();
        this.tank.amount = fluidStack.getAmount();
        this.fluidStack = fluidStack;
    }

    @Override
    public int getTankSize() {
        return COUNT_TANKS;
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage(int tank) {
        return this.tank;
    }

    @Override
    public SingleVariantStorage<ItemVariant> getItemStorage(@Nullable Direction direction) {
        if (direction == null) return this.itemStorages[2];
        return switch (direction) {
            case UP -> this.itemStorages[0];
            case DOWN -> this.itemStorages[1];
            default -> this.itemStorages[2];
        };
    }

    @Override
    public int getTanks() {
        return getTankSize();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluidStack(tank);
    }

    private boolean dirty = false;

    @Override
    public void markDirty() {
        this.dirty = true;
        for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) this.level, this.worldPosition)) {
            NetworkHandler.sendMessageToPlayer(this.getSyncPacket(), player);
        }
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
        return new ClientboundContinuousMinerFluidSyncPacket("continuous_miner", List.of(this.getFluidStack(0)));
    }

    public static class ContinuousMinerTank extends SingleFluidStorage {
        final long capacity;
        final Runnable onChange;

        ContinuousMinerTank(long capacity, Runnable onChange) {
            this.capacity = capacity;
            this.onChange = onChange;
        }

        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
            onChange.run();
        }

        @Override
        protected long getCapacity(FluidVariant fluidVariant) {
            return this.capacity;
        }
    }
}
