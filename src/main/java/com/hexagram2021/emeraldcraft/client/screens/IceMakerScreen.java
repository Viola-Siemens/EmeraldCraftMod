package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerMenu;
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
public class IceMakerScreen extends ContainerScreen<IceMakerMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/ice_maker.png");

	public IceMakerScreen(IceMakerMenu menu, PlayerInventory inventory, ITextComponent component) {
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

		int l = this.menu.getFreezeProgress();
		this.blit(transform, i + 97, j + 34, 176, 8, l + 1, 16);

		int ingredientLevel = this.menu.getIngredientFluidLevel();
		if(ingredientLevel > 0) {
			int k = MathHelper.clamp((IceMakerBlockEntity.MAX_INGREDIENT_FLUID_LEVEL - 1 - ingredientLevel) / 20, 0, 49);
			this.blit(transform, i + 79, j + 18 + k, 12 * FluidTypes.getFluidTypeWithID(this.menu.getFluidTypeIndex()).getGUIID(), 166 + k, 12, 49 - k);
		}

		int condensateLevel = this.menu.getCondensateFluidLevel();
		if(condensateLevel > 0) {
			int k = MathHelper.clamp((IceMakerBlockEntity.MAX_CONDENSATE_FLUID_LEVEL - condensateLevel) / 25, 0, 32);
			this.blit(transform, i + 8 + k, j + 64, 176 + k, 0, 32 - k, 8);
		}
	}
	
	@Override
	protected void renderTooltip(@Nonnull MatrixStack transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.hoveredSlot == null && this.isHovering(79, 18, 12, 49, x, y) && this.menu.getIngredientFluidLevel() > 0) {
			this.renderComponentTooltip(transform, this.getFluidTypeToolTips(this.menu.getIngredientFluidLevel(), this.menu.getFluidType()), x, y);
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
