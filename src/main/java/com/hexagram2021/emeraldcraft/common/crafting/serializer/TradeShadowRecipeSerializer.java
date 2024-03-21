package com.hexagram2021.emeraldcraft.common.crafting.serializer;

import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class TradeShadowRecipeSerializer<T extends TradeShadowRecipe> implements RecipeSerializer<T> {
	private static final MapCodec<ItemStack> ITEM_STACK_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(ItemStack::getItem),
					Codec.INT.fieldOf("count").forGetter(ItemStack::getCount)
			).apply(instance, ItemStack::new)
	);

	private final TradeShadowRecipeSerializer.Creator<T> factory;
	private final Codec<T> codec;

	public TradeShadowRecipeSerializer(TradeShadowRecipeSerializer.Creator<T> creator) {
		this.factory = creator;
		this.codec = RecordCodecBuilder.create(
				instance -> instance.group(
						ITEM_STACK_CODEC.fieldOf("costA").forGetter(TradeShadowRecipe::costA),
						ITEM_STACK_CODEC.fieldOf("costB").forGetter(TradeShadowRecipe::costB),
						ITEM_STACK_CODEC.fieldOf("result").forGetter(TradeShadowRecipe::result),
						ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entityType").forGetter(TradeShadowRecipe::entityType),
						ForgeRegistries.VILLAGER_PROFESSIONS.getCodec().fieldOf("profession")
								.orElse(VillagerProfession.NONE).forGetter(TradeShadowRecipe::profession),
						Codec.INT.fieldOf("villagerLevel").forGetter(TradeShadowRecipe::villagerLevel),
						Codec.INT.fieldOf("xp").forGetter(TradeShadowRecipe::xp)
				).apply(instance, creator::create)
		);
	}

	@Override
	public Codec<T> codec() {
		return this.codec;
	}

	@Override @Nullable
	public T fromNetwork(FriendlyByteBuf buf) {
		ItemStack costA = buf.readItem();
		ItemStack costB = buf.readItem();
		ItemStack result = buf.readItem();
		EntityType<?> entityType = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getValue(buf.readResourceLocation()));
		VillagerProfession profession = Objects.requireNonNullElse(ForgeRegistries.VILLAGER_PROFESSIONS.getValue(buf.readResourceLocation()), VillagerProfession.NONE);
		int villagerLevel = buf.readVarInt();
		int xp = buf.readVarInt();

		return this.factory.create(costA, costB, result, entityType, profession, villagerLevel, xp);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeItem(recipe.costA());
		buf.writeItem(recipe.costB());
		buf.writeItem(recipe.result());
		buf.writeResourceLocation(getRegistryName(recipe.entityType()));
		VillagerProfession profession = recipe.profession();
		buf.writeResourceLocation(getRegistryName(Objects.requireNonNullElse(profession, VillagerProfession.NONE)));
		buf.writeVarInt(recipe.villagerLevel());
		buf.writeVarInt(recipe.xp());
	}


	public interface Creator<T extends TradeShadowRecipe> {
		T create(ItemStack costA, ItemStack costB, ItemStack result, EntityType<?> entityType, VillagerProfession profession, int villagerLevel, int xp);
	}
}
