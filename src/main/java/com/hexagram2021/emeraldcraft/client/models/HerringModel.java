package com.hexagram2021.emeraldcraft.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class HerringModel<T extends Entity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "herring"), "main");
	private final ModelPart root;
	private final ModelPart tail;

	public HerringModel(ModelPart root) {
		this.root = root;
		this.tail = root.getChild("body").getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-0.5F, -4.0F, -3.0F, 2.0F, 3.0F, 6.0F)
						.texOffs(10, -6).addBox(0.5F, -8.0F, -3.0F, 0.0F, 4.0F, 6.0F)
						.texOffs(17, 1).addBox(0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 6.0F),
				PartPose.offset(-0.5F, 24.0F, 0.0F)
		);

		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, -4).addBox(0.0F, -4.0F, 0.0F, 0.0F, 3.0F, 4.0F), PartPose.offset(0.5F, 0.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override @NotNull
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}

		this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}
}