package com.hexagram2021.emeraldcraft.common.crafting.compat.jei;

import com.hexagram2021.emeraldcraft.common.crafting.TradeShadowRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public class VillagerTradeCategory implements IRecipeCategory<TradeShadowRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, "villager_trade");
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/villager_trade.png");

	protected static final int VILLAGER_X = 116;
	protected static final int VILLAGER_Y = 46;
	protected static final int VILLAGER_W = 47;
	protected static final int VILLAGER_H = 71;

	private final IDrawable background;
	private final IDrawable icon;

	public VillagerTradeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 166, 120);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Items.EMERALD));
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("jei.emeraldcraft.villager_trade");
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

	@Override
	public void draw(TradeShadowRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		renderEntityInCategory(stack, VILLAGER_X, VILLAGER_Y, VILLAGER_W, VILLAGER_H, mouseX, mouseY, recipe.getRenderVillager());
		drawVillagerName(recipe.getRenderVillager().getName().getString(), stack, 30);
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends TradeShadowRecipe> getRecipeClass() {
		return TradeShadowRecipe.class;
	}

	@SuppressWarnings("SameParameterValue")
	protected void drawVillagerName(String name, PoseStack poseStack, int y) {
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		int stringWidth = fontRenderer.width(name);
		fontRenderer.draw(poseStack, name, this.background.getWidth() - stringWidth, y, 0xFF808080);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TradeShadowRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 15).addItemStack(recipe.getCostA());
		builder.addSlot(RecipeIngredientRole.INPUT, 34, 15).addItemStack(recipe.getCostB());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 15).addItemStack(recipe.getResultItem());
	}

	@SuppressWarnings("deprecation")
	public static void renderEntityInCategory(PoseStack transform, int x, int y, int w, int h, double mouseX, double mouseY, LivingEntity livingEntity) {
		float scale = h / 2.4f;
		int cx = x + (w / 2);
		int cy = y + (h / 2);
		int by = y + h;
		int offsetY = 4;

		float headYaw = (float) Math.atan((cx - mouseX) / 40.0F) * 40.0F;
		float yaw = (float) Math.atan((cx - mouseX) / 40.0F) * 20.0F;
		float pitch = (float) Math.atan((cy - offsetY - mouseY) / 40.0F) * 20.0F;

		double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
		double[] xyzTranslation = getGLTranslation(transform, guiScale);
		x *= guiScale;
		y *= guiScale;
		w *= guiScale;
		h *= guiScale;
		int scissorX = Math.round(Math.round(xyzTranslation[0] + x));
		int scissorY = Math.round(Math.round(Minecraft.getInstance().getWindow().getScreenHeight() - y - h - xyzTranslation[1]));
		int scissorW = Math.round(w);
		int scissorH = Math.round(h);
		RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);

		final Minecraft mc = Minecraft.getInstance();
		transform.pushPose();
		transform.translate(cx, by - offsetY, 1050.0F);
		transform.scale(1.0F, 1.0F, -1.0F);
		transform.translate(0.0D, 0.0D, 1000.0D);
		transform.scale(scale, scale, scale);
		Quaternion pitchRotation = Vector3f.XP.rotationDegrees(pitch);
		transform.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		transform.mulPose(pitchRotation);
		float oldYawOffset = livingEntity.yBodyRot;
		float oldYaw = livingEntity.getYRot();
		float oldPitch = livingEntity.getXRot();
		float oldPrevYawHead = livingEntity.yHeadRotO;
		float oldYawHead = livingEntity.yHeadRot;
		livingEntity.yBodyRot = 180.0F + yaw;
		livingEntity.setYRot(180.0F + headYaw);
		livingEntity.setXRot(-pitch);
		livingEntity.yHeadRot = livingEntity.getYRot();
		livingEntity.yHeadRotO = livingEntity.getYRot();

		EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
		pitchRotation.conj();
		dispatcher.overrideCameraOrientation(pitchRotation);
		dispatcher.setRenderShadow(false);

		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> dispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, transform, buffers, 0x00F000F0));
		buffers.endBatch();
		dispatcher.setRenderShadow(true);

		livingEntity.yBodyRot = oldYawOffset;
		livingEntity.setYRot(oldYaw);
		livingEntity.setXRot(oldPitch);
		livingEntity.yHeadRotO = oldPrevYawHead;
		livingEntity.yHeadRot = oldYawHead;
		transform.popPose();

		RenderSystem.disableScissor();
	}

	private static double[] getGLTranslation(PoseStack transform, double scale) {
		final Matrix4f matrix = transform.last().pose();
		final FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		matrix.store(buf);
		return new double[] {
				buf.get(getIndexFloatBuffer(0, 3)) * scale,
				buf.get(getIndexFloatBuffer(1, 3)) * scale,
				buf.get(getIndexFloatBuffer(2, 3)) * scale
		};
	}

	@SuppressWarnings("SameParameterValue")
	private static int getIndexFloatBuffer(int x, int y) {
		return y * 4 + x;
	}
}
