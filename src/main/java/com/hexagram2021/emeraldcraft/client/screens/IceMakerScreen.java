package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.fluid.FluidType;
import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class IceMakerScreen extends AbstractContainerScreen<IceMakerMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/ice_maker.png");

	public IceMakerScreen(IceMakerMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
		--this.titleLabelY;
	}

	@Override
	public void render(@NotNull PoseStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(@NotNull PoseStack transform, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BG_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(transform, i, j, 0, 0, this.imageWidth, this.imageHeight);

		int l = this.menu.getFreezeProgress();
		this.blit(transform, i + 97, j + 34, 176, 8, l + 1, 16);

		int ingredientLevel = this.menu.getIngredientFluidLevel();
		if(ingredientLevel > 0) {
			int k = Mth.clamp((IceMakerBlockEntity.MAX_INGREDIENT_FLUID_LEVEL - 1 - ingredientLevel) / 20, 0, 49);
			this.blit(transform, i + 79, j + 18 + k, 12 * FluidTypes.getFluidTypeWithID(this.menu.getFluidTypeIndex()).getGUIID(), 166 + k, 12, 49 - k);
		}

		int condensateLevel = this.menu.getCondensateFluidLevel();
		if(condensateLevel > 0) {
			int k = Mth.clamp((IceMakerBlockEntity.MAX_CONDENSATE_FLUID_LEVEL - condensateLevel) / 25, 0, 32);
			this.blit(transform, i + 8 + k, j + 64, 176 + k, 0, 32 - k, 8);
		}
	}

	@Override
	protected void renderTooltip(@NotNull PoseStack transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.menu.getCarried().isEmpty() && this.hoveredSlot == null && this.isHovering(79, 18, 12, 49, x, y) && this.menu.getIngredientFluidLevel() > 0) {
			this.renderTooltip(transform, this.getFluidTypeToolTips(this.menu.getIngredientFluidLevel(), this.menu.getFluidType()), Optional.empty(), x, y);
		}
	}

	private List<Component> getFluidTypeToolTips(int fluidLevel, FluidType fluidType) {
		List<Component> ret = Lists.newArrayList(Component.translatable(fluidType.getTranslationTag()));
		if(this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
			ret.add(Component.translatable("fluids.save.bucket", String.format("%.2f", fluidLevel / 100.0F), Component.translatable(FluidTypes.getFluidBucketItem(fluidType).getDescriptionId())));
		}
		return ret;
	}
}
