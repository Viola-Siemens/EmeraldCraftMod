// Made with Blockbench 4.1.4
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class PiglinCuteyModel<T extends PiglinCuteyEntity> extends HierarchicalModel<T> implements ArmedModel, HeadedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "piglin_cutey"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart LeftEar;
	private final ModelPart RightEar;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;

	public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
	public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;

	public PiglinCuteyModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.LeftEar = this.head.getChild("left_ear");
		this.RightEar = this.head.getChild("right_ear");
		this.LeftArm = root.getChild("left_arm");
		this.RightArm = root.getChild("right_arm");
		this.LeftLeg = root.getChild("left_leg");
		this.RightLeg = root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bodyDef = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 32).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headDef = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.24F))
				.texOffs(29, 1).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 4).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(2, 0).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftEar = headDef.addOrReplaceChild(
				"left_ear",
				CubeListBuilder.create().texOffs(51, 6)
						.addBox(-0.5F, 1.0F, -2.0F, 1.0F, 5.0F, 4.0F,
				new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.3491F)
		);
		PartDefinition rightEar = headDef.addOrReplaceChild(
				"right_ear",
				CubeListBuilder.create().texOffs(39, 6)
						.addBox(-0.5F, 1.0F, -2.0F, 1.0F, 5.0F, 4.0F,
				new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.3491F)
		);
		PartDefinition LeftArm = partdefinition.addOrReplaceChild(
				"left_arm", CubeListBuilder.create().texOffs(34, 48)
						.addBox(-1.0F, -1.0F, -2.0F, 3.0F, 10.0F, 3.0F,
								new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 2.0F, 0.0F)
		);
		PartDefinition RightArm = partdefinition.addOrReplaceChild(
				"right_arm",
				CubeListBuilder.create().texOffs(42, 16)
						.addBox(-2.0F, -1.0F, -2.0F, 3.0F, 10.0F, 3.0F,
				new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F)
		);
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild(
				"left_leg",
				CubeListBuilder.create().texOffs(18, 48)
						.addBox(-1.4F, 1.0F, -2.0F, 3.0F, 11.0F, 3.0F,
				new CubeDeformation(0.0F)),
				PartPose.offset(1.9F, 11.0F, 0.0F)
		);
		PartDefinition RightLeg = partdefinition.addOrReplaceChild(
				"right_leg",
				CubeListBuilder.create().texOffs(2, 16)
						.addBox(-1.6F, 1.0F, -2.0F, 3.0F, 11.0F, 3.0F,
								new CubeDeformation(0.0F)),
				PartPose.offset(-1.9F, 11.0F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);

		this.body.yRot = 0.0F;
		this.LeftArm.z = 0.0F;
		this.LeftArm.x = 5.0F;
		this.RightArm.z = 0.0F;
		this.RightArm.x = -5.0F;

		this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.LeftArm.zRot = 0.0F;
		this.RightArm.zRot = 0.0F;
		this.LeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.RightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.LeftLeg.yRot = 0.0F;
		this.RightLeg.yRot = 0.0F;
		this.LeftLeg.zRot = 0.0F;
		this.RightLeg.zRot = 0.0F;
		if (this.riding) {
			this.LeftArm.xRot += (-(float)Math.PI / 5F);
			this.RightArm.xRot += (-(float)Math.PI / 5F);
			this.LeftLeg.xRot = -1.4137167F;
			this.LeftLeg.yRot = (-(float)Math.PI / 10F);
			this.LeftLeg.zRot = -0.07853982F;
			this.RightLeg.xRot = -1.4137167F;
			this.RightLeg.yRot = ((float)Math.PI / 10F);
			this.RightLeg.zRot = 0.07853982F;
		}

		this.LeftArm.yRot = 0.0F;
		this.RightArm.yRot = 0.0F;

		boolean rightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
		if (entity.isUsingItem()) {
			boolean mainHandUseItem = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
			if (mainHandUseItem == rightHanded) {
				this.poseRightArm(entity);
			} else {
				this.poseLeftArm(entity);
			}
		} else {
			boolean oppositeTwoHanded = rightHanded ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
			if (rightHanded != oppositeTwoHanded) {
				this.poseLeftArm(entity);
				this.poseRightArm(entity);
			} else {
				this.poseRightArm(entity);
				this.poseLeftArm(entity);
			}
		}

		this.body.xRot = 0.0F;
		this.LeftLeg.z = 0.1F;
		this.RightLeg.z = 0.1F;
		this.LeftLeg.y = 12.0F;
		this.RightLeg.y = 12.0F;
		this.head.y = 0.0F;
		this.body.y = 0.0F;
		this.LeftArm.y = 2.0F;
		this.RightArm.y = 2.0F;

		if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
			AnimationUtils.bobModelPart(this.RightArm, ageInTicks, 1.0F);
		}

		if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
			AnimationUtils.bobModelPart(this.LeftArm, ageInTicks, -1.0F);
		}
	}

	public void copyPropertiesTo(PiglinCuteyModel<T> model) {
		super.copyPropertiesTo(model);
		model.leftArmPose = this.leftArmPose;
		model.rightArmPose = this.rightArmPose;
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.LeftArm.copyFrom(this.LeftArm);
		model.RightArm.copyFrom(this.RightArm);
		model.LeftLeg.copyFrom(this.LeftLeg);
		model.RightLeg.copyFrom(this.RightLeg);
	}

	private void poseRightArm(T entity) {
		switch(this.rightArmPose) {
			case EMPTY:
				this.RightArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.RightArm.xRot = this.RightArm.xRot * 0.5F - 0.9424779F;
				this.RightArm.yRot = (-(float)Math.PI / 6F);
				break;
			case ITEM:
				this.RightArm.xRot = this.RightArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.RightArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.RightArm.xRot = this.RightArm.xRot * 0.5F - (float)Math.PI;
				this.RightArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.RightArm.yRot = -0.1F + this.head.yRot;
				this.LeftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.RightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.LeftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				AnimationUtils.animateCrossbowCharge(this.RightArm, this.LeftArm, entity, true);
				break;
			case CROSSBOW_HOLD:
				AnimationUtils.animateCrossbowHold(this.RightArm, this.LeftArm, this.head, true);
				break;
			case SPYGLASS:
				this.RightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (entity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.RightArm.yRot = this.head.yRot - 0.2617994F;
		}

	}

	private void poseLeftArm(T entity) {
		switch(this.leftArmPose) {
			case EMPTY:
				this.LeftArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.LeftArm.xRot = this.LeftArm.xRot * 0.5F - 0.9424779F;
				this.LeftArm.yRot = ((float)Math.PI / 6F);
				break;
			case ITEM:
				this.LeftArm.xRot = this.LeftArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.LeftArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.LeftArm.xRot = this.LeftArm.xRot * 0.5F - (float)Math.PI;
				this.LeftArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.RightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.LeftArm.yRot = 0.1F + this.head.yRot;
				this.RightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.LeftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				AnimationUtils.animateCrossbowCharge(this.RightArm, this.LeftArm, entity, false);
				break;
			case CROSSBOW_HOLD:
				AnimationUtils.animateCrossbowHold(this.RightArm, this.LeftArm, this.head, false);
				break;
			case SPYGLASS:
				this.LeftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (entity.isCrouching() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.LeftArm.yRot = this.head.yRot + 0.2617994F;
		}

	}

	protected ModelPart getArm(HumanoidArm pSide) {
		return pSide == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
	}

	private HumanoidArm getAttackArm(T pEntity) {
		HumanoidArm humanoidarm = pEntity.getMainArm();
		return pEntity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void translateToHand(HumanoidArm side, PoseStack matrixStack) {
		this.getArm(side).translateAndRotate(matrixStack);
	}

	@Override
	public ModelPart getHead() {
		return this.head;
	}
}