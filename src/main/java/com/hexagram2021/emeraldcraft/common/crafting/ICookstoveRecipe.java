package com.hexagram2021.emeraldcraft.common.crafting;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public interface ICookstoveRecipe extends Recipe<CookstoveBlockEntity>, IPartialMatchRecipe<Container> {
    FluidStack fluidStack();

    Ingredient container();

    ICookstoveDisplay display();

    int cookTime();
}
