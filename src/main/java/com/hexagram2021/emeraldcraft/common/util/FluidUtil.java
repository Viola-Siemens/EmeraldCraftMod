package com.hexagram2021.emeraldcraft.common.util;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.mixin.accessor.BucketItemAccess;
import com.hexagram2021.emeraldcraft.mixin.accessor.EmptyBucketStorageAccess;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.fluid.EmptyBucketStorage;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings({"UnstableApiUsage", "removal"})
public final class FluidUtil {
    public static final long BOTTLE_VOLUME = FluidStack.convertMbToDroplets(250);

    public static boolean drainFromItemToTank(Player player, SingleVariantStorage<FluidVariant> fluidHandler, ItemStack itemStack) {
        if (!canDrain(itemStack)) {
            return false;
        }

        Pair<FluidStack, ItemStack> emptyingResult = drainFromItem(fluidHandler, itemStack, true);
        FluidStack fluidStack = emptyingResult.getFirst();

        if (fluidStack.getAmount() != fluidHandler.simulateInsert(fluidStack.getFluidVariant(), fluidStack.getAmount(), null)) {
            return false;
        }

        ItemStack copyOfHeld = itemStack.copy();
        emptyingResult = drainFromItem(fluidHandler, copyOfHeld, false);
        try (Transaction transaction = Transaction.openOuter()) {
            fluidHandler.insert(fluidStack.getFluidVariant(), fluidStack.getAmount(), transaction);
            player.getInventory().placeItemBackInInventory(emptyingResult.getSecond());
            transaction.commit();
            return true;
        }
    }

    public static boolean fillFromTankToItem(Player player, Tank fluidHandler, int tankIndex, ItemStack itemStack) {
        if (!canFill(itemStack)) {
            return false;
        }
        Storage<FluidVariant> tank = ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM);
        if (tank == null) {
            return false;
        }
        for (int i = 0; i < fluidHandler.getTanks(); ++i) {
            FluidStack fluidStack = fluidHandler.getFluidInTank(i);
            if (fluidStack.isEmpty()) {
                continue;
            }
            long requiredFluidAmount = getRequiredFluidAmount(itemStack, tank, fluidStack.copy());
            if (requiredFluidAmount <= 0 || requiredFluidAmount > fluidStack.getAmount()) {
                continue;
            }
            ItemStack retItem = fillItem(requiredFluidAmount, itemStack, fluidStack.copy());

            FluidStack copy = fluidStack.copy();
            copy.setAmount(requiredFluidAmount);
            try (Transaction transaction = Transaction.openOuter()) {
                fluidHandler.getFluidStorage(tankIndex).extract(copy.getFluidVariant(), copy.getAmount(), transaction);
                player.getInventory().placeItemBackInInventory(retItem);
                transaction.commit();
                return true;
            }
        }
        return false;
    }

    private static boolean canDrain(ItemStack itemStack) {
        Storage<FluidVariant> tank = ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM);
        if (tank == null) {
            return false;
        }
        for (StorageView<FluidVariant> view : tank) {
            if (view.getAmount() > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean canFill(ItemStack itemStack) {
        Storage<FluidVariant> tank = ContainerItemContext.withConstant(itemStack).find(FluidStorage.ITEM);
        if (tank == null) {
            return false;
        }
        for (StorageView<FluidVariant> view : tank) {
            if (view.getAmount() < view.getCapacity()) {
                return true;
            }
        }
        return false;
    }

    private static Pair<FluidStack, ItemStack> drainFromItem(SingleVariantStorage<FluidVariant> storage, ItemStack itemStack, boolean simulate) {
        FluidStack retFluid = FluidStack.EMPTY;
        ItemStack retItem = ItemStack.EMPTY;

        ItemStack split = itemStack.copy();
        split.setCount(1);
        ContainerItemContext itemContext = ContainerItemContext.withConstant(split);
        Storage<FluidVariant> tank = itemContext.find(FluidStorage.ITEM);
        if (tank == null) {
            return Pair.of(retFluid, retItem);
        }
        long amount = storage.amount;
        long maxCapacity = FluidConstants.BUCKET;
        for (StorageView<FluidVariant> view : tank) {
            FluidVariant fluid = storage.variant.isBlank() ? view.getResource() : storage.variant;
            if (simulate) {
                amount = tank.simulateExtract(fluid, maxCapacity, null);
            } else {
                try (Transaction transaction = Transaction.openOuter()) {
                    amount = tank.extract(fluid, maxCapacity, transaction);
                    transaction.commit();
                }
            }
            retFluid.setFluidVariant(fluid);
            if (view instanceof SingleSlotStorage<FluidVariant>)
                break;
        }
        retFluid.grow(amount);
        retItem = itemContext.getItemVariant().toStack().copy();
        if (!simulate) {
            itemStack.shrink(1);
        }

        return Pair.of(retFluid, retItem.is(Items.WATER_BUCKET) || retItem.is(Items.LAVA_BUCKET) ? new ItemStack(Items.BUCKET) : retItem);
    }

    private static long getRequiredFluidAmount(ItemStack itemStack, Storage<FluidVariant> tank, FluidStack
            availableFluid) {
        if (itemStack.getItem() == Items.GLASS_BOTTLE) {
            return BOTTLE_VOLUME;
        }
        if (itemStack.getItem() == Items.BUCKET) {
            return (int) FluidConstants.BUCKET;
        }

        if (tank instanceof EmptyBucketStorage) {
            Item filledBucket = availableFluid.getFluidVariant().getFluid().getBucket();
            if (filledBucket == Items.AIR) {
                return 0;
            }
            Item item = ((EmptyBucketStorageAccess) tank).getContext().getItemVariant().getItem();
            FluidStack fluidStack;
            if (item instanceof BucketItem) {
                fluidStack = new FluidStack(FluidVariant.of(((BucketItemAccess) item).getContent()), FluidStack.convertMbToDroplets(1000));
            } else {
                fluidStack = /*item instanceof MilkBucketItem ? new FluidStack((Fluid) Fluids.MILK.get(), 1000) : */FluidStack.EMPTY;
            }
            if (!fluidStack.isEmpty()) {
                return 0;
            }
            return (int) FluidConstants.BUCKET;
        }

        return (int) tank.simulateInsert(availableFluid.getFluidVariant(), availableFluid.getAmount(), null);
    }

    @SuppressWarnings("deprecation")
    private static ItemStack fillItem(long requiredAmount, ItemStack itemStack, FluidStack availableFluid) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount);

        if (itemStack.getItem() == Items.GLASS_BOTTLE) {
            ItemStack fillBottle;
            Fluid fluid = toFill.getFluidVariant().getFluid();
            if (fluid.is(FluidTags.WATER)) {
                fillBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
            } else {
                fillBottle = fillBottle(itemStack, toFill);
            }
            itemStack.shrink(1);
            return fillBottle;
        }

        ItemStack split = itemStack.copy();
        split.setCount(1);
        ContainerItemContext itemContext = ContainerItemContext.withConstant(split);
        Storage<FluidVariant> tank = itemContext.find(FluidStorage.ITEM);

        if (tank == null) {
            return ItemStack.EMPTY;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            tank.insert(toFill.getFluidVariant(), requiredAmount, transaction);
            ItemStack container = itemContext.getItemVariant().toStack().copy();
            itemStack.shrink(1);
            return container;
        }
    }

    @SuppressWarnings("unused")
    private static ItemStack fillBottle(ItemStack itemStack, FluidStack toFill) {
        // Inject me!
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    private FluidUtil() {
    }
}
