package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.blocks.entity.CookstoveBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.CookstoveRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class CookstoveRecipeCategory implements IRecipeCategory<CookstoveRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "cookstove");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_cookstove.png");
	public static final int COOKTIME = 100;

	private final IDrawable background;
	private final IDrawable tankOverlay;
	private final IDrawableAnimated animatedFlame;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public CookstoveRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 146, 56);
		this.tankOverlay = guiHelper.createDrawable(TEXTURE, 146, 31, 16, 28);
		IDrawableStatic staticFlame = guiHelper.createDrawable(TEXTURE, 146, 0, 14, 14);
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.COOKSTOVE));
		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 146, 14, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	protected IDrawableAnimated getArrow(CookstoveRecipe recipe) {
		int cookTime = recipe.getCookTime();
		if (cookTime <= 0) {
			cookTime = COOKTIME;
		}
		return this.cachedArrows.getUnchecked(cookTime);
	}

	@Override
	public RecipeType<CookstoveRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.COOKSTOVE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.cookstove");
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return this.icon;
	}

	@Override
	public void draw(CookstoveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics transform, double mouseX, double mouseY) {
		this.animatedFlame.draw(transform, 29, 40);

		IDrawableAnimated arrow = this.getArrow(recipe);
		arrow.draw(transform, 93, 14);

		this.drawCookTime(recipe, transform, 45);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(CookstoveRecipe recipe, GuiGraphics transform, int y) {
		int cookTime = recipe.getCookTime();
		if (cookTime > 0) {
			int cookTimeSeconds = cookTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.cookstove.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			transform.drawString(fontRenderer, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CookstoveRecipe recipe, IFocusGroup focuses) {
		builder.setShapeless();
		List<Ingredient> list = recipe.getIngredients();
		for(int i = 0; i < list.size(); ++i) {
			int xOff = 1 + 18 * (i % 4);
			int yOff = 5 + 18 * (i / 4);
			builder.addSlot(RecipeIngredientRole.INPUT, xOff, yOff).addIngredients(list.get(i));
		}
		FluidStack fluidStack = recipe.getFluidStack();
		if(!fluidStack.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.INPUT, 74, 8)
					.setFluidRenderer(CookstoveBlockEntity.MAX_TANK_CAPABILITY * 2, false, 16, 28)
					.addFluidStack(fluidStack.getFluid(), fluidStack.getAmount())
					.setOverlay(this.tankOverlay, 0, 0);
		}
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 97, 37).addIngredients(recipe.getContainer());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 14).addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@Override
	public boolean isHandled(CookstoveRecipe recipe) {
		return !recipe.isSpecial();
	}
}
