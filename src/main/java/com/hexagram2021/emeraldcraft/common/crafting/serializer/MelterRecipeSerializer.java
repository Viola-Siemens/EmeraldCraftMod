package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.MelterRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class MelterRecipeSerializer<T extends MelterRecipe> implements RecipeSerializer<T> {
	private final MelterRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public MelterRecipeSerializer(MelterRecipeSerializer.Creator<T> creator, int defaultMeltingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(MelterRecipe::group),
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MelterRecipe::ingredient),
						FluidStack.CODEC.fieldOf("result").forGetter(MelterRecipe::resultFluid),
						Codec.INT.fieldOf("meltingtime").orElse(defaultMeltingTime).forGetter(MelterRecipe::meltingTime)
				).apply(instance, creator::create)
		);
	}

	@Override
	public Codec<T> codec() {
		return this.codec;
	}

	@Override @Nullable
	public T fromNetwork(FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		FluidStack result = FluidStack.readFromPacket(buf);
		int time = buf.readVarInt();
		return this.factory.create(group, ingredient, result, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.group());
		recipe.getIngredient().toNetwork(buf);
		recipe.resultFluid().writeToPacket(buf);
		buf.writeVarInt(recipe.meltingTime());
	}

	public interface Creator<T extends Recipe<Container>> {
		T create(String group, Ingredient ingredient, FluidStack resultFluid, int meltingTime);
	}
}
