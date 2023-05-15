package com.hexagram2021.emeraldcraft.client.models;

import com.hexagram2021.emeraldcraft.common.entities.mobs.PurpleSpottedBigeyeEntity;
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
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class PurpleSpottedBigeyeModel<T extends PurpleSpottedBigeyeEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "purple_spotted_bigeye"), "main");
	private final ModelPart root;
	private final ModelPart tail;

	public PurpleSpottedBigeyeModel(ModelPart root) {
		this.root = root;
		this.tail = root.getChild("body").getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-0.5F, -6.0F, -2.0F, 2.0F, 6.0F, 8.0F)
						.texOffs(20, 4).addBox(0.5F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F)
						.texOffs(20, -6).addBox(0.5F, -10.0F, 0.0F, 0.0F, 4.0F, 6.0F),
				PartPose.offset(-0.5F, 24.0F, 0.0F));

		body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, -1).addBox(0.0F, -6.0F, 0.0F, 0.0F, 6.0F, 5.0F), PartPose.offset(0.5F, 0.0F, 6.0F));

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