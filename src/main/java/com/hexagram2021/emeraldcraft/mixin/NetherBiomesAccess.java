package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(MultiNoiseBiomeSource.Preset.class)
public interface NetherBiomesAccess {
	@Accessor
	@Mutable
	static void setNETHER(MultiNoiseBiomeSource.Preset NETHER) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}
}
