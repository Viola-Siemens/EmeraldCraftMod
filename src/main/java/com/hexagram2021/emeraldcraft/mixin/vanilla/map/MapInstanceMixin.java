package com.hexagram2021.emeraldcraft.mixin.vanilla.map;

import com.hexagram2021.emeraldcraft.client.MapCustomIcons;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(MapRenderer.MapInstance.class)
public class MapInstanceMixin {
	@WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration;getImage()B"))
	private byte emeraldcraft$getImageAndUpdateBuffer(MapDecoration instance, Operation<Byte> original, @Share(value = "decorationRenderType") LocalRef<RenderType> decorationRenderType) {
		MapDecoration.Type type = instance.type();
		decorationRenderType.set(MapCustomIcons.RENDER_TYPES.get(type));
		return MapCustomIcons.ORDINARIES.getOrDefault(type, original.call(instance));
	}

	@WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 1))
	private VertexConsumer emeraldcraft$getVertexConsumerForCustomIcons(MultiBufferSource instance, RenderType renderType, Operation<VertexConsumer> original, @Share(value = "decorationRenderType") LocalRef<RenderType> decorationRenderType) {
		return original.call(instance, Objects.requireNonNullElse(decorationRenderType.get(), renderType));
	}
}
