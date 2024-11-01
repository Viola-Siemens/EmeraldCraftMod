package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface ICookstoveDisplayType {
	Map<ResourceLocation, ICookstoveDisplayType> COOKSTOVE_DISPLAY_TYPES = Maps.newHashMap();
	Map<ICookstoveDisplayType, ResourceLocation> COOKSTOVE_DISPLAY_IDS = Maps.newIdentityHashMap();

	static void registerCookstoveDisplayType(ResourceLocation id, ICookstoveDisplayType cookstoveDisplayType) {
		COOKSTOVE_DISPLAY_TYPES.put(id, cookstoveDisplayType);
		COOKSTOVE_DISPLAY_IDS.put(cookstoveDisplayType, id);
	}

	Codec<ICookstoveDisplayType> REGISTRY_CODEC = new Codec<>() {
		@Override
		public <R> DataResult<Pair<ICookstoveDisplayType, R>> decode(DynamicOps<R> ops, R input) {
			return ResourceLocation.CODEC.decode(ops, input).flatMap(pair -> {
				if (!COOKSTOVE_DISPLAY_TYPES.containsKey(pair.getFirst())) {
					return DataResult.error(() -> "Unexpected cookstove display type: %s".formatted(pair.getFirst()));
				}
				return DataResult.success(pair.mapFirst(COOKSTOVE_DISPLAY_TYPES::get));
			});
		}
		@Override
		public <R> DataResult<R> encode(ICookstoveDisplayType input, DynamicOps<R> ops, R prefix) {
			ResourceLocation id = COOKSTOVE_DISPLAY_IDS.get(input);
			if (id == null) {
				return DataResult.error(() -> "Unknown cookstove display type: %s".formatted(input));
			}
			R key = ops.createString(id.toString());
			return ops.mergeToPrimitive(prefix, key);
		}
	};

	Codec<? extends ICookstoveDisplay> codec();
	ICookstoveDisplay fromNetwork(FriendlyByteBuf buf);

	static ResourceLocation getId(ICookstoveDisplayType type) {
		return COOKSTOVE_DISPLAY_IDS.get(type);
	}
	static ICookstoveDisplayType getType(ResourceLocation id) {
		return COOKSTOVE_DISPLAY_TYPES.get(id);
	}
}
