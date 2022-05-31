package com.hexgram2021.emeraldcraft.data;

import com.google.gson.JsonObject;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks.*;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
	private final Path ADV_ROOT;
	private final HashMap<String, Integer> PATH_COUNT = new HashMap<>();

	public Recipes(DataGenerator gen) {
		super(gen);
		ADV_ROOT = gen.getOutputFolder().resolve("data/minecraft/advancements/recipes/root.json");
	}

	@Override
	protected void saveAdvancement(HashCache cache, JsonObject json, Path path) {
		if(path.equals(ADV_ROOT)) return; //We NEVER care about this.
		super.saveAdvancement(cache, json, path);
	}

	private String toPath(ItemLike src) {
		return src.asItem().getRegistryName().getPath();
	}

	private ResourceLocation toRL(String s) {
		if(!s.contains("/"))
			s = "crafting/"+s;
		if(PATH_COUNT.containsKey(s))
		{
			int count = PATH_COUNT.get(s)+1;
			PATH_COUNT.put(s, count);
			return new ResourceLocation(EmeraldCraft.MODID, s+count);
		}
		PATH_COUNT.put(s, 1);
		return new ResourceLocation(EmeraldCraft.MODID, s);
	}

	private void addArmor(ItemLike input, Map<EquipmentSlot, ? extends ItemLike> items, String name, Consumer<FinishedRecipe> out) {
		ItemLike feet = items.get(EquipmentSlot.FEET);
		ItemLike legs = items.get(EquipmentSlot.LEGS);
		ItemLike chest = items.get(EquipmentSlot.CHEST);
		ItemLike head = items.get(EquipmentSlot.HEAD);
		ShapedRecipeBuilder.shaped(feet)
				.pattern("x x")
				.pattern("x x")
				.define('x', input)
				.unlockedBy("has_"+name, has(input))
				.save(out, toRL(toPath(feet)));
		ShapedRecipeBuilder.shaped(legs)
				.pattern("xxx")
				.pattern("x x")
				.pattern("x x")
				.define('x', input)
				.unlockedBy("has_"+name, has(input))
				.save(out, toRL(toPath(legs)));
		ShapedRecipeBuilder.shaped(chest)
				.pattern("x x")
				.pattern("xxx")
				.pattern("xxx")
				.define('x', input)
				.unlockedBy("has_"+name, has(input))
				.save(out, toRL(toPath(chest)));
		ShapedRecipeBuilder.shaped(head)
				.pattern("xxx")
				.pattern("x x")
				.define('x', input)
				.unlockedBy("has_"+name, has(input))
				.save(out, toRL(toPath(head)));
	}

	private void add3x3Conversion(ItemLike bigItem, ItemLike smallItem, Consumer<FinishedRecipe> out) {
		ShapedRecipeBuilder.shaped(bigItem)
				.define('s', smallItem)
				.pattern("sss")
				.pattern("sss")
				.pattern("sss")
				.unlockedBy("has_"+toPath(smallItem), has(smallItem))
				.save(out, toRL(toPath(smallItem)+"_to_")+toPath(bigItem));
	}

	private void addSlab(ItemLike block, ItemLike slab, Consumer<FinishedRecipe> out) {
		ShapedRecipeBuilder.shaped(slab, 6)
				.define('s', block)
				.pattern("sss")
				.unlockedBy("has_"+toPath(block), has(block))
				.save(out, toRL(toPath(block)+"_to_slab"));
	}

	private void addStairs(ItemLike block, ItemLike stair, Consumer<FinishedRecipe> out) {
		ShapedRecipeBuilder.shaped(stair, 4)
				.define('s', block)
				.pattern("s  ")
				.pattern("ss ")
				.pattern("sss")
				.unlockedBy("has_"+toPath(block), has(block))
				.save(out, toRL(toPath(block)+"_to_stair"));
	}

	private void addWall(ItemLike block, ItemLike stair, Consumer<FinishedRecipe> out) {
		ShapedRecipeBuilder.shaped(stair, 6)
				.define('w', block)
				.pattern("www")
				.pattern("www")
				.unlockedBy("has_"+toPath(block), has(block))
				.save(out, toRL(toPath(block)+"_to_wall"));
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> out) {
		recipesDecorations(out);
		addArmor(Items.EMERALD, ECItems.EMERALD_ARMOR, "emerald", out);
		addArmor(Items.LAPIS_LAZULI, ECItems.LAPIS_ARMOR, "lapis", out);
	}

	private void recipesDecorations(@Nonnull Consumer<FinishedRecipe> out) {
		for(Map.Entry<ResourceLocation, BlockEntry<SlabBlock>> blockSlab : ECBlocks.TO_SLAB.entrySet()) {
			addSlab(ForgeRegistries.BLOCKS.getValue(blockSlab.getKey()), blockSlab.getValue(), out);
		}
		for(Map.Entry<ResourceLocation, BlockEntry<StairBlock>> blockStairs : ECBlocks.TO_STAIRS.entrySet()) {
			addStairs(ForgeRegistries.BLOCKS.getValue(blockStairs.getKey()), blockStairs.getValue(), out);
		}
		for(Map.Entry<ResourceLocation, BlockEntry<WallBlock>> blockWall : ECBlocks.TO_WALL.entrySet()) {
			addWall(ForgeRegistries.BLOCKS.getValue(blockWall.getKey()), blockWall.getValue(), out);
		}

		add3x3Conversion(Items.WARPED_WART_BLOCK, ECItems.WARPED_WART, out);
	}
}
