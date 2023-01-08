package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccess {
	@Accessor("recipes")
	Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> ec_getRecipes();

	@Accessor("recipes")
	void ec_setRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> newRecipes);
}
