package com.hexagram2021.emeraldcraft.common.blocks.entity;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.workstation.MelterBlock;
import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.menu.MelterMenu;
import com.hexagram2021.emeraldcraft.common.register.ECBlockEntity;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import com.hexagram2021.emeraldcraft.network.ClientboundFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.ClientboundMelterFluidSyncPacket;
import com.hexagram2021.emeraldcraft.network.NetworkHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.registry.FuelRegistry;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class MelterBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, Tank, ISynchronizableContainer {
    public static final long MAX_FLUID_LEVEL = FluidConstants.BUCKET * 10;
    public static final int TANK_OUTPUT = 0;
    public static final int COUNT_TANKS = 1;

    private static final int[] SLOTS_FOR_UP = new int[]{2, 0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{3, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{2, 1};

    protected NonNullList<ItemStack> items = NonNullList.withSize(MelterMenu.SLOT_COUNT, ItemStack.EMPTY);
    int litTime;
    int litDuration;
    int meltingProgress;
    int meltingTotalTime;
    final SingleFluidStorage tank = SingleFluidStorage.withFixedCapacity(MAX_FLUID_LEVEL, MelterBlockEntity.this::markDirty);
    final SingleItemStorage[] itemStorages = new SingleItemStorage[]{
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage(),
            new BaseBlockEntityItemStorage()
    };

    private FluidStack fluidStack = FluidStack.EMPTY;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            return switch (index) {
                case 0 -> MelterBlockEntity.this.litTime;
                case 1 -> MelterBlockEntity.this.litDuration;
                case 2 -> MelterBlockEntity.this.meltingProgress;
                case 3 -> MelterBlockEntity.this.meltingTotalTime;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> MelterBlockEntity.this.litTime = value;
                case 1 -> MelterBlockEntity.this.litDuration = value;
                case 2 -> MelterBlockEntity.this.meltingProgress = value;
                case 3 -> MelterBlockEntity.this.meltingTotalTime = value;
            }
        }

        public int getCount() {
            return MelterMenu.DATA_COUNT;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<Container, MelterRecipe> quickCheck;

    public MelterBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntity.MELTER, pos, state);
        this.quickCheck = RecipeManager.createCheck(ECRecipes.MELTER_TYPE);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @SuppressWarnings("ConstantValue")
    public static void serverTick(Level level, BlockPos pos, BlockState blockState, MelterBlockEntity blockEntity) {
        boolean isBurning = blockEntity.isLit();
        boolean changed = false;
        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        ItemStack fuelItemStack = blockEntity.items.get(MelterMenu.FUEL_SLOT);
        boolean inputExists = !blockEntity.items.get(MelterMenu.INGREDIENT_SLOT).isEmpty();
        boolean fuelExists = !fuelItemStack.isEmpty();
        if (blockEntity.isLit() || fuelExists && inputExists) {
            MelterRecipe recipe;
            if (inputExists) {
                recipe = blockEntity.quickCheck.getRecipeFor(blockEntity, level).orElse(null);
            } else {
                recipe = null;
            }
            if (!blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
                blockEntity.litTime = blockEntity.getBurnDuration(fuelItemStack);
                blockEntity.litDuration = blockEntity.litTime;
                if (blockEntity.isLit()) {
                    changed = true;
                    ItemStack remainder = fuelItemStack.getRecipeRemainder();
                    if (!remainder.isEmpty()) {
                        blockEntity.items.set(MelterMenu.FUEL_SLOT, remainder);
                    } else if (!fuelItemStack.isEmpty()) {
                        fuelItemStack.shrink(1);
                        if (fuelItemStack.isEmpty()) {
                            blockEntity.items.set(MelterMenu.FUEL_SLOT, remainder);
                        }
                    }
                }
            }

            if (blockEntity.isLit() && blockEntity.canBurn(recipe, blockEntity.items)) {
                ++blockEntity.meltingProgress;
                if (blockEntity.meltingProgress >= blockEntity.meltingTotalTime) {
                    blockEntity.meltingProgress = 0;
                    blockEntity.meltingTotalTime = getTotalMeltTime(level, blockEntity);
                    blockEntity.burn(recipe, blockEntity.items);

                    changed = true;
                }
            } else {
                blockEntity.meltingProgress = 0;
            }
        } else if (!blockEntity.isLit() && blockEntity.meltingProgress > 0) {
            blockEntity.meltingProgress = Mth.clamp(blockEntity.meltingProgress - 2, 0, blockEntity.meltingTotalTime);
        }

        if (isBurning != blockEntity.isLit()) {
            changed = true;
            blockState = blockState.setValue(MelterBlock.LIT, blockEntity.isLit());
            level.setBlock(pos, blockState, Block.UPDATE_ALL);
        }

        ItemStack resultInput = blockEntity.items.get(MelterMenu.RESULT_INPUT_SLOT);
        ItemStack resultOutput = blockEntity.items.get(MelterMenu.RESULT_OUTPUT_SLOT);
        Item bucket = blockEntity.tank.variant.getFluid().getBucket();
        if (!resultInput.isEmpty()) {
            if (resultInput.is(bucket)) {

                if (blockEntity.tank.amount <= MAX_FLUID_LEVEL - FluidConstants.BUCKET) {
                    if (resultOutput.isEmpty()) {
                        blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
                    } else if (resultOutput.is(Items.BUCKET) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
                        resultOutput.grow(1);
                    } else {
                        return;
                    }
                    resultInput.shrink(1);
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.insert(blockEntity.tank.variant, FluidConstants.BUCKET, transaction);
                        blockEntity.fluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.fluidStack.setAmount(blockEntity.tank.amount);
                        changed = true;
                        transaction.commit();
                    }
                }
            } else if (resultInput.is(Items.BUCKET)) {
                if (blockEntity.tank.amount >= FluidConstants.BUCKET) {
                    if (resultOutput.isEmpty()) {
                        blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(bucket));
                    } else if (resultOutput.is(bucket) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
                        resultOutput.grow(1);
                    } else {
                        return;
                    }
                    resultInput.shrink(1);
                    try (Transaction transaction = Transaction.openOuter()) {
                        blockEntity.tank.extract(blockEntity.tank.variant, FluidConstants.BUCKET, transaction);
                        blockEntity.fluidStack.setFluidVariant(blockEntity.tank.variant);
                        blockEntity.fluidStack.setAmount(blockEntity.tank.amount);
                        changed = true;
                        transaction.commit();
                    }
                }
            } else if (blockEntity.tank.isResourceBlank() || blockEntity.tank.amount <= 0) {
                Storage<FluidVariant> c = ContainerItemContext.withConstant(resultInput).find(FluidStorage.ITEM);
                if (c != null) {
                    if (resultOutput.isEmpty()) {
                        blockEntity.items.set(MelterMenu.RESULT_OUTPUT_SLOT, new ItemStack(Items.BUCKET));
                    } else if (resultOutput.is(Items.BUCKET) && resultOutput.getCount() < resultOutput.getMaxStackSize()) {
                        resultOutput.grow(1);
                    } else {
                        return;
                    }
                    FluidStack newFluidStack = FluidStack.EMPTY;
                    for (StorageView<FluidVariant> view : c) {
                        if (!view.isResourceBlank()) {
                            newFluidStack.setFluidVariant(view.getResource());
                            newFluidStack.setAmount(view.getAmount());
                            if (view instanceof SingleSlotStorage<FluidVariant>)
                                break;
                        }
                    }
                    blockEntity.setFluidStack(0, newFluidStack);
                    resultInput.shrink(1);
                    changed = true;
                }
            }

            if (changed) {
                blockEntity.markDirty();
                setChanged(level, pos, blockState);
            }
        }
    }

    @Contract("null,_->false")
    private boolean canBurn(@Nullable MelterRecipe recipe, NonNullList<ItemStack> container) {
        if (recipe == null || container.get(0).isEmpty()) {
            return false;
        }
        FluidStack fluidStack = this.getFluidStack(0);
        if (!recipe.resultFluid().isFluidEqual(fluidStack)) {
            return fluidStack.isEmpty();
        }
        return recipe.resultFluid().getAmount() + fluidStack.getAmount() <= MAX_FLUID_LEVEL;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Contract("null,_->false")
    private boolean burn(@Nullable MelterRecipe recipe, NonNullList<ItemStack> container) {
        if (this.canBurn(recipe, container)) {
            ItemStack itemstack = container.get(0);
            try (Transaction transaction = Transaction.openOuter()) {
                this.tank.insert(recipe.resultFluid().getFluidVariant(), recipe.resultFluid().getAmount(), transaction);
                this.fluidStack.setFluidVariant(this.tank.variant);
                this.fluidStack.setAmount(this.tank.amount);
                transaction.commit();
            }
            itemstack.shrink(1);
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.litTime = nbt.getInt("BurnTime");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        this.meltingProgress = nbt.getInt("MeltTime");
        this.meltingTotalTime = nbt.getInt("MeltTimeTotal");
        this.tank.readNbt(nbt);
        CompoundTag compoundtag = nbt.getCompound("RecipesUsed");

        for (String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("BurnTime", this.litTime);
        nbt.putInt("MeltTime", this.meltingProgress);
        nbt.putInt("MeltTimeTotal", this.meltingTotalTime);
        this.tank.writeNbt(nbt);
        ContainerHelper.saveAllItems(nbt, this.items);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((id, value) -> compoundtag.putInt(id.toString(), value));
        nbt.put("RecipesUsed", compoundtag);
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
    protected Component getDefaultName() {
        return Component.translatable("container.melter");
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
        ItemStack itemstack = this.items.get(index);
        boolean flag = !itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, itemstack);
        this.items.set(index, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !flag) {
            this.meltingTotalTime = getTotalMeltTime(Objects.requireNonNull(this.level), this);
            this.meltingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack itemStack) {
        if (index == MelterMenu.RESULT_INPUT_SLOT || index == MelterMenu.RESULT_OUTPUT_SLOT) {
            return ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM) != null;
        }
        if (index != MelterMenu.FUEL_SLOT) {
            return true;
        }
        ItemStack fuelItemStack = this.items.get(MelterMenu.FUEL_SLOT);
        Integer burnTime = FuelRegistry.INSTANCE.get(itemStack.getItem());
        return (burnTime != null && burnTime > 0) || itemStack.is(Items.BUCKET) && !fuelItemStack.is(Items.BUCKET);
    }

    protected int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }
        Integer burnTime = FuelRegistry.INSTANCE.get(itemStack.getItem());
        return burnTime != null ? burnTime : 0;
    }

    private static int getTotalMeltTime(Level level, MelterBlockEntity blockEntity) {
        return blockEntity.quickCheck.getRecipeFor(blockEntity, level).map(MelterRecipe::meltingTime).orElse(MelterRecipe.MELTING_TIME);
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

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            Objects.requireNonNull(this.level).getRecipeManager().byKey(entry.getKey()).ifPresent(list::add);
        }
        player.awardRecipes(list);

        this.recipesUsed.clear();
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
        if (direction == Direction.DOWN && index == 1) {
            return itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
        }
        return true;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new MelterMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public FluidStack getFluidStack(int index) {
        if (index >= COUNT_TANKS) {
            throw new IndexOutOfBoundsException(index);
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
        return new ClientboundMelterFluidSyncPacket("melter", List.of(this.getFluidStack(0)));
    }
}
