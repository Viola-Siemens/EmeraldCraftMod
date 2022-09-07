package com.hexagram2021.emeraldcraft.common.crafting.recipebook;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

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
}
