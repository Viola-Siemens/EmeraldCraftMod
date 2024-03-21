package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.RabbleFurnaceRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class RabbleFurnaceRecipeSerializer<T extends RabbleFurnaceRecipe> implements RecipeSerializer<T> {
	private final RabbleFurnaceRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public RabbleFurnaceRecipeSerializer(RabbleFurnaceRecipeSerializer.Creator<T> creator, int defaultCookingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(RabbleFurnaceRecipe::getGroup),
						ExtraCodecs.strictOptionalField(Codec.STRING, "category", "").forGetter(RabbleFurnaceRecipe::category),
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(RabbleFurnaceRecipe::ingredient),
						Ingredient.CODEC.fieldOf("mix1").forGetter(RabbleFurnaceRecipe::mix1),
						Ingredient.CODEC.fieldOf("mix2").forGetter(RabbleFurnaceRecipe::mix2),
						ForgeRegistries.ITEMS.getCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(RabbleFurnaceRecipe::result),
						Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(RabbleFurnaceRecipe::experience),
						Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(RabbleFurnaceRecipe::rabblingTime)
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
		String category = buf.readUtf();
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		Ingredient mix1 = Ingredient.fromNetwork(buf);
		Ingredient mix2 = Ingredient.fromNetwork(buf);
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(group, category, ingredient, mix1, mix2, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeUtf(recipe.category());
		recipe.ingredient().toNetwork(buf);
		recipe.mix1().toNetwork(buf);
		recipe.mix2().toNetwork(buf);
		buf.writeItem(recipe.result());
		buf.writeFloat(recipe.experience());
		buf.writeVarInt(recipe.rabblingTime());
	}

	public interface Creator<T extends Recipe<?>> {
		T create(String group, String category, Ingredient ingredient, Ingredient mix1, Ingredient mix2,
				 ItemStack result, float experience, int cookingTime);
	}
}
