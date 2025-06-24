package com.hexagram2021.emeraldcraft.common.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import static net.minecraftforge.fluids.FluidType.BUCKET_VOLUME;

public final class FluidUtil {
	public static final int BOTTLE_VOLUME = 250;

	public static boolean drainFromItemToTank(Player player, IFluidHandler fluidHandler, ItemStack itemStack) {
		if(!canDrain(itemStack)) {
			return false;
		}

		Pair<FluidStack, ItemStack> emptyingResult = drainFromItem(itemStack, true);
		FluidStack fluidStack = emptyingResult.getFirst();

		if (fluidStack.getAmount() != fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)) {
			return false;
		}

		ItemStack copyOfHeld = itemStack.copy();
		emptyingResult = drainFromItem(copyOfHeld, false);
		fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
		player.getInventory().placeItemBackInInventory(emptyingResult.getSecond());
		return true;
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	public static boolean fillFromTankToItem(Player player, IFluidHandler fluidHandler, ItemStack itemStack) {
		if(!canFill(itemStack)) {
			return false;
		}

		LazyOptional<IFluidHandlerItem> capability = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		IFluidHandlerItem tank = capability.orElse(null);
		if(tank == null) {
			return false;
		}
		for(int i = 0; i < fluidHandler.getTanks(); ++i) {
			FluidStack fluidStack = fluidHandler.getFluidInTank(i);
			if(fluidStack.isEmpty()) {
				continue;
			}
			int requiredFluidAmount = getRequiredFluidAmount(itemStack, tank, fluidStack.copy());
			if(requiredFluidAmount <= 0 || requiredFluidAmount > fluidStack.getAmount()) {
				continue;
			}
			ItemStack retItem = fillItem(requiredFluidAmount, itemStack, fluidStack.copy());

			FluidStack copy = fluidStack.copy();
			copy.setAmount(requiredFluidAmount);
			fluidHandler.drain(copy, IFluidHandler.FluidAction.EXECUTE);
			player.getInventory().placeItemBackInInventory(retItem);
			return true;
		}
		return false;
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	private static boolean canDrain(ItemStack itemStack) {
		LazyOptional<IFluidHandlerItem> capability = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		IFluidHandlerItem tank = capability.orElse(null);
		if (tank == null) {
			return false;
		}
		for (int i = 0; i < tank.getTanks(); i++) {
			if (tank.getFluidInTank(i).getAmount() > 0) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	private static boolean canFill(ItemStack itemStack) {
		LazyOptional<IFluidHandlerItem> capability = itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		IFluidHandlerItem tank = capability.orElse(null);
		if (tank == null) {
			return false;
		}
		for (int i = 0; i < tank.getTanks(); i++) {
			if (tank.getFluidInTank(i).getAmount() < tank.getTankCapacity(i)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	private static Pair<FluidStack, ItemStack> drainFromItem(ItemStack itemStack, boolean simulate) {
		FluidStack retFluid = FluidStack.EMPTY;
		ItemStack retItem = ItemStack.EMPTY;

		ItemStack split = itemStack.copy();
		split.setCount(1);
		LazyOptional<IFluidHandlerItem> capability = split.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		IFluidHandlerItem tank = capability.orElse(null);
		if (tank == null) {
			return Pair.of(retFluid, retItem);
		}
		retFluid = tank.drain(BUCKET_VOLUME, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
		retItem = tank.getContainer().copy();
		if(!simulate) {
			itemStack.shrink(1);
		}

		return Pair.of(retFluid, retItem);
	}

	@SuppressWarnings("ConstantValue")
	private static int getRequiredFluidAmount(ItemStack itemStack, IFluidHandlerItem tank, FluidStack availableFluid) {
		if (itemStack.getItem() == Items.GLASS_BOTTLE) {
			return BOTTLE_VOLUME;
		}
		if (itemStack.getItem() == Items.BUCKET) {
			return BUCKET_VOLUME;
		}

		if (tank instanceof FluidBucketWrapper) {
			Item filledBucket = availableFluid.getFluid().getBucket();
			if (filledBucket == null || filledBucket == Items.AIR) {
				return 0;
			}
			if (!((FluidBucketWrapper) tank).getFluid().isEmpty()) {
				return 0;
			}
			return BUCKET_VOLUME;
		}

		return tank.fill(availableFluid, IFluidHandler.FluidAction.SIMULATE);
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue", "deprecation"})
	private static ItemStack fillItem(int requiredAmount, ItemStack itemStack, FluidStack availableFluid) {
		FluidStack toFill = availableFluid.copy();
		toFill.setAmount(requiredAmount);
		availableFluid.shrink(requiredAmount);

		if (itemStack.getItem() == Items.GLASS_BOTTLE) {
			ItemStack fillBottle;
			Fluid fluid = toFill.getFluid();
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
		LazyOptional<IFluidHandlerItem> capability = split.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		IFluidHandlerItem tank = capability.orElse(null);
		if (tank == null) {
			return ItemStack.EMPTY;
		}
		tank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
		ItemStack container = tank.getContainer().copy();
		itemStack.shrink(1);
		return container;
	}

	@SuppressWarnings("unused")
	private static ItemStack fillBottle(ItemStack itemStack, FluidStack toFill) {
		// Inject me!
		return new ItemStack(Items.GLASS_BOTTLE);
	}

	private FluidUtil() {
	}
}
