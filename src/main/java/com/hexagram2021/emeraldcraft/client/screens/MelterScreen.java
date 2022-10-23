package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.common.blocks.entity.MelterBlockEntity;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.MelterMenu;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class MelterScreen extends ContainerScreen<MelterMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/melter.png");

	public MelterScreen(MelterMenu menu, PlayerInventory inventory, ITextComponent component) {
		super(menu, inventory, component);
		--this.titleLabelY;
	}

	@Override
	public void render(@Nonnull MatrixStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack transform, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BG_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);
		if (this.menu.isLit()) {
			int k = this.menu.getLitProgress();
			this.blit(transform, i + 41, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}

		int l = this.menu.getBurnProgress();
		this.blit(transform, i + 64, j + 34, 176, 14, l + 1, 16);

		int energyLevel = this.menu.getFluidLevel();
		if(energyLevel > 0) {
			int k = MathHelper.clamp((MelterBlockEntity.MAX_FLUID_LEVEL - 1 - energyLevel) / 20, 0, 49);
			this.blit(transform, i + 105, j + 18 + k, 12 * FluidTypes.getFluidTypeWithID(this.menu.getFluidTypeIndex()).getGUIID(), 166 + k, 12, 49 - k);
		}
	}
	
	@Override
	protected void renderTooltip(@Nonnull MatrixStack transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.hoveredSlot == null && this.isHovering(105, 18, 12, 49, x, y) && this.menu.getFluidLevel() > 0) {
			this.renderComponentTooltip(transform, this.getFluidTypeToolTips(this.menu.getFluidLevel(), this.menu.getFluidType()), x, y);
		}
	}
	
	private List<ITextComponent> getFluidTypeToolTips(int fluidLevel, FluidType fluidType) {
		List<ITextComponent> ret = Lists.newArrayList(new TranslationTextComponent(fluidType.getTranslationTag()));
		if(this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
			ret.add(new TranslationTextComponent("fluids.save.bucket", String.format("%.2f", fluidLevel / 100.0F), new TranslationTextComponent(FluidTypes.getFluidBucketItem(fluidType).getDescriptionId())));
		}
		return ret;
	}
}
