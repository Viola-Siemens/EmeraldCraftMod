package com.hexagram2021.emeraldcraft.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class WraithModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "wraith"), "main");
	private final ModelPart heart;
	private final ModelPart intestine;

	public WraithModel(ModelPart root) {
		super(root);
		ModelPart body = root.getChild("body");
		this.heart = body.getChild("heart");
		this.intestine = body.getChild("intestine");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F))
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 25).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
		head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -1.5F, -4.0F, 7.0F, 1.0F, 8.0F), PartPose.rotation(0.3054F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 43).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
				.texOffs(32, 6).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F))
				.texOffs(24, 0).addBox(-4.0F, 6.0F, -1.0F, 4.0F, 2.0F, 2.0F)
				.texOffs(24, 0).mirror().addBox(0.0F, 8.0F, -1.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, -4.0F, 0.0F));
		body.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 1.0F, -1.0F, 4.0F, 4.0F, 2.0F), PartPose.ZERO);
		body.addOrReplaceChild("intestine", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 9.0F, -1.0F, 0.0F, -0.523599F, -1.570796F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(-0.5F)), PartPose.offset(-4.0F, -2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 40).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(4.0F, -2.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 40).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(-0.5F)), PartPose.offset(-2.0F, 8.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 40).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(2.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		float t1 = ageInTicks / 8.0F;
		this.heart.z = Mth.sin(t1 + Mth.PI / 4.0F) / 4.0F + Mth.sin(2.0F * t1) / 7.0F + Mth.sin(5.0F * t1) / 31.0F - Mth.sin(7.0F * t1) / 17.0F - 1.0F;

		float t2 = ageInTicks / 5.5F;
		this.intestine.zRot = Mth.sin(t2) * 0.5F - Mth.PI / 2.0F;
	}
}