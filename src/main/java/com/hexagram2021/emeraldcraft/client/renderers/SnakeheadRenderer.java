package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.SnakeheadModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.SnakeheadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class SnakeheadRenderer extends MobRenderer<SnakeheadEntity, SnakeheadModel<SnakeheadEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/fishes/snakehead.png");

	public SnakeheadRenderer(EntityRendererProvider.Context manager) {
		super(manager, new SnakeheadModel<>(manager.bakeLayer(SnakeheadModel.LAYER_LOCATION)), 0.7F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull SnakeheadEntity entity) { return TEXTURE; }
}
