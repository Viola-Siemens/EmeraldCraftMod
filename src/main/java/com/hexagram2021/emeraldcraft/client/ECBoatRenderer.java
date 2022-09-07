package com.hexagram2021.emeraldcraft.client;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class ECBoatRenderer extends BoatRenderer {
	private final Map<ECBoat.ECBoatType, ResourceLocation> boatResources;

	public ECBoatRenderer(EntityRendererManager context) {
		super(context);
		this.boatResources = Stream.of(ECBoat.ECBoatType.values()).collect(
				ImmutableMap.toImmutableMap(
						(key) -> key,
						(model) -> new ResourceLocation(MODID, "textures/entity/boat/" + model.getName() + ".png")
				)
		);
	}

	@Override @Nonnull
	public ResourceLocation getTextureLocation(@Nonnull BoatEntity boat) {
		return this.boatResources.get(((ECBoat)boat).getECBoatType());
	}
}