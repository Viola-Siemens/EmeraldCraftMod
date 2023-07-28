package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.util.ListAppendable;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(StructureSet.class)
public class StructureSetMixin implements ListAppendable<StructureSet.StructureSelectionEntry> {
	@Shadow @Final @Mutable
	private List<StructureSet.StructureSelectionEntry> structures;

	@Override
	public List<StructureSet.StructureSelectionEntry> append(StructureSet.StructureSelectionEntry entry) {
		return this.structures = ImmutableList.<StructureSet.StructureSelectionEntry>builder().addAll(this.structures).add(entry).build();
	}

	@Override
	public List<StructureSet.StructureSelectionEntry> appendAll(Iterator<StructureSet.StructureSelectionEntry> entries) {
		return this.structures = ImmutableList.<StructureSet.StructureSelectionEntry>builder().addAll(this.structures).addAll(entries).build();
	}

	@Override
	public List<StructureSet.StructureSelectionEntry> appendAll(Iterable<StructureSet.StructureSelectionEntry> entries) {
		return this.structures = ImmutableList.<StructureSet.StructureSelectionEntry>builder().addAll(this.structures).addAll(entries).build();
	}
}
