package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.CookedDumplingCookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.crafting.display.ICookstoveDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

public class CookedDumplingCookstoveRecipeSerializer<T extends CookedDumplingCookstoveRecipe> implements RecipeSerializer<T> {
	private final CookedDumplingCookstoveRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public CookedDumplingCookstoveRecipeSerializer(CookedDumplingCookstoveRecipeSerializer.Creator<T> factory, int defaultCookingTime) {
		this.factory = factory;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(FluidStack.CODEC, "fluid", FluidStack.EMPTY).forGetter(CookedDumplingCookstoveRecipe::fluidStack),
						ExtraCodecs.strictOptionalField(Ingredient.CODEC, "container", Ingredient.EMPTY).forGetter(CookedDumplingCookstoveRecipe::container),
						ICookstoveDisplay.CODEC.fieldOf("display").forGetter(CookedDumplingCookstoveRecipe::display),
						Codec.INT.fieldOf("cookTime").orElse(defaultCookingTime).forGetter(CookedDumplingCookstoveRecipe::cookTime)
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
		ICookstoveDisplay display = ICookstoveDisplay.fromNetwork(buf);
		int cookTime = buf.readVarInt();
		return this.factory.create(fluidStack, container, display, cookTime);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, CookedDumplingCookstoveRecipe recipe) {
		recipe.fluidStack().writeToPacket(buf);
		recipe.container().toNetwork(buf);
		recipe.display().toNetwork(buf);
		buf.writeVarInt(recipe.cookTime());
	}

	public interface Creator<T extends CookedDumplingCookstoveRecipe> {
		T create(FluidStack fluidStack, Ingredient container, ICookstoveDisplay display, int cookTime);
	}
}
