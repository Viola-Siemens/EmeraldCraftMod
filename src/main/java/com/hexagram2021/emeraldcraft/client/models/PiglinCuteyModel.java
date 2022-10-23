package com.hexagram2021.emeraldcraft.client.models;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class PiglinCuteyModel<T extends PiglinCuteyEntity> extends SegmentedModel<T> implements IHasArm, IHasHead {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer leftEar;
	private final ModelRenderer rightEar;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;

	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;

	public PiglinCuteyModel(float root) {
		this.texHeight = this.texWidth = 64;

		this.body = new ModelRenderer(this);
		this.body.texOffs(16, 16).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, root + 0.0F);
		this.body.texOffs(16, 32).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 12.0F, 4.0F, root + 0.25F);

		this.head = new ModelRenderer(this);
		this.head.texOffs(0, 0).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 8.0F, 8.0F, root + 0.24F);
		this.head.texOffs(29, 1).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 4.0F, 1.0F, root + 0.0F);
		this.head.texOffs(2, 4).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, root + 0.0F);
		this.head.texOffs(2, 0).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, root + 0.0F);

		this.leftEar = new ModelRenderer(this);
		this.leftEar.setPos(5.0F, -6.0F, 0.0F);
		setRotationAngle(this.leftEar, 0.0F, 0.0F, -0.3491F);
		this.leftEar.texOffs(51, 6).addBox(-0.5F, 1.0F, -2.0F, 1.0F, 5.0F, 4.0F, root + 0.0F, false);
		this.head.addChild(this.leftEar);

		this.rightEar = new ModelRenderer(this);
		this.rightEar.setPos(-5.0F, -6.0F, 0.0F);
		setRotationAngle(this.rightEar, 0.0F, 0.0F, 0.3491F);
		this.rightEar.texOffs(39, 6).addBox(-0.5F, 1.0F, -2.0F, 1.0F, 5.0F, 4.0F, root + 0.0F, false);
		this.head.addChild(this.rightEar);

		this.leftArm = new ModelRenderer(this);
		this.leftArm.setPos(5.0F, 2.0F, 0.0F);
		this.leftArm.texOffs(34, 48).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 10.0F, 3.0F, root + 0.0F, false);

		this.rightArm = new ModelRenderer(this);
		this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
		this.rightArm.texOffs(42, 16).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 10.0F, 3.0F, root + 0.0F, false);

		this.leftLeg = new ModelRenderer(this);
		this.leftLeg.setPos(1.9F, 12.0F, 0.0F);
		this.leftLeg.texOffs(18, 48).addBox(-1.4F, 1.0F, -2.0F, 3.0F, 11.0F, 3.0F, root + 0.0F, false);

		this.rightLeg = new ModelRenderer(this);
		this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);
		this.rightLeg.texOffs(2, 16).addBox(-1.6F, 1.0F, -2.0F, 3.0F, 11.0F, 3.0F, root + 0.0F, false);
	}

	public static void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);

		this.body.yRot = 0.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;

		this.leftEar.zRot = -0.3491F;
		this.rightEar.zRot = 0.3491F;

		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.zRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		if (this.riding) {
			this.leftArm.xRot += (-(float)Math.PI / 5F);
			this.rightArm.xRot += (-(float)Math.PI / 5F);
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
		}

		this.leftArm.yRot = 0.0F;
		this.rightArm.yRot = 0.0F;

		boolean rightHanded = entity.getMainArm() == HandSide.RIGHT;
		if (entity.isUsingItem()) {
			boolean mainHandUseItem = entity.getUsedItemHand() == Hand.MAIN_HAND;
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
		this.leftLeg.z = 0.1F;
		this.rightLeg.z = 0.1F;
		this.leftLeg.y = 12.0F;
		this.rightLeg.y = 12.0F;
		this.head.y = 0.0F;
		this.body.y = 0.0F;
		this.leftArm.y = 2.0F;
		this.rightArm.y = 2.0F;
	}

	public void copyPropertiesTo(PiglinCuteyModel<T> model) {
		super.copyPropertiesTo(model);
		model.leftArmPose = this.leftArmPose;
		model.rightArmPose = this.rightArmPose;
		model.head.copyFrom(this.head);
		model.body.copyFrom(this.body);
		model.leftArm.copyFrom(this.leftArm);
		model.rightArm.copyFrom(this.rightArm);
		model.leftLeg.copyFrom(this.leftLeg);
		model.rightLeg.copyFrom(this.rightLeg);
	}

	private void poseRightArm(T entity) {
		switch(this.rightArmPose) {
			case EMPTY:
				this.rightArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
				this.rightArm.yRot = (-(float)Math.PI / 6F);
				break;
			case ITEM:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.rightArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
				this.rightArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.rightArm.yRot = -0.1F + this.head.yRot;
				this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
				break;
		}

	}

	private void poseLeftArm(T entity) {
		switch(this.leftArmPose) {
			case EMPTY:
				this.leftArm.yRot = 0.0F;
				break;
			case BLOCK:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
				this.leftArm.yRot = ((float)Math.PI / 6F);
				break;
			case ITEM:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float)Math.PI / 10F);
				this.leftArm.yRot = 0.0F;
				break;
			case THROW_SPEAR:
				this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
				this.leftArm.yRot = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
				this.leftArm.yRot = 0.1F + this.head.yRot;
				this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entity, false);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
				break;
		}

	}

	protected ModelRenderer getArm(HandSide pSide) {
		return pSide == HandSide.LEFT ? this.leftArm : this.rightArm;
	}

	private HandSide getAttackArm(T pEntity) {
		HandSide humanoidArm = pEntity.getMainArm();
		return pEntity.swingingArm == Hand.MAIN_HAND ? humanoidArm : humanoidArm.getOpposite();
	}

	@Override
	public void translateToHand(@Nonnull HandSide side, @Nonnull MatrixStack matrixStack) {
		this.getArm(side).translateAndRotate(matrixStack);
	}

	@Override @Nonnull
	public ModelRenderer getHead() {
		return this.head;
	}

	@Override @Nonnull
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}
}