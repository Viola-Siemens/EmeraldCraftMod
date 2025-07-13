package com.hexagram2021.emeraldcraft.common.crafting;

import cn.sh1rocu.emeraldcraft.util.RecipeUtil;
import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.hexagram2021.emeraldcraft.common.crafting.serializer.CookstoveRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.NbtIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record CookstoveRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack fluidStack,
                              Ingredient container, ItemStack result, ICookstoveDisplay display,
                              int cookTime, boolean isSimple) implements ICookstoveRecipe {
    public static final CachedRecipeList<CookstoveRecipe> recipeList = new CachedRecipeList<>(
            ECRecipes.COOKSTOVE_TYPE,
            CookstoveRecipe.class
    );

    public static final int COOK_TIME = 100;

    @SuppressWarnings("UnstableApiUsage")
    public CookstoveRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, FluidStack fluidStack, Ingredient container, ItemStack result, ICookstoveDisplay display, int cookTime) {
        this(id, ingredients, fluidStack, container, result, display, cookTime, ingredients.stream().noneMatch(ingredient ->
                ingredient.getCustomIngredient() instanceof NbtIngredient
        ));
    }

    @Override
    public boolean matches(CookstoveBlockEntity container, Level level) {
        StackedContents stackedcontents = new StackedContents();
        List<ItemStack> inputs = Lists.newArrayList();
        int count = 0;
        FluidStack inputFluid = container.getFluidStack(CookstoveBlockEntity.TANK_INPUT);
        if (!inputFluid.containsFluid(this.fluidStack)) {
            return false;
        }
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                count += 1;
                if (this.isSimple) {
                    stackedcontents.accountStack(itemStack, 1);
                } else {
                    inputs.add(itemStack);
                }
            }
        }

        if (count != this.ingredients.size()) return false;
        if (this.isSimple) {
            return stackedcontents.canCraft(this, null);
        } else {
            RecipeUtil.findMatches(inputs, this.ingredients);
            return true;
        }
    }

    @Override
    public boolean matchesAllowEmpty(Container container) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.isEmpty() || this.ingredients.stream().anyMatch(ingredient -> ingredient.test(itemStack))) {
                count += 1;
            }
        }
        return count == container.getContainerSize();
    }

    @Override
    public ItemStack assemble(CookstoveBlockEntity container, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public CookstoveRecipeSerializer<CookstoveRecipe> getSerializer() {
        return ECRecipeSerializer.COOKSTOVE_SERIALIZER;
    }

    @Override
    public RecipeType<CookstoveRecipe> getType() {
        return ECRecipes.COOKSTOVE_TYPE;
    }
}
