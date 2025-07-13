package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.MantaModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Environment(EnvType.CLIENT)
public class MantaRenderer extends MobRenderer<MantaEntity, MantaModel<MantaEntity>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/manta/manta.png");

    public MantaRenderer(EntityRendererProvider.Context manager) {
        super(manager, new MantaModel<>(manager.bakeLayer(MantaModel.LAYER_LOCATION)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(MantaEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(MantaEntity manta, BlockPos blockPos) {
        return 15;
    }
}
