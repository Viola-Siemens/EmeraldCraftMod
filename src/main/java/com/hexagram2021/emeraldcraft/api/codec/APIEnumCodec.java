package com.hexagram2021.emeraldcraft.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record APIEnumCodec<E extends StringRepresentable>(Codec<E> codec, Function<String, E> resolver) implements Codec<E> {
	public APIEnumCodec(E[] array, Map<String, Integer> idGetter, Function<String, E> resolver) {
		this(ExtraCodecs.orCompressed(
				ExtraCodecs.stringResolverCodec(StringRepresentable::getSerializedName, resolver),
				ExtraCodecs.idResolverCodec((entry) -> idGetter.get(entry.toString()), (id) -> id >= 0 && id < array.length ? array[id] : null, -1)
		), resolver);
	}

	@Override
	public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
		return this.codec.decode(ops, input);
	}

	@Override
	public <T> DataResult<T> encode(E entry, DynamicOps<T> ops, T prefix) {
		return this.codec.encode(entry, ops, prefix);
	}

	@Nullable
	public E byName(@Nullable String name) {
		return this.resolver.apply(name);
	}

	public static <T extends StringRepresentable> APIEnumCodec<T> instance(Supplier<T[]> aGetter, Map<String, Integer> idGetter) {
		T[] values = aGetter.get();
		Map<String, T> map = Arrays.stream(values).collect(Collectors.toMap(StringRepresentable::getSerializedName, (entry) -> entry));
		return new APIEnumCodec<>(values, idGetter, (name) -> name == null ? null : map.get(name));
	}
}
