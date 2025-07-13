package com.hexagram2021.emeraldcraft.mixin.vanilla.render;

import cn.sh1rocu.emeraldcraft.util.api.extension.CustomBoatModel;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(BoatRenderer.class)
public class BoatRendererMixin {
    @ModifyReturnValue(method = "getTextureLocation(Lnet/minecraft/world/entity/vehicle/Boat;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation ec$getTextureLocation(ResourceLocation original, Boat boat) {
        if (this instanceof CustomBoatModel customModel)
            return customModel.getModelWithLocation(boat).getFirst();
        return original;
    }

    @WrapOperation(
            method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object getCustomModel(Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> instance, Object o, Operation<Pair<ResourceLocation, ListModel<Boat>>> original, Boat boat) {
        if (this instanceof CustomBoatModel customModel)
            return customModel.getModelWithLocation(boat);
        return original.call(instance, o);
    }
}