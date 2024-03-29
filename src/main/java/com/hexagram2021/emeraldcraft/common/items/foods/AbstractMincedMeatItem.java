package com.hexagram2021.emeraldcraft.common.items.foods;

import net.minecraft.world.item.Item;

public abstract class AbstractMincedMeatItem extends Item {
	public AbstractMincedMeatItem(Properties properties) {
		super(properties);
	}

	public abstract Item rawMeatItem();
	public abstract Item cookedMeatItem();
}
