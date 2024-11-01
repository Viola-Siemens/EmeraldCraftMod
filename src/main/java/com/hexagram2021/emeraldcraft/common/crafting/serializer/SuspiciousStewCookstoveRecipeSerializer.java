package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.SuspiciousStewCookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class SuspiciousStewCookstoveRecipeSerializer<T extends SuspiciousStewCookstoveRecipe> implements RecipeSerializer<T> {
	private final SuspiciousStewCookstoveRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public SuspiciousStewCookstoveRecipeSerializer(SuspiciousStewCookstoveRecipeSerializer.Creator<T> factory, int defaultCookingTime) {
		this.factory = factory;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(FluidStack.CODEC, "fluid", FluidStack.EMPTY).forGetter(SuspiciousStewCookstoveRecipe::fluidStack),
						ExtraCodecs.strictOptionalField(Ingredient.CODEC, "container", Ingredient.EMPTY).forGetter(SuspiciousStewCookstoveRecipe::container),
						CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(SuspiciousStewCookstoveRecipe::result),
						ICookstoveDisplay.CODEC.fieldOf("display").forGetter(SuspiciousStewCookstoveRecipe::display),
						Codec.INT.fieldOf("cookTime").orElse(defaultCookingTime).forGetter(SuspiciousStewCookstoveRecipe::cookTime)
				).apply(instance, factory::create)
		);
	}

	@Override
	public Codec<T> codec() {
		return this.codec;
	}

	@Override
	public T fromNetwork(FriendlyByteBuf buf) {
		FluidStack fluidStack = FluidStack.readFromPacket(buf);
		Ingredient container = Ingredient.fromNetwork(buf);
		ItemStack result = buf.readItem();
		ICookstoveDisplay display = ICookstoveDisplay.fromNetwork(buf);
		int cookTime = buf.readVarInt();
		return this.factory.create(fluidStack, container, result, display, cookTime);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, SuspiciousStewCookstoveRecipe recipe) {
		recipe.fluidStack().writeToPacket(buf);
		recipe.container().toNetwork(buf);
		buf.writeItem(recipe.result());
		recipe.display().toNetwork(buf);
		buf.writeVarInt(recipe.cookTime());
	}

	public interface Creator<T extends SuspiciousStewCookstoveRecipe> {
		T create(FluidStack fluidStack, Ingredient container, ItemStack result, ICookstoveDisplay display, int cookTime);
	}
}
