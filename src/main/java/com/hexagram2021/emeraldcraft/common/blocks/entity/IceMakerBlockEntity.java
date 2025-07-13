package com.hexagram2021.emeraldcraft.common.blocks.entity;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.IceMakerBlock;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IceMakerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.ClientboundIceMakerFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class IceMakerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, Tank, ISynchronizableContainer {
    public static final long MAX_INGREDIENT_FLUID_LEVEL = FluidConstants.BUCKET * 10;
    public static final long MAX_CONDENSATE_FLUID_LEVEL = FluidConstants.BUCKET * 4;
    public static final int TANK_INPUT = 0;
    public static final int TANK_CONDENSATE = 1;
    public static final int COUNT_TANKS = 2;

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{2, 0};

    protected NonNullList<ItemStack> items = NonNullList.withSize(IceMakerMenu.SLOT_COUNT, ItemStack.EMPTY);
    final SingleFluidStorage tank = SingleFluidStorage.withFixedCapacity(MAX_INGREDIENT_FLUID_LEVEL, this::markDirty);
    final SingleFluidStorage tankCondensate = SingleFluidStorage.withFixedCapacity(MAX_CONDENSATE_FLUID_LEVEL, IceMakerBlockEntity.this::markDirty);
    final SingleItemStorage[] itemStorages = new SingleItemStorage[]{
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage()
    };

    private FluidStack inputFluidStack = FluidStack.EMPTY;
    private FluidStack condensateFluidStack = FluidStack.EMPTY;

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
        super(ECBlockEntity.ICE_MAKER, pos, state);
        this.quickCheck = RecipeManager.createCheck(ECRecipes.ICE_MAKER_TYPE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.ice_maker");
    }

    private boolean hasInput() {
        return this.tank.amount > 0;
    }

    private boolean isLit() {
        return this.tankCondensate.amount > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState blockState, IceMakerBlockEntity blockEntity) {
        long bucketCap = FluidConstants.BUCKET;
        boolean flag = blockEntity.isLit() && blockEntity.hasInput();
        boolean changed = false;

        ItemStack condensateItemStack = blockEntity.items.get(IceMakerMenu.CONDENSATE_SLOT);
        if (blockEntity.tankCondensate.amount <= MAX_CONDENSATE_FLUID_LEVEL - bucketCap && condensateItemStack.getCount() == 1) {
            Storage<FluidVariant> storage = ContainerItemContext.withConstant(condensateItemStack).find(FluidStorage.ITEM);
            if (storage != null) {
                try (Transaction transaction = Transaction.openOuter()) {
                    for (StorageView<FluidVariant> view : storage) {
                        FluidVariant resource = view.getResource();
                        if (!resource.isBlank() && (blockEntity.tankCondensate.isResourceBlank() || blockEntity.tankCondensate.amount <= 0 || blockEntity.tankCondensate.variant.equals(resource))) {
                            long amount = view.getAmount();
                            blockEntity.tankCondensate.insert(resource, amount, transaction);
                            blockEntity.condensateFluidStack.setFluidVariant(resource);
                            blockEntity.condensateFluidStack.setAmount(blockEntity.tankCondensate.amount);
                            blockEntity.items.set(IceMakerMenu.CONDENSATE_SLOT, new ItemStack(Items.BUCKET));
                            if (view instanceof SingleSlotStorage<FluidVariant>)
                                break;
                        }
                    }
                    transaction.commit();
                }
            }
        }

        boolean inputExists = blockEntity.hasInput();
        if (blockEntity.isLit() && inputExists) {
            IceMakerRecipe recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);

            if (blockEntity.canFreeze(level.registryAccess(), recipe, blockEntity.items, blockEntity.getMaxStackSize())) {
                ++blockEntity.freezingProgress;
                try (Transaction transaction = Transaction.openOuter()) {
                    blockEntity.tankCondensate.extract(blockEntity.tankCondensate.variant, FluidStack.convertMbToDroplets(5), transaction);
                    blockEntity.condensateFluidStack.setFluidVariant(blockEntity.tankCondensate.variant);
                    blockEntity.condensateFluidStack.setAmount(blockEntity.tankCondensate.amount);
                    transaction.commit();
                }
                if (blockEntity.freezingProgress >= blockEntity.freezingTotalTime) {
                    blockEntity.freezingProgress = 0;
                    blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
                    blockEntity.freeze(level.registryAccess(), recipe, blockEntity.items, blockEntity.getMaxStackSize());

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
        if (!ingredientInput.isEmpty()) {
            Item inputBucketItem = blockEntity.tank.variant.getFluid().getBucket();
            if (ingredientInput.is(inputBucketItem)) {
                if (blockEntity.tank.amount <= MAX_INGREDIENT_FLUID_LEVEL - bucketCap) {
                    if (ingredientOutput.isEmpty()) {
                        blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
                    } else if (ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
                        ingredientOutput.grow(1);
                    } else {
                        return;
                    }
                    ingredientInput.shrink(1);
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.insert(blockEntity.tank.variant, bucketCap, transaction);
                        blockEntity.inputFluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.inputFluidStack.setAmount(blockEntity.tank.amount);
                        changed = true;
                        transaction.commit();
                    }
                }
            } else if (ingredientInput.is(Items.BUCKET)) {
                if (blockEntity.tank.amount >= bucketCap) {
                    if (ingredientOutput.isEmpty()) {
                        blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(inputBucketItem));
                    } else if (ingredientOutput.is(inputBucketItem) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
                        ingredientOutput.grow(1);
                    } else {
                        return;
                    }
                    //原料为空的时候重置进度
/*                    if (ingredientInput.getCount() == 1)
                        blockEntity.freezingProgress = 0;*/
                    ingredientInput.shrink(1);
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.extract(blockEntity.tank.variant, bucketCap, transaction);
                        blockEntity.inputFluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.inputFluidStack.setAmount(blockEntity.tank.amount);
                        changed = true;
                        transaction.commit();
                    }
                }
            } else if (blockEntity.tank.amount <= 0) {
                Storage<FluidVariant> c = ContainerItemContext.withConstant(ingredientInput).find(FluidStorage.ITEM);
                if (c != null) {
                    if (ingredientOutput.isEmpty()) {
                        blockEntity.items.set(IceMakerMenu.INGREDIENT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
                    } else if (ingredientOutput.is(Items.BUCKET) && ingredientOutput.getCount() < ingredientOutput.getMaxStackSize()) {
                        ingredientOutput.grow(1);
                    } else {
                        return;
                    }
                    try (Transaction transaction = Transaction.openOuter()) {
                        for (StorageView<FluidVariant> view : c) {
                            FluidVariant resource = view.getResource();
                            if (resource == null || resource.isBlank()) continue;
                            long amount = view.getAmount();
                            blockEntity.tank.insert(resource, amount, transaction);
                            blockEntity.inputFluidStack.setFluidVariant(resource);
                            blockEntity.inputFluidStack.setAmount(blockEntity.tank.amount);
                            if (view instanceof SingleSlotStorage<FluidVariant>)
                                break;
                        }
                        transaction.commit();
                    }
                    ingredientInput.shrink(1);
                    blockEntity.freezingTotalTime = getTotalFreezeTime(level, blockEntity);
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
    private boolean canFreeze(RegistryAccess registryAccess, @Nullable IceMakerRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
        if (recipe == null) {
            return false;
        }
        ItemStack result = recipe.assemble(this, registryAccess);
        if (result.isEmpty()) {
            return false;
        }
        ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
        if (itemstack.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItem(itemstack, result)) {
            return false;
        }
        if (itemstack.getCount() + result.getCount() <= maxCount && itemstack.getCount() + result.getCount() <= itemstack.getMaxStackSize()) {
            return true;
        }
        return itemstack.getCount() + result.getCount() <= result.getMaxStackSize();
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean freeze(RegistryAccess registryAccess, @Nullable IceMakerRecipe recipe, NonNullList<ItemStack> container, int maxCount) {
        if (this.canFreeze(registryAccess, recipe, container, maxCount)) {
            ItemStack result = recipe.assemble(this, registryAccess);
            ItemStack itemstack = container.get(IceMakerMenu.RESULT_SLOT);
            if (itemstack.isEmpty()) {
                container.set(IceMakerMenu.RESULT_SLOT, result.copy());
            } else if (ItemStack.isSameItem(itemstack, result)) {
                itemstack.grow(result.getCount());
            }

            try (Transaction transaction = Transaction.openOuter()) {
                FluidStack inputFluid = recipe.inputFluid();
                this.tank.extract(inputFluid.getFluidVariant(), inputFluid.getAmount(), transaction);
                this.inputFluidStack.setFluidVariant(this.tank.variant);
                this.inputFluidStack.setAmount(this.tank.amount);
                transaction.commit();
                return true;
            }
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
        if (nbt.contains(INPUT_FLUID_TAG, Tag.TAG_COMPOUND)) {
            this.tank.readNbt(nbt.getCompound(INPUT_FLUID_TAG));
        }
        this.freezingProgress = nbt.getInt("FreezingProgress");
        this.freezingTotalTime = nbt.getInt("FreezingTimeTotal");
        if (nbt.contains(CONDENSATE_FLUID_TAG, Tag.TAG_COMPOUND)) {
            this.tankCondensate.readNbt(nbt.getCompound(CONDENSATE_FLUID_TAG));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag inputFluidTag = new CompoundTag();
        this.tank.writeNbt(inputFluidTag);
        nbt.put(INPUT_FLUID_TAG, inputFluidTag);
        nbt.putInt("FreezingProgress", this.freezingProgress);
        nbt.putInt("FreezingTimeTotal", this.freezingTotalTime);
        CompoundTag condensateFluidTag = new CompoundTag();
        this.tankCondensate.writeNbt(condensateFluidTag);
        nbt.put(CONDENSATE_FLUID_TAG, condensateFluidTag);
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
        for (ItemStack itemstack : this.items) {
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
        return ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM) != null;
    }

    private static int getTotalFreezeTime(Level level, IceMakerBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(IceMakerRecipe::freezingTime).orElse(IceMakerRecipe.FREEZING_TIME);
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
        switch (tank) {
            case TANK_INPUT:
                this.inputFluidStack.setFluidVariant(this.tank.variant);
                this.inputFluidStack.setAmount(this.tank.amount);
                return this.inputFluidStack.copy();
            case TANK_CONDENSATE:
                this.condensateFluidStack.setFluidVariant(this.tankCondensate.variant);
                this.condensateFluidStack.setAmount(this.tankCondensate.amount);
                return this.condensateFluidStack.copy();
            default:
                throw new IndexOutOfBoundsException(tank);
        }
    }

    @Override
    public void setFluidStack(int tank, FluidStack fluidStack) {
        switch (tank) {
            case TANK_INPUT:
                this.tank.variant = fluidStack.getFluidVariant();
                this.tank.amount = fluidStack.getAmount();
                this.inputFluidStack = fluidStack;
            case TANK_CONDENSATE:
                this.tankCondensate.variant = fluidStack.getFluidVariant();
                this.tankCondensate.amount = fluidStack.getAmount();
                this.condensateFluidStack = fluidStack;
            default:
                throw new IndexOutOfBoundsException(tank);
        }
    }

    @Override
    public int getTankSize() {
        return COUNT_TANKS;
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage(int tank) {
        return switch (tank) {
            case TANK_INPUT -> this.tank;
            case TANK_CONDENSATE -> this.tankCondensate;
            default -> throw new IndexOutOfBoundsException(tank);
        };
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
        return new ClientboundIceMakerFluidSyncPacket("ice_maker", List.of(this.getFluidStack(0), this.getFluidStack(1)));
    }
}
