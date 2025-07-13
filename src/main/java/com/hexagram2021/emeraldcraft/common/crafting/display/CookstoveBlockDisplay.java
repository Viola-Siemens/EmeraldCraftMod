package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryEntry;
import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public record CookstoveBlockDisplay(Block block) implements ICookstoveDisplay {
    public static final Codec<CookstoveBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(CookstoveBlockDisplay::block)
    ).apply(instance, CookstoveBlockDisplay::new));

    public static CookstoveBlockDisplay fromNetwork(FriendlyByteBuf buf) {
        return new CookstoveBlockDisplay(getRegistryEntry(BuiltInRegistries.BLOCK, buf.readResourceLocation()));
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

    @Override
    public void render(PoseStack transform, CookstoveRenderer renderer, CookstoveBlockEntity blockEntity, MultiBufferSource buffer, int color, int overlay) {
        transform.pushPose();
        transform.translate(0.0D, 0.575F, 0.0D);
        renderer.getBlockRenderer().renderSingleBlock(this.block.defaultBlockState(), transform, buffer, color, overlay);
        transform.popPose();
    }
}
