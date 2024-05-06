package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.MeatGrinderRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class MeatGrinderRecipeSerializer<T extends MeatGrinderRecipe> implements RecipeSerializer<T> {
	private final MeatGrinderRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	private static final MapCodec<ItemStack> ITEM_STACK_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemStack::getItem),
					Codec.INT.fieldOf("count").forGetter(ItemStack::getCount)
			).apply(instance, ItemStack::new)
	);

	public MeatGrinderRecipeSerializer(MeatGrinderRecipeSerializer.Creator<T> creator, int defaultCookingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(MeatGrinderRecipe::getGroup),
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MeatGrinderRecipe::getIngredient),
						ITEM_STACK_CODEC.fieldOf("result").forGetter(MeatGrinderRecipe::getResult),
						Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(MeatGrinderRecipe::getExperience),
						Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(MeatGrinderRecipe::getCookingTime)
				).apply(instance, creator::create)
		);
	}

	@Override
	public Codec<T> codec() {
		return this.codec;
	}

	@Override
	public T fromNetwork(FriendlyByteBuf buf) {
		String group = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(group, ingredient, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResult());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends MeatGrinderRecipe> {
		T create(String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
