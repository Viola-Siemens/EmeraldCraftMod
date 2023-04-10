package com.hexagram2021.emeraldcraft.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public interface StructureTemplatePoolAccess {
	@Accessor
	List<Pair<StructurePoolElement, Integer>> getRawTemplates();
	@Accessor
	void setRawTemplates(List<Pair<StructurePoolElement, Integer>> value);

	@Accessor
	ObjectArrayList<StructurePoolElement> getTemplates();
}