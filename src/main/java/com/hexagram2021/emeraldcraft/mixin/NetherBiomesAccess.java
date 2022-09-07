package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.world.biome.provider.NetherBiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetherBiomeProvider.Preset.class)
public interface NetherBiomesAccess {
	@Accessor
	@Mutable
	static void setNETHER(NetherBiomeProvider.Preset NETHER) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}
}