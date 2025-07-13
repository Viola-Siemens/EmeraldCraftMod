package com.hexagram2021.emeraldcraft.client.screens;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.common.blocks.entity.MelterBlockEntity;
import com.hexagram2021.emeraldcraft.common.blocks.entity.Tank;
import com.hexagram2021.emeraldcraft.common.crafting.menu.MelterMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@Environment(EnvType.CLIENT)
public class MelterScreen extends AbstractContainerScreen<MelterMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(MODID, "textures/gui/container/melter.png");

    public MelterScreen(MelterMenu menu, Inventory inventory, Component component) {
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
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            transform.blit(BG_LOCATION, left + 41, top + 36 + 12 - litProgress, 176, 12 - litProgress, 14, litProgress + 1);
        }

        int progress = this.menu.getBurnProgress();
        transform.blit(BG_LOCATION, left + 64, top + 34, 176, 14, progress + 1, 16);

        ScreenUtils.renderFluidFromContainer(transform, this.menu.getContainer(), 0, left + 105, top + 18, 12, 50, MelterBlockEntity.MAX_FLUID_LEVEL);
    }

    @Override
    protected void renderTooltip(GuiGraphics transform, int x, int y) {
        super.renderTooltip(transform, x, y);
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot == null && this.isHovering(105, 18, 12, 49, x, y)) {
            if (this.menu.getContainer() instanceof Tank tank) {
                FluidStack fluidStack = tank.getFluidStack(0);
                if (!fluidStack.isEmpty()) {
                    transform.renderTooltip(this.font, this.getFluidTypeToolTips(fluidStack), Optional.empty(), x, y);
                }
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private List<Component> getFluidTypeToolTips(FluidStack fluidStack) {
        ResourceLocation fluidKey = BuiltInRegistries.FLUID.getKey(fluidStack.getFluid());
        List<Component> ret = Lists.newArrayList(Component.translatable("fluid." + fluidKey.toLanguageKey()));
        if (this.minecraft != null && this.minecraft.options.advancedItemTooltips) {
            ret.add(Component.translatable("fluids.save.bucket", String.format("%.2f", fluidStack.getAmount() / (float) FluidConstants.BUCKET), fluidStack.getFluid().getBucket().getDescription()));
        }
        return ret;
    }
}
