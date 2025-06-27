package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ICookstoveDisplay {
	Codec<ICookstoveDisplay> CODEC = ICookstoveDisplayType.REGISTRY_CODEC.dispatch(ICookstoveDisplay::type, ICookstoveDisplayType::codec);

	static ICookstoveDisplay fromNetwork(FriendlyByteBuf buf) {
		return ICookstoveDisplayType.getType(buf.readResourceLocation()).fromNetwork(buf);
	}

	default void toNetwork(FriendlyByteBuf buf) {
		buf.writeResourceLocation(ICookstoveDisplayType.getId(this.type()));
	}
	ICookstoveDisplayType type();

	@OnlyIn(Dist.CLIENT)
	void render(PoseStack transform, CookstoveRenderer renderer, CookstoveBlockEntity blockEntity, MultiBufferSource buffer, int color, int overlay);
}
