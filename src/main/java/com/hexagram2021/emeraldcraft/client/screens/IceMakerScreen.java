package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.common.crafting.menu.IceMakerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class IceMakerScreen extends AbstractContainerScreen<IceMakerMenu> {
	private static final ResourceLocation BURN_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/ice_maker/burn_progress");
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/ice_maker.png");

	public IceMakerScreen(IceMakerMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
		--this.titleLabelY;
	}

	@Override
	public void render(GuiGraphics transform, int x, int y, float partialTicks) {
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		transform.blit(BG_LOCATION, left, top, 0, 0, this.imageWidth, this.imageHeight);

		int progress = this.menu.getFreezeProgress();
		transform.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, left + 97, top + 34, progress + 1, 16);

		ScreenUtils.renderFluidFromContainer(transform, this.menu.getContainer(), 0, left + 79, top + 18, 12, 50, IceMakerBlockEntity.MAX_INGREDIENT_FLUID_LEVEL);
		ScreenUtils.renderFluidFromContainer(transform, this.menu.getContainer(), 1, left + 8, top + 64, 32, 8, IceMakerBlockEntity.MAX_CONDENSATE_FLUID_LEVEL);
	}

	@Override
	protected void renderTooltip(GuiGraphics transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.menu.getCarried().isEmpty() && this.hoveredSlot == null && this.isHovering(79, 18, 12, 49, x, y)) {
			if(this.menu.getContainer() instanceof Tank tank) {
				FluidStack fluidStack = tank.getFluidStack(0);
				if(!fluidStack.isEmpty()) {
					transform.renderTooltip(this.font, this.getFluidTypeToolTips(fluidStack), Optional.empty(), x, y);
				}
			}
		}
	}

	private List<Component> getFluidTypeToolTips(FluidStack fluidStack) {
		List<Component> ret = Lists.newArrayList(fluidStack.getFluid().getFluidType().getDescription());
		if(this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
			ret.add(Component.translatable("fluids.save.bucket", String.format("%.2f", fluidStack.getAmount() / 100.0F), fluidStack.getFluid().getBucket().getDescription()));
		}
		return ret;
	}
}
