package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherPigmanEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherPigmanModel<T extends NetherPigmanEntity> extends PlayerModel<T> {
	public NetherPigmanModel(float root) {
		super(root, false);
	}
}
