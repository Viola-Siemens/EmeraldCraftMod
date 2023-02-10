package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.MantaModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.MantaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class MantaRenderer extends MobRenderer<MantaEntity, MantaModel<MantaEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/manta/manta.png");

	public MantaRenderer(EntityRendererProvider.Context manager) {
		super(manager, new MantaModel<>(manager.bakeLayer(MantaModel.LAYER_LOCATION)), 0.8F);
	}

	@Override @NotNull
	public ResourceLocation getTextureLocation(@NotNull MantaEntity entity) { return TEXTURE; }

	@Override
	protected int getBlockLightLevel(@NotNull MantaEntity manta, @NotNull BlockPos blockPos) {
		return 15;
	}
}
