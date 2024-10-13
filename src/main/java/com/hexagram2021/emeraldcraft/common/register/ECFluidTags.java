package com.hexagram2021.emeraldcraft.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class ECFluidTags {
	public static final TagKey<Fluid> RESIN = create("resin");
	public static final TagKey<Fluid> MELTED_EMERALD = create("melted_emerald");
	public static final TagKey<Fluid> MELTED_IRON = create("melted_iron");
	public static final TagKey<Fluid> MELTED_GOLD = create("melted_gold");
	public static final TagKey<Fluid> MELTED_COPPER = create("melted_copper");
	public static final TagKey<Fluid> MELTED_ZINC = create("melted_zinc");
	public static final TagKey<Fluid> MELTED_ALUMINUM = create("melted_aluminum");
	public static final TagKey<Fluid> MELTED_LEAD = create("melted_lead");
	public static final TagKey<Fluid> MELTED_SILVER = create("melted_silver");
	public static final TagKey<Fluid> MELTED_NICKEL = create("melted_nickel");
	public static final TagKey<Fluid> MELTED_URANIUM = create("melted_uranium");

	private ECFluidTags() {
	}

	private static TagKey<Fluid> create(String name) {
		return TagKey.create(Registries.FLUID, new ResourceLocation(MODID, name));
	}
}
