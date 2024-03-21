package com.hexagram2021.emeraldcraft.common.crafting;

import net.minecraft.world.Container;

public interface IPartialMatchRecipe<C extends Container> {
	boolean matchesAllowEmpty(C container);
}
