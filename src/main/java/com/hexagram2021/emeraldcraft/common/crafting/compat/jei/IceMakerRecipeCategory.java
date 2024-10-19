package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hexagram2021.emeraldcraft.common.blocks.entity.IceMakerBlockEntity;
import com.hexagram2021.emeraldcraft.common.crafting.IceMakerRecipe;
import com.hexagram2021.emeraldcraft.common.register.ECBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class IceMakerRecipeCategory implements IRecipeCategory<IceMakerRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "ice_maker");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/gui_ice_maker.png");

	public static final int FREEZETIME = 100;

	private final IDrawable background;
	private final IDrawable icon;
	private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

	public IceMakerRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 148, 56);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ECBlocks.WorkStation.ICE_MAKER));

		this.cachedArrows = CacheBuilder.newBuilder()
				.maximumSize(25)
				.build(new CacheLoader<>() {
					@Override
					public IDrawableAnimated load(Integer cookTime) {
						return guiHelper.drawableBuilder(TEXTURE, 148, 8, 24, 17)
								.buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
					}
				});
	}

	@Override
	public RecipeType<IceMakerRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.ICE_MAKER;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("block.emeraldcraft.ice_maker");
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
	public void draw(IceMakerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics transform, double mouseX, double mouseY) {
		IDrawableAnimated arrow = this.getArrow(recipe);
		arrow.draw(transform, 90, 16);
		this.drawCookTime(recipe, transform, 49);
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawCookTime(IceMakerRecipe recipe, GuiGraphics transform, int y) {
		int freezeTime = recipe.freezingTime();
		if (freezeTime > 0) {
			int cookTimeSeconds = freezeTime / 20;
			Component timeString = Component.translatable("gui.emeraldcraft.ice_maker.time.seconds", cookTimeSeconds);
			Minecraft minecraft = Minecraft.getInstance();
			Font fontRenderer = minecraft.font;
			int stringWidth = fontRenderer.width(timeString);
			transform.drawString(fontRenderer, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IceMakerRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 9, 9).addItemStack(new ItemStack(Items.WATER_BUCKET));
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 47)
				.setFluidRenderer(IceMakerBlockEntity.MAX_CONDENSATE_FLUID_LEVEL, false, 32, 8)
				.addFluidStack(Fluids.WATER, IceMakerBlockEntity.MAX_CONDENSATE_FLUID_LEVEL);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 19).addItemStack(RecipeUtil.getResultItem(recipe));
		builder.addSlot(RecipeIngredientRole.INPUT, 72, 1)
				.setFluidRenderer(IceMakerBlockEntity.MAX_INGREDIENT_FLUID_LEVEL, false, 12, 50)
				.addFluidStack(recipe.inputFluid().getFluid(), recipe.inputFluid().getAmount());
	}

	protected IDrawableAnimated getArrow(IceMakerRecipe recipe) {
		int freezeTime = recipe.freezingTime();
		if (freezeTime <= 0) {
			freezeTime = FREEZETIME;
		}
		return this.cachedArrows.getUnchecked(freezeTime);
	}
}
