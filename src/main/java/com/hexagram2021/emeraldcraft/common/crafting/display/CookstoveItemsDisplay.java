package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.hexagram2021.emeraldcraft.client.renderers.block.CookstoveRenderer;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.util.CodecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public record CookstoveItemsDisplay(Background background, Ingredient ingredient) implements ICookstoveDisplay {
	public static final Codec<CookstoveItemsDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Background.CODEC.fieldOf("background").forGetter(CookstoveItemsDisplay::background),
			CodecUtil.INGREDIENT_CODEC.fieldOf("ingredient").forGetter(CookstoveItemsDisplay::ingredient)
	).apply(instance, CookstoveItemsDisplay::new));

	public static final ResourceLocation COOKSTOVE_ATLAS = new ResourceLocation(MODID, "textures/atlas/cookstove_shapes.png");

	public static CookstoveItemsDisplay fromNetwork(FriendlyByteBuf buf) {
		Background background = Background.fromNetwork(buf);
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		return new CookstoveItemsDisplay(background, ingredient);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		ICookstoveDisplay.super.toNetwork(buf);
		this.background.toNetwork(buf);
		this.ingredient.toNetwork(buf);
	}

	@Override
	public ICookstoveDisplayType type() {
		return CookstoveDisplayTypes.ITEMS;
	}

	@Override
	public void render(PoseStack transform, CookstoveRenderer renderer, CookstoveBlockEntity blockEntity, MultiBufferSource buffer, int color, int overlay) {
		// items
		ItemStack[] displayItems = this.ingredient().getItems();
		int length = displayItems.length * blockEntity.getResult().getCount();
		for(int i = 0; i < length; ++i) {
			double angle = i * Math.PI * 2.0D / length;
			double dz = Math.cos(angle) * 0.25D;
			double dx = Math.sin(angle) * 0.25D;
			transform.pushPose();
			transform.translate(dx + 0.5D, 0.6F, dz + 0.5D);
			transform.mulPose(Axis.YP.rotationDegrees(Mth.RAD_TO_DEG * (float) angle));
			transform.mulPose(Axis.XP.rotationDegrees(85.0F));
			transform.scale(0.25F, 0.25F, 0.25F);
			renderer.getItemRenderer().renderStatic(displayItems[i % displayItems.length], ItemDisplayContext.FIXED, color, overlay, transform, buffer, blockEntity.getLevel(), 0);
			transform.popPose();
		}
		// shape
		transform.pushPose();
		transform.translate(0.5D, 0.575F, 0.5D);
		Material shape = new Material(COOKSTOVE_ATLAS, this.background().shape());
		int backgroundColor = this.background().color();
		int r = (backgroundColor >> 16) & 0xff;
		int g = (backgroundColor >> 8) & 0xff;
		int b = backgroundColor & 0xff;
		renderer.getDisplayModel().renderToBuffer(transform, shape.buffer(buffer, RenderType::entityTranslucent), color, overlay, r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
		transform.popPose();
	}

	public record Background(int color, ResourceLocation shape) {
		public static final Codec<Background> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("color").forGetter(Background::color),
				ResourceLocation.CODEC.fieldOf("shape").forGetter(Background::shape)
		).apply(instance, Background::new));

		public static Background fromNetwork(FriendlyByteBuf buf) {
			int color = buf.readVarInt();
			ResourceLocation shape = buf.readResourceLocation();
			return new Background(color, shape);
		}

		public void toNetwork(FriendlyByteBuf buf) {
			buf.writeVarInt(this.color);
			buf.writeResourceLocation(this.shape);
		}
	}
}
