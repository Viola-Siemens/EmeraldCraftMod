package com.hexagram2021.emeraldcraft.common.crafting.compat.jei.replacers;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.CookstoveItemsDisplay;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class CookedDumplingCookstoveRecipeMaker {
    @SuppressWarnings("UnstableApiUsage")
    public static Stream<CookstoveRecipe> createRecipesStream() {
        Ingredient rawDumpling = Ingredient.of(ECItems.RAW_DUMPLING);
        return IntStream.rangeClosed(1, CookstoveBlockEntity.COUNT_SLOTS).mapToObj(count -> {
            NonNullList<Ingredient> inputs = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int i = 0; i < count; ++i) {
                inputs.set(i, rawDumpling);
            }
            return new CookstoveRecipe(
                    new ResourceLocation(MODID, "jei/cookstove/dumpling_" + count),
                    inputs, new FluidStack(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET), Ingredient.EMPTY, new ItemStack(ECItems.COOKED_DUMPLING, count),
                    new CookstoveItemsDisplay(
                            new CookstoveItemsDisplay.Background(0xFCFCFC, new ResourceLocation(MODID, "soup")),
                            Ingredient.of(ECItems.COOKED_DUMPLING)
                    ),
                    CookstoveRecipe.COOK_TIME
            );
        });
    }

    private CookedDumplingCookstoveRecipeMaker() {
    }
}
