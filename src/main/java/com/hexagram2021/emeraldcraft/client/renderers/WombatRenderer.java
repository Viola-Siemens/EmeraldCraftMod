package com.hexagram2021.emeraldcraft.client.renderers;

import com.hexagram2021.emeraldcraft.client.models.WombatModel;
import com.hexagram2021.emeraldcraft.common.entities.mobs.WombatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class WombatRenderer extends MobRenderer<WombatEntity, WombatModel<WombatEntity>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/entity/wombat/wombat.png");

	public WombatRenderer(EntityRendererProvider.Context manager) {
		super(manager, new WombatModel<>(manager.bakeLayer(WombatModel.LAYER_LOCATION)), 0.7F);
	}

	@Override
	public ResourceLocation getTextureLocation(WombatEntity entity) { return TEXTURE; }
}
