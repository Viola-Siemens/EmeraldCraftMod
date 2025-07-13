package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.WraithModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.WraithEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Environment(EnvType.CLIENT)
public class WraithRenderer extends MobRenderer<WraithEntity, WraithModel<WraithEntity>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/wraith/wraith.png");

    public WraithRenderer(EntityRendererProvider.Context manager) {
        super(manager, new WraithModel<>(manager.bakeLayer(WraithModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(WraithEntity entity) {
        return TEXTURE;
    }
}
