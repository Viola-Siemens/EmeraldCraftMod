package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryEntry;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public record CookstoveBlockDisplay(Block block) implements ICookstoveDisplay {
	public static final Codec<CookstoveBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ForgeRegistries.BLOCKS.getCodec().fieldOf("block").forGetter(CookstoveBlockDisplay::block)
	).apply(instance, CookstoveBlockDisplay::new));

	public static CookstoveBlockDisplay fromNetwork(FriendlyByteBuf buf) {
		return new CookstoveBlockDisplay(getRegistryEntry(ForgeRegistries.BLOCKS, buf.readResourceLocation()));
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		ICookstoveDisplay.super.toNetwork(buf);
		buf.writeResourceLocation(getRegistryName(this.block));
	}

	@Override
	public ICookstoveDisplayType type() {
		return CookstoveDisplayTypes.BLOCK;
	}

	@SuppressWarnings("DataFlowIssue")
	@Override
	public void render(PoseStack transform, CookstoveRenderer renderer, CookstoveBlockEntity blockEntity, MultiBufferSource buffer, int color, int overlay) {
		transform.pushPose();
		transform.translate(0.5D, 0.575F, 0.5D);
		renderer.getBlockRenderer().renderSingleBlock(this.block.defaultBlockState(), transform, buffer, color, overlay, ModelData.EMPTY, null);
		transform.popPose();
	}
}
