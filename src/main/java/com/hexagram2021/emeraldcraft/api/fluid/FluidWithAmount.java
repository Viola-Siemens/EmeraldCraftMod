package com.hexagram2021.emeraldcraft.api.fluid;

import com.hexagram2021.emeraldcraft.api.codec.APIEnumCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FluidWithAmount(FluidType fluidType, int amount) {
	private static final int DEFAULT_AMOUNT = 100;

	public static MapCodec<FluidWithAmount> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					APIEnumCodec.instance(FluidType::values, FluidTypes.ALL_FLUID_TYPES_IDS).fieldOf("fluid").forGetter(FluidWithAmount::fluidType),
					Codec.INT.fieldOf("amount").orElse(DEFAULT_AMOUNT).forGetter(FluidWithAmount::amount)
			).apply(instance, FluidWithAmount::new)
	);
}
