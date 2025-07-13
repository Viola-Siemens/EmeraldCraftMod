package com.hexagram2021.emeraldcraft.common.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class CodecUtil {
    private static final Codec<Item> ITEM_NON_AIR_CODEC = ExtraCodecs.validate(
            BuiltInRegistries.ITEM.byNameCodec(), var0 -> var0 == Items.AIR ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(var0)
    );

    private static final Codec<Ingredient.ItemValue> INGREDIENT_ITEM_VALUE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(ITEM_NON_AIR_CODEC.xmap(ItemStack::new, ItemStack::getItem).fieldOf("item").forGetter(var0x -> var0x.item)).apply(instance, Ingredient.ItemValue::new)
    );
    private static final Codec<Ingredient.TagValue> INGREDIENT_TAG_VALUE_CODEC = RecordCodecBuilder.create(
            var0 -> var0.group(TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(var0x -> var0x.tag)).apply(var0, Ingredient.TagValue::new)
    );


    private static final Codec<Ingredient.Value> INGREDIENT_VALUE_CODEC = ExtraCodecs.xor(INGREDIENT_ITEM_VALUE_CODEC, INGREDIENT_TAG_VALUE_CODEC)
            .xmap(either -> either.map(Function.identity(), Function.identity()), value -> {
                if (value instanceof Ingredient.TagValue tagValue) {
                    return Either.right(tagValue);
                } else if (value instanceof Ingredient.ItemValue itemValue) {
                    return Either.left(itemValue);
                } else {
                    throw new UnsupportedOperationException("This is neither an item value nor a tag value.");
                }
            });

    public static final Codec<Ingredient> INGREDIENT_CODEC = new ExtraCodecs.EitherCodec<>(
            Codec.list(INGREDIENT_VALUE_CODEC)
                    .comapFlatMap(
                            values -> values.isEmpty() ?
                                    DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") :
                                    DataResult.success(values.toArray(new Ingredient.Value[0])),
                            List::of
                    ), INGREDIENT_VALUE_CODEC
    ).flatComapMap(
            either -> either.map(values -> Ingredient.fromValues(Stream.of(values)), value -> Ingredient.fromValues(Stream.of(value))),
            var1x -> {
                if (var1x.values.length == 1) {
                    return DataResult.success(Either.right(var1x.values[0]));
                } else {
                    return var1x.values.length == 0 ?
                            DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") :
                            DataResult.success(Either.left(var1x.values));
                }
            }
    );

    private CodecUtil() {
    }
}
