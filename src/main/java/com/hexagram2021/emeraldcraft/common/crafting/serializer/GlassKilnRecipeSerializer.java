package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.GlassKilnRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class GlassKilnRecipeSerializer<T extends GlassKilnRecipe> implements RecipeSerializer<T> {
	private final GlassKilnRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public GlassKilnRecipeSerializer(GlassKilnRecipeSerializer.Creator<T> creator, int defaultCookingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(GlassKilnRecipe::getGroup),
						ExtraCodecs.strictOptionalField(Codec.STRING, "category", "").forGetter(GlassKilnRecipe::getCategory),
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(GlassKilnRecipe::getIngredient),
						ForgeRegistries.ITEMS.getCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(GlassKilnRecipe::getResult),
						Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(GlassKilnRecipe::getExperience),
						Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(GlassKilnRecipe::getCookingTime)
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
		ItemStack itemstack = buf.readItem();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.factory.create(group, category, ingredient, itemstack, xp, time);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeUtf(recipe.getGroup());
		buf.writeUtf(recipe.getCategory());
		recipe.getIngredient().toNetwork(buf);
		buf.writeItem(recipe.getResult());
		buf.writeFloat(recipe.getExperience());
		buf.writeVarInt(recipe.getCookingTime());
	}

	public interface Creator<T extends GlassKilnRecipe> {
		T create(String group, String category, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
