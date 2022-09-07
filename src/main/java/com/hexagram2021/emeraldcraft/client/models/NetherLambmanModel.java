package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherLambmanModel<T extends NetherLambmanEntity> extends PlayerModel<T> {
	public NetherLambmanModel(float root) {
		super(root, false);
	}
}
