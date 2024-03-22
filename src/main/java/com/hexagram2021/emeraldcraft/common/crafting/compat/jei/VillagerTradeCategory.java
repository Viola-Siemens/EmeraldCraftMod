package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.joml.Quaternionf;

import java.util.List;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class VillagerTradeCategory implements IRecipeCategory<TradeShadowRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "villager_trade");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/villager_trade.png");

	protected static final int VILLAGER_X = 116 + 24;
	protected static final int VILLAGER_Y = 46 + 64;

	private final IDrawable background;
	private final IDrawable icon;

	public VillagerTradeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 120);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.EMERALD));
	}

	@Override
	public Component getTitle() {
		return Component.translatable("jei.emeraldcraft.villager_trade");
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
	public RecipeType<TradeShadowRecipe> getRecipeType() {
		return JEIHelper.ECJEIRecipeTypes.TRADES;
	}

	private static final int VILLAGER_NAME_Y = 34;
	private static final int JOBSITE_Y = 48;
	@Override
	public void draw(TradeShadowRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics transform, double mouseX, double mouseY) {
		final float headYaw = (float) Math.atan((VILLAGER_X - mouseX) / 40.0F) * 40.0F;
		final float yaw = (float) Math.atan((VILLAGER_X - mouseX) / 40.0F) * 20.0F;
		final float pitch = (float) Math.atan((VILLAGER_Y - 32 - mouseY) / 40.0F) * 20.0F;
		renderEntityInCategory(transform.pose(), VILLAGER_X, VILLAGER_Y, 32.0D, headYaw, yaw, pitch, recipe.getRenderVillager());
		this.drawName(recipe.getRenderVillager().getName().getString(), transform, VILLAGER_NAME_Y, false);	//Villager name
		if(recipe.profession() != null && !recipe.profession().equals(VillagerProfession.NONE)) {
			List<Block> jobsites = TradeShadowRecipe.getJobsitesFromProfession(recipe.profession());
			if(!jobsites.isEmpty()) {
				StringBuilder sb = new StringBuilder(Component.translatable(jobsites.get(0).getDescriptionId()).getString());
				for(int i = 1; i < jobsites.size(); ++i) {
					sb.append(" / ");
					sb.append(Component.translatable(jobsites.get(i).getDescriptionId()).getString());
				}
				this.drawName(
						Component.translatable("jei.emeraldcraft.villager_trade.jobsites", sb.toString()).getString(),
						transform, JOBSITE_Y, true
				);																										//Jobsite names
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawName(String name, GuiGraphics transform, int y, boolean leftToRight) {
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		if(leftToRight) {
			transform.drawString(fontRenderer, name, 1, y, 0xFF808080, false);
		} else {
			int stringWidth = fontRenderer.width(name) + 1;
			transform.drawString(fontRenderer, name, this.background.getWidth() - stringWidth, y, 0xFF808080, false);
		}
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TradeShadowRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 15).addItemStack(recipe.costA());
		builder.addSlot(RecipeIngredientRole.INPUT, 34, 15).addItemStack(recipe.costB());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 15).addItemStack(RecipeUtil.getResultItem(recipe));
	}

	@SuppressWarnings({"deprecation", "ConstantValue"})
	public static void renderEntityInCategory(final PoseStack poseStack, final int x, final int y, final double scale,
											  final float headYaw, final float yaw, final float pitch, final LivingEntity livingEntity) {
		final Minecraft mc = Minecraft.getInstance();
		if (livingEntity.level() == null) {
			return;
		}
		poseStack.pushPose();
		poseStack.translate((float) x, (float) y, 1050.0F);
		poseStack.scale(1.0F, 1.0F, -1.0F);
		poseStack.translate(0.0D, 0.0D, 1000.0D);
		poseStack.scale((float) scale, (float) scale, (float) scale);
		final Quaternionf pitchRotation = Axis.XP.rotationDegrees(pitch);
		poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		poseStack.mulPose(pitchRotation);
		final float oldYaw = livingEntity.getYRot();
		final float oldPitch = livingEntity.getXRot();
		final float oldYawOffset = livingEntity.yBodyRot;
		final float oldPrevYawHead = livingEntity.yHeadRotO;
		final float oldYawHead = livingEntity.yHeadRot;
		livingEntity.setYRot(180.0F + headYaw);
		livingEntity.setXRot(-pitch);
		livingEntity.yBodyRot = 180.0F + yaw;
		livingEntity.yHeadRot = livingEntity.getYRot();
		livingEntity.yHeadRotO = livingEntity.getYRot();
		Lighting.setupForEntityInInventory();
		final EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		pitchRotation.conjugate();
		dispatcher.overrideCameraOrientation(pitchRotation);
		dispatcher.setRenderShadow(false);
		final MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> dispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, buffers, 0x00F000F0));
		buffers.endBatch();
		dispatcher.setRenderShadow(true);
		livingEntity.setYRot(oldYaw);
		livingEntity.setXRot(oldPitch);
		livingEntity.yBodyRot = oldYawOffset;
		livingEntity.yHeadRotO = oldPrevYawHead;
		livingEntity.yHeadRot = oldYawHead;
		poseStack.popPose();
		Lighting.setupFor3DItems();
	}
}
