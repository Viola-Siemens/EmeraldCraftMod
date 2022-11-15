package com.hexagram2021.emeraldcraft.client;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.entities.ECBoat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class ECBoatRenderer extends BoatRenderer {
	private final Map<ECBoat.ECBoatType, Pair<ResourceLocation, BoatModel>> boatResources;

	public ECBoatRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.boatResources = Stream.of(ECBoat.ECBoatType.values()).collect(
				ImmutableMap.toImmutableMap((key) -> key,
				(model) -> Pair.of(
						new ResourceLocation(MODID, "textures/entity/boat/" + model.getName() + ".png"),
						new BoatModel(context.bakeLayer(createBoatModelName(model)))
				))
		);
	}

	@Override @NotNull
	public Pair<ResourceLocation, BoatModel> getModelWithLocation(@NotNull Boat boat) {
		return this.boatResources.get(((ECBoat)boat).getECBoatType());
	}

	@SuppressWarnings("SameParameterValue")
	private static ModelLayerLocation createLocation(String name, String layer) {
		return new ModelLayerLocation(new ResourceLocation(MODID, name), layer);
	}

	public static ModelLayerLocation createBoatModelName(ECBoat.ECBoatType model) {
		return createLocation("boat/" + model.getName(), "main");
	}
}