package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class MantaModel<T extends MantaEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "manta"), "main");
	private final ModelPart root;
	private final ModelPart leftWingBase;
	private final ModelPart leftWingTip;
	private final ModelPart rightWingBase;
	private final ModelPart rightWingTip;
	private final ModelPart tailBase;
	private final ModelPart tailTip;

	public MantaModel(ModelPart root) {
		this.root = root;
		ModelPart modelpart = root.getChild("body");
		this.tailBase = modelpart.getChild("tail_base");
		this.tailTip = this.tailBase.getChild("tail_tip");
		this.leftWingBase = modelpart.getChild("left_wing_base");
		this.leftWingTip = this.leftWingBase.getChild("left_wing_tip");
		this.rightWingBase = modelpart.getChild("right_wing_base");
		this.rightWingTip = this.rightWingBase.getChild("right_wing_tip");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 13.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.1F, 0.0F, 0.0F));
		PartDefinition tail = body.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 6.0F), PartPose.offset(0.0F, -2.0F, 5.0F));
		tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F), PartPose.offset(0.0F, 0.5F, 6.0F));
		PartDefinition left_wing = body.addOrReplaceChild("left_wing_base", CubeListBuilder.create().texOffs(24, 12).addBox(0.0F, 0.0F, 0.0F, 7.0F, 2.0F, 13.0F), PartPose.offsetAndRotation(2.0F, -2.0F, -8.0F, 0.0F, 0.0F, 0.1F));
		left_wing.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(12, 27).addBox(0.0F, 0.0F, 0.0F, 13.0F, 1.0F, 13.0F), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F));
		PartDefinition right_wing = body.addOrReplaceChild("right_wing_base", CubeListBuilder.create().texOffs(24, 12).mirror().addBox(-7.0F, 0.0F, 0.0F, 7.0F, 2.0F, 13.0F), PartPose.offsetAndRotation(-3.0F, -2.0F, -8.0F, 0.0F, 0.0F, -0.1F));
		right_wing.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(12, 27).mirror().addBox(-14.0F, 0.0F, 0.0F, 13.0F, 1.0F, 13.0F), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1F));
		body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 5.0F, 3.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 1.0F, -7.0F, 0.2F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = ((float)entity.getUniqueFlapTickOffset() + ageInTicks) * 7.448451F * ((float)Math.PI / 180F);
		float f1 = 16.0F;
		this.leftWingBase.zRot = Mth.cos(f) * f1 * ((float)Math.PI / 180F);
		this.leftWingTip.zRot = Mth.cos(f) * f1 * ((float)Math.PI / 180F);
		this.rightWingBase.zRot = -this.leftWingBase.zRot;
		this.rightWingTip.zRot = -this.leftWingTip.zRot;
		this.tailBase.xRot = -(5.0F + Mth.cos(f * 2.0F) * 5.0F) * ((float)Math.PI / 180F);
		this.tailTip.xRot = -(5.0F + Mth.cos(f * 2.0F) * 5.0F) * ((float)Math.PI / 180F);
	}
}