package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.WombatEntity;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class WombatModel<T extends WombatEntity> extends QuadrupedModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "wombat"), "main");

	public WombatModel(ModelPart root) {
		super(root, true, 5.0F, 2.0F, 2.0F, 2.0F, 24);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 1.0F));

		body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 10).addBox(-5.0F, -10.0F, -3.0F, 10.0F, 18.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 6.0F, 4.0F)
				.texOffs(24, 0).addBox(-3.0F, -0.5F, -4.75F, 6.0F, 5.0F, 4.0F, new CubeDeformation(-1.25F)), PartPose.offset(0.0F, -2.0F, -12.0F));

		head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(20, 0).addBox(-3.5F, -3.25F, -1.25F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, -0.2618F));
		head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(20, 0).addBox(1.5F, -3.25F, -1.25F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.2618F));

		body.addOrReplaceChild("backLegL", CubeListBuilder.create().texOffs(0, 10).addBox(0.8F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(1.1F, 1.0F, 6.0F));
		body.addOrReplaceChild("backLegR", CubeListBuilder.create().texOffs(0, 10).addBox(-2.8F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F).mirror(), PartPose.offset(-1.1F, 1.0F, 6.0F));
		body.addOrReplaceChild("frontLegL", CubeListBuilder.create().texOffs(0, 10).addBox(0.7F, 3.8F, -2.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(1.2F, -3.0F, -5.0F));
		body.addOrReplaceChild("frontLegR", CubeListBuilder.create().texOffs(0, 10).addBox(-2.7F, 3.8F, -2.0F, 2.0F, 6.0F, 2.0F).mirror(), PartPose.offset(-1.2F, -3.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}
}