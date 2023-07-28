package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.SnakeheadEntity;
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

public class SnakeheadModel<T extends SnakeheadEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "snakehead"), "main");
	private final ModelPart root;
	private final ModelPart bodyBack;

	public SnakeheadModel(ModelPart root) {
		this.root = root;
		this.bodyBack = root.getChild("body_front").getChild("body_back");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body_front = partdefinition.addOrReplaceChild("body_front",
				CubeListBuilder.create().texOffs(4, 0).addBox(-1.5F, -8.5F, 1.0F, 3.0F, 5.0F, 7.0F),
				PartPose.offset(0.0F, 24.0F, -4.0F));

		PartDefinition body_back = body_front.addOrReplaceChild("body_back",
				CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -8.5F, 0.0F, 3.0F, 5.0F, 8.0F),
				PartPose.offset(0.0F, 0.0F, 8.0F));

		body_back.addOrReplaceChild("dorsal_back",
				CubeListBuilder.create().texOffs(0, -3).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 5.0F),
				PartPose.offset(0.0F, -5.0F, 0.0F));
		body_back.addOrReplaceChild("tail_fin",
				CubeListBuilder.create().texOffs(20, 10).addBox(0.0F, -8.5F, 0.0F, 0.0F, 5.0F, 6.0F),
				PartPose.offset(0.0F, 0.0F, 8.0F));

		body_front.addOrReplaceChild("dorsal_front",
				CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, -5.0F, 6.0F));

		body_front.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -3.5F, -3.0F, 2.0F, 3.0F, 4.0F),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		body_front.addOrReplaceChild("left_fin",
				CubeListBuilder.create().texOffs(2, 0).addBox(-2.0075F, -2.867F, 2.0F, 2.0F, 0.0F, 2.0F),
				PartPose.offsetAndRotation(1.5F, -1.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		body_front.addOrReplaceChild("right_fin",
				CubeListBuilder.create().texOffs(-2, 0).addBox(0.0074F, -2.867F, 2.0F, 2.0F, 0.0F, 2.0F),
				PartPose.offsetAndRotation(-1.5F, -1.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		float f1 = 1.0F;
		if (!entity.isInWater()) {
			f = 1.3F;
			f1 = 1.7F;
		}

		this.bodyBack.yRot = -f * 0.25F * Mth.sin(f1 * 0.6F * ageInTicks);
	}
}