package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.api.fluid.FluidWithAmount;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class IceMakerRecipeSerializer<T extends IceMakerRecipe> implements RecipeSerializer<T> {
	private final IceMakerRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public IceMakerRecipeSerializer(IceMakerRecipeSerializer.Creator<T> creator, int defaultFreezingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(IceMakerRecipe::group),
						FluidWithAmount.CODEC.fieldOf("ingredient").forGetter(IceMakerRecipe::inputFluid),
						ForgeRegistries.ITEMS.getCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(IceMakerRecipe::result),
						Codec.INT.fieldOf("freezingtime").orElse(defaultFreezingTime).forGetter(IceMakerRecipe::freezingTime)
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
		FluidType fluidType = FluidTypes.getFluidTypeFromName(buf.readUtf());
		int fluidAmount = buf.readVarInt();
		ItemStack result = buf.readItem();
		int time = buf.readVarInt();
		return this.factory.create(group, new FluidWithAmount(fluidType, fluidAmount), result, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.group());
		FluidWithAmount inputFluid = recipe.inputFluid();
		buf.writeUtf(inputFluid.fluidType().toString());
		buf.writeVarInt(inputFluid.amount());
		buf.writeItem(recipe.result());
		buf.writeVarInt(recipe.freezingTime());
	}

	public interface Creator<T extends Recipe<Container>> {
		T create(String group, FluidWithAmount inputFluid, ItemStack result, int freezingTime);
	}
}
