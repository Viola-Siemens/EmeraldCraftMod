package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.common.crafting.menu.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECFluids;
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
public class ContinuousMinerScreen extends AbstractContainerScreen<ContinuousMinerMenu> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/continuous_miner.png");

	public ContinuousMinerScreen(ContinuousMinerMenu menu, Inventory inventory, Component component) {
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
		ScreenUtils.renderFluidFromContainer(transform, this.menu.getContainer(), 0, left + 119, top + 20, 12, 50, ContinuousMinerBlockEntity.MAX_FLUID_LEVEL);
	}

	@Override
	protected void renderTooltip(GuiGraphics transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.menu.getCarried().isEmpty() && this.hoveredSlot == null && this.isHovering(119, 20, 12, 49, x, y)) {
			if(this.menu.getContainer() instanceof Tank tank) {
				FluidStack fluidStack = tank.getFluidStack(0);
				if(!fluidStack.isEmpty()) {
					transform.renderTooltip(this.font, this.getFluidTypeToolTips(fluidStack.getAmount()), Optional.empty(), x, y);
				}
			}
		}
	}

	private List<Component> getFluidTypeToolTips(int fluidLevel) {
		List<Component> ret = Lists.newArrayList(ECFluids.MELTED_EMERALD.getStill().getFluidType().getDescription());
		if(this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
			ret.add(Component.translatable("fluids.save.bucket", String.format("%.2f", fluidLevel / 100.0F), ECFluids.MELTED_EMERALD.getBucket().getDescription()));
		}
		return ret;
	}
}
