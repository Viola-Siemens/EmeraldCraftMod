package com.hexagram2021.emeraldcraft.client;

import com.hexagram2021.emeraldcraft.client.models.MantaModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class MantaRenderer extends MobRenderer<MantaEntity, MantaModel<MantaEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/manta/manta.png");

	public MantaRenderer(EntityRendererManager manager) {
		super(manager, new MantaModel<>(0.0F), 0.8F);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull MantaEntity entity) { return TEXTURE; }
}
