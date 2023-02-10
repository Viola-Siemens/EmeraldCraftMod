package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.LumineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class LumineModel extends HierarchicalModel<LumineEntity> implements ArmedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "lumine"), "main");

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_wing;
	private final ModelPart left_wing;
	private static final float FLYING_ANIMATION_X_ROT = 0.6981317F;
	private static final float DEFAULT_WING_X_ROT = 0.43633232F;
	private static final float DEFAULT_WING_Y_ROT = 0.61086524F;
	private static final float DEFAULT_ARM_Y_ROT = 0.27925268F;

	public LumineModel(ModelPart p_233312_) {
		this.root = p_233312_.getChild("root");
		this.head = this.root.getChild("head");
		this.body = this.root.getChild("body");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		this.right_wing = this.body.getChild("right_wing");
		this.left_wing = this.body.getChild("left_wing");
	}

	@Override @NotNull
	public ModelPart root() {
		return this.root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition rootPart = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 0.0F));
		rootPart.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -3.99F, 0.0F));
		PartDefinition bodyPart = rootPart.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, -4.0F, 0.0F));
		bodyPart.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(-1.75F, 0.5F, 0.0F));
		bodyPart.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -0.5F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(1.75F, 0.5F, 0.0F));
		bodyPart.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.65F));
		bodyPart.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 1.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.65F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(LumineEntity lumine, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		float f = ageInTicks * 20.0F * (Mth.PI / 180F) + limbSwingAmount;
		float f1 = Mth.cos(f) * Mth.PI * 0.15F;
		float f2 = ageInTicks - lumine.tickCount;
		float f3 = ageInTicks * 9.0F * (Mth.PI / 180F);
		float f4 = Math.min(limbSwingAmount / 0.3F, 1.0F);
		float f5 = 1.0F - f4;
		float f6 = lumine.getHoldingItemAnimationProgress(f2);
		if (lumine.isDancing()) {
			float f7 = ageInTicks * 8.0F * (Mth.PI / 180F) + limbSwingAmount;
			float f8 = Mth.cos(f7) * 16.0F * (Mth.PI / 180F);
			float f9 = lumine.getSpinningProgress(f2);
			float f10 = Mth.cos(f7) * 14.0F * (Mth.PI / 180F);
			float f11 = Mth.cos(f7) * 30.0F * (Mth.PI / 180F);
			this.root.yRot = lumine.isSpinning() ? 12.566371F * f9 : this.root.yRot;
			this.root.zRot = f8 * (1.0F - f9);
			this.head.yRot = f11 * (1.0F - f9);
			this.head.zRot = f10 * (1.0F - f9);
		} else {
			this.head.xRot = headPitch * (Mth.PI / 180F);
			this.head.yRot = netHeadYaw * (Mth.PI / 180F);
		}

		this.right_wing.xRot = DEFAULT_WING_X_ROT;
		this.right_wing.yRot = -DEFAULT_WING_Y_ROT + f1;
		this.left_wing.xRot = DEFAULT_WING_X_ROT;
		this.left_wing.yRot = DEFAULT_WING_Y_ROT - f1;
		float f12 = f4 * FLYING_ANIMATION_X_ROT;
		this.body.xRot = f12;
		float f13 = Mth.lerp(f6, f12, Mth.lerp(f4, (-Mth.PI / 3F), (-Mth.PI / 4F)));
		this.root.y += Mth.cos(f3) * 0.25F * f5;
		this.right_arm.xRot = f13;
		this.left_arm.xRot = f13;
		float f14 = f5 * (1.0F - f6);
		float f15 = DEFAULT_WING_X_ROT - Mth.cos(f3 + (Mth.PI * 1.5F)) * Mth.PI * 0.075F * f14;
		this.left_arm.zRot = -f15;
		this.right_arm.zRot = f15;
		this.right_arm.yRot = DEFAULT_ARM_Y_ROT * f6;
		this.left_arm.yRot = -DEFAULT_ARM_Y_ROT * f6;
	}

	@Override
	public void translateToHand(@NotNull HumanoidArm arm, @NotNull PoseStack transform) {
		this.root.translateAndRotate(transform);
		this.body.translateAndRotate(transform);
		transform.translate(0.0D, -0.09375D, 0.09375D);
		transform.mulPose(Vector3f.XP.rotation(this.right_arm.xRot + DEFAULT_WING_X_ROT));
		transform.scale(0.7F, 0.7F, 0.7F);
		transform.translate(0.0625D, 0.0D, 0.0D);
	}
}
