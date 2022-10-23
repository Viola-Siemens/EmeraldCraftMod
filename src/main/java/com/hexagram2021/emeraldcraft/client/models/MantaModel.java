package com.hexagram2021.emeraldcraft.client.models;

import com.google.common.collect.ImmutableList;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class MantaModel<T extends MantaEntity> extends SegmentedModel<T> implements IHasHead {
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer leftWingBase;
	private final ModelRenderer leftWingTip;
	private final ModelRenderer rightWingBase;
	private final ModelRenderer rightWingTip;
	private final ModelRenderer tailBase;
	private final ModelRenderer tailTip;

	public MantaModel(float root) {
		this.texHeight = this.texWidth = 64;
		
		this.body = new ModelRenderer(this);
		this.body.texOffs(0, 8).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 13.0F, root);
		this.body.setPos(0.0F, 16.0F, 0.0F);
		setRotationAngle(this.body, -0.1F, 0.0F, 0.0F);
		
		this.tailBase = new ModelRenderer(this);
		this.tailBase.texOffs(0, 24).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 6.0F, root);
		this.tailBase.setPos(0.0F, -2.0F, 5.0F);
		this.body.addChild(this.tailBase);
		
		this.tailTip = new ModelRenderer(this);
		this.tailTip.texOffs(0, 32).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 6.0F, root);
		this.tailTip.setPos(0.0F, 0.5F, 6.0F);
		this.tailBase.addChild(this.tailTip);
		
		this.leftWingBase = new ModelRenderer(this);
		this.leftWingBase.texOffs(24, 12).addBox(0.0F, 0.0F, 0.0F, 7.0F, 2.0F, 13.0F, root);
		this.leftWingBase.setPos(2.0F, -2.0F, -8.0F);
		setRotationAngle(this.leftWingBase, 0.0F, 0.0F, 0.1F);
		this.body.addChild(this.leftWingBase);
		
		this.leftWingTip = new ModelRenderer(this);
		this.leftWingTip.texOffs(12, 27).addBox(0.0F, 0.0F, 0.0F, 13.0F, 1.0F, 13.0F, root);
		this.leftWingTip.setPos(6.0F, 0.0F, 0.0F);
		setRotationAngle(this.leftWingTip, 0.0F, 0.0F, 0.1F);
		this.leftWingBase.addChild(this.leftWingTip);
		
		this.rightWingBase = new ModelRenderer(this);
		this.rightWingBase.mirror = true;
		this.rightWingBase.texOffs(24, 12).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 2.0F, 13.0F, root);
		this.rightWingBase.setPos(-3.0F, -2.0F, -8.0F);
		setRotationAngle(this.rightWingBase, 0.0F, 0.0F, -0.1F);
		this.body.addChild(this.rightWingBase);
		
		this.rightWingTip = new ModelRenderer(this);
		this.rightWingTip.mirror = true;
		this.rightWingTip.texOffs(12, 27).addBox(-14.0F, 0.0F, 0.0F, 13.0F, 1.0F, 13.0F, root);
		this.rightWingTip.setPos(-6.0F, 0.0F, 0.0F);
		setRotationAngle(this.rightWingTip, 0.0F, 0.0F, -0.1F);
		this.rightWingBase.addChild(this.rightWingTip);
		
		this.head = new ModelRenderer(this);
		this.head.texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 5.0F, 3.0F, 5.0F, root);
		this.head.setPos(0.0F, 1.0F, -7.0F);
		setRotationAngle(this.head, 0.2F, 0.0F, 0.0F);
		this.body.addChild(this.head);
	}
	
	public static void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	@Override @Nonnull
	public ModelRenderer getHead() {
		return this.head;
	}
	
	@Override @Nonnull
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = ((float)entity.getUniqueFlapTickOffset() + ageInTicks) * 7.448451F * ((float)Math.PI / 180F);
		float f1 = 16.0F;
		this.leftWingBase.zRot = MathHelper.cos(f) * f1 * ((float)Math.PI / 180F);
		this.leftWingTip.zRot = MathHelper.cos(f) * f1 * ((float)Math.PI / 180F);
		this.rightWingBase.zRot = -this.leftWingBase.zRot;
		this.rightWingTip.zRot = -this.leftWingTip.zRot;
		this.tailBase.xRot = -(5.0F + MathHelper.cos(f * 2.0F) * 5.0F) * ((float)Math.PI / 180F);
		this.tailTip.xRot = -(5.0F + MathHelper.cos(f * 2.0F) * 5.0F) * ((float)Math.PI / 180F);
	}
}