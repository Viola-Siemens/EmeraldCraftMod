package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.NetherLambmanEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class NetherLambmanModel<T extends NetherLambmanEntity> extends PlayerModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "nether_lambman"), "main");
	public NetherLambmanModel(ModelPart root) {
		super(root, false);
	}

	public static LayerDefinition createBodyLayer() {
		return LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.0F), false), 64, 64);
	}
}
