package com.hexagram2021.emeraldcraft.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class PurpleSpottedBigeyeModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer tail;

	public PurpleSpottedBigeyeModel() {
		this.texWidth = 32;
		this.texHeight = 16;
		
		this.body = new ModelRenderer(this);
		this.body.setPos(-0.5F, 24.0F, 0.0F);
		this.body.texOffs(0, 0).addBox(-0.5F, -6.0F, -2.0F, 2.0F, 6.0F, 8.0F)
				.texOffs(20, 4).addBox(0.5F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F)
				.texOffs(20, -6).addBox(0.5F, -10.0F, 0.0F, 0.0F, 4.0F, 6.0F);
		
		this.tail = new ModelRenderer(this);
		this.tail.setPos(0.5F, 0.0F, 6.0F);
		this.tail.texOffs(22, -1).addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F);
		this.body.addChild(this.tail);
	}
	
	@Override @Nonnull
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body);
	}

	@Override
	public void setupAnim(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}

		this.tail.yRot = -f * 0.45F * MathHelper.sin(0.6F * ageInTicks);
	}
}