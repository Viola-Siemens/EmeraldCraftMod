package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
	@Shadow @Final @Mutable
	private static Map<ResourceLocation, ResourceLocation> VANILLA_ATLASES;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resources/model/ModelManager;VANILLA_ATLASES:Ljava/util/Map;", shift = At.Shift.AFTER))
	private static void emeraldcraft$registerAtlases(CallbackInfo ci) {
		if(VANILLA_ATLASES instanceof HashMap<ResourceLocation, ResourceLocation>) {
			VANILLA_ATLASES.put(CookstoveRenderer.COOKSTOVE_ATLAS, new ResourceLocation(MODID, "cookstove_shapes"));
		} else {
			ImmutableMap.Builder<ResourceLocation, ResourceLocation> builder = ImmutableMap.builder();
			builder.putAll(VANILLA_ATLASES);
			builder.put(CookstoveRenderer.COOKSTOVE_ATLAS, new ResourceLocation(MODID, "cookstove_shapes"));
			VANILLA_ATLASES = builder.build();
		}
	}
}
