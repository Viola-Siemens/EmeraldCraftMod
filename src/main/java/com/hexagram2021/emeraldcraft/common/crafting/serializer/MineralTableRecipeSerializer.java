package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.MineralTableRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class MineralTableRecipeSerializer<T extends MineralTableRecipe> implements RecipeSerializer<T> {
	private final MineralTableRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public MineralTableRecipeSerializer(MineralTableRecipeSerializer.Creator<T> creator, int defaultCookingTime) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(MineralTableRecipe::getGroup),
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MineralTableRecipe::getIngredient),
						ForgeRegistries.ITEMS.getCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(MineralTableRecipe::getResult),
						Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(MineralTableRecipe::getExperience),
						Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(MineralTableRecipe::getCookingTime)
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

	public interface Creator<T extends AbstractCookingRecipe> {
		T create(String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
	}
}
