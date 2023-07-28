package com.hexagram2021.emeraldcraft.client.screens;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.fluid.FluidTypes;
import com.hexagram2021.emeraldcraft.common.blocks.entity.ContinuousMinerBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.menu.ContinuousMinerMenu;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		this.renderTooltip(transform, x, y);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		transform.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int energyLevel = this.menu.getFluidLevel();
		if(energyLevel > 0) {
			int k = Mth.clamp((ContinuousMinerBlockEntity.MAX_FLUID_LEVEL - 1 - energyLevel) / 5, 0, 49);
			transform.blit(BG_LOCATION, i + 119, j + 20 + k, 176, k, 12, 49 - k);
		}
	}

	@Override
	protected void renderTooltip(GuiGraphics transform, int x, int y) {
		super.renderTooltip(transform, x, y);
		if(this.menu.getCarried().isEmpty() && this.hoveredSlot == null && this.isHovering(119, 20, 12, 49, x, y) && this.menu.getFluidLevel() > 0) {
			transform.renderTooltip(this.font, this.getFluidTypeToolTips(this.menu.getFluidLevel()), Optional.empty(), x, y);
		}
	}

	private List<Component> getFluidTypeToolTips(int fluidLevel) {
		List<Component> ret = Lists.newArrayList(Component.translatable(FluidTypes.melted_emerald.getTranslationTag()));
		if(this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
			ret.add(Component.translatable("fluids.save.bucket", String.format("%.2f", fluidLevel / 100.0F), Component.translatable(ECItems.MELTED_EMERALD_BUCKET.get().getDescriptionId())));
		}
		return ret;
	}
}
