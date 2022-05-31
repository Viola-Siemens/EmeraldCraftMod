package com.hexagram2021.emeraldcraft.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(MultiNoiseBiomeSource.class)
public interface MultiNoiseBiomeSourceAccess {
	@Invoker("<init>")
	static MultiNoiseBiomeSource construct(long v, List<Pair<Biome.ClimateParameters, Supplier<Biome>>> list, Optional<Pair<Registry<Biome>, MultiNoiseBiomeSource.Preset>> optional)
	{
		throw new UnsupportedOperationException("This will be replaced by Mixin");
	}
}
