package com.hexagram2021.emeraldcraft.mixin;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(TileEntityType.class)
public interface BlockEntityTypeAccess {
	@Accessor("validBlocks")
	Set<Block> ec_getValidBlocks();

	@Accessor("validBlocks")
	@Mutable
	void ec_setValidBlocks(Set<Block> blocks);
}
