package com.hexagram2021.emeraldcraft.common.crafting.compat.jei.replacers;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.CookstoveItemsDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;

import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public final class SuspiciousStewCookstoveRecipeMaker {
    public static Stream<CookstoveRecipe> createRecipesStream() {
        Item brownMushroomItem = Blocks.BROWN_MUSHROOM.asItem();
        Item redMushroomItem = Blocks.RED_MUSHROOM.asItem();
        Ingredient brownMushroom = Ingredient.of(brownMushroomItem);
        Ingredient redMushroom = Ingredient.of(redMushroomItem);
        Ingredient bowl = Ingredient.of(Items.BOWL);

        return BuiltInRegistries.ITEM.getTag(ItemTags.SMALL_FLOWERS)
                .stream()
                .flatMap(HolderSet.ListBacked::stream)
                .map(Holder::value)
                .filter(BlockItem.class::isInstance)
                .map(item -> ((BlockItem) item).getBlock())
                .filter(FlowerBlock.class::isInstance)
                .map(FlowerBlock.class::cast)
                .map(flowerBlock -> {
                    Item flowerItem = flowerBlock.asItem();
                    Ingredient flower = Ingredient.of(flowerItem);
                    NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, brownMushroom, redMushroom, flower);
                    ItemStack output = new ItemStack(Items.SUSPICIOUS_STEW, 1);
                    SuspiciousStewItem.saveMobEffect(output, flowerBlock.getSuspiciousEffect(), flowerBlock.getEffectDuration());
                    ResourceLocation flowerId = getRegistryName(flowerBlock);
                    return new CookstoveRecipe(
                            new ResourceLocation(MODID, "jei/cookstove/suspicious_stew/" + flowerId.getNamespace() + "_" + flowerId.getPath()),
                            inputs, FluidStack.EMPTY, bowl, output,
                            new CookstoveItemsDisplay(
                                    new CookstoveItemsDisplay.Background(0xCC9878, new ResourceLocation(MODID, "soup")),
                                    Ingredient.of(brownMushroomItem, redMushroomItem, flowerItem)
                            ),
                            CookstoveRecipe.COOK_TIME
                    );
                });
    }

    private SuspiciousStewCookstoveRecipeMaker() {
    }
}
