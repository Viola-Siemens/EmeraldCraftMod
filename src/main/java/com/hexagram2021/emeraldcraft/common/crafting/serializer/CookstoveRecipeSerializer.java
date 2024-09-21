package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fluids.FluidStack;

public class CookstoveRecipeSerializer<T extends CookstoveRecipe> implements RecipeSerializer<T> {
	private final CookstoveRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public CookstoveRecipeSerializer(CookstoveRecipeSerializer.Creator<T> factory, int defaultCookingTime) {
		this.factory = factory;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
							Ingredient[] array = ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
							if (array.length == 0) {
								return DataResult.error(() -> "No ingredients for cookstove recipe");
							}
							return array.length > CookstoveBlockEntity.COUNT_SLOTS ?
									DataResult.error(() -> "Too many ingredients for cookstove recipe") :
									DataResult.success(NonNullList.of(Ingredient.EMPTY, array));
						}, DataResult::success).forGetter(CookstoveRecipe::getIngredients),
						ExtraCodecs.strictOptionalField(FluidStack.CODEC, "fluid", FluidStack.EMPTY).forGetter(CookstoveRecipe::getFluidStack),
						ExtraCodecs.strictOptionalField(Ingredient.CODEC, "container", Ingredient.EMPTY).forGetter(CookstoveRecipe::getContainer),
						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(CookstoveRecipe::getResult),
						Codec.INT.fieldOf("cookTime").orElse(defaultCookingTime).forGetter(CookstoveRecipe::getCookingTime)
				).apply(instance, factory::create)
		);
	}

	@Override
	public Codec<T> codec() {
		return this.codec;
	}

	@Override
	public T fromNetwork(FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
		ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buf));

		FluidStack fluidStack = FluidStack.readFromPacket(buf);
		Ingredient container = Ingredient.fromNetwork(buf);
		ItemStack result = buf.readItem();
		int cookTime = buf.readVarInt();
		return this.factory.create(ingredients, fluidStack, container, result, cookTime);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, CookstoveRecipe recipe) {
		buf.writeVarInt(recipe.getIngredients().size());
		for(Ingredient ingredient: recipe.getIngredients()) {
			ingredient.toNetwork(buf);
		}

		recipe.getFluidStack().writeToPacket(buf);
		recipe.getContainer().toNetwork(buf);
		buf.writeItem(recipe.getResult());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends CookstoveRecipe> {
		T create(NonNullList<Ingredient> ingredients, FluidStack fluidStack, Ingredient container, ItemStack result, int cookTime);
	}
}
