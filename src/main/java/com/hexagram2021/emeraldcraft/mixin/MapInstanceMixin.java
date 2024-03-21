package com.hexagram2021.emeraldcraft.mixin;

import com.hexagram2021.emeraldcraft.client.MapCustomIcons;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(MapRenderer.MapInstance.class)
public class MapInstanceMixin {
	@Unique @Nullable
	private RenderType emeraldcraft$bufferDecorationRenderType = null;

	@Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration;getImage()B"))
	private byte getImageAndUpdateBuffer(MapDecoration instance) {
		MapDecoration.Type type = instance.type();
		this.emeraldcraft$bufferDecorationRenderType = MapCustomIcons.RENDER_TYPES.get(type);
		return MapCustomIcons.ORDINARIES.getOrDefault(type, instance.getImage());
	}

	@Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 1))
	private VertexConsumer getVertexConsumerForCustomIcons(MultiBufferSource instance, RenderType renderType) {
		return instance.getBuffer(Objects.requireNonNullElse(this.emeraldcraft$bufferDecorationRenderType, renderType));
	}
}
