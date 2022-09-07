package com.hexagram2021.emeraldcraft.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.NetherBiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(NetherBiomeProvider.class)
public interface MultiNoiseBiomeSourceAccess {
	@Invoker("<init>")
	static NetherBiomeProvider construct(long v, List<Pair<Biome.Attributes, Supplier<Biome>>> list, Optional<Pair<Registry<Biome>, NetherBiomeProvider.Preset>> optional) {
		throw new UnsupportedOperationException("This will be replaced by Mixin");
	}
}
