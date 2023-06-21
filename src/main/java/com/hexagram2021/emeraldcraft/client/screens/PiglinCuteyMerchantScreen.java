package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.PiglinCuteyMerchantMenu;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyData;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PiglinCuteyMerchantScreen extends AbstractContainerScreen<PiglinCuteyMerchantMenu> {
	private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager2.png");
	private static final int TEXTURE_WIDTH = 512;
	private static final int TEXTURE_HEIGHT = 256;
	private static final int MERCHANT_MENU_PART_X = 99;
	private static final int PROGRESS_BAR_X = 136;
	private static final int PROGRESS_BAR_Y = 16;
	private static final int SELL_ITEM_1_X = 5;
	private static final int SELL_ITEM_2_X = 35;
	private static final int BUY_ITEM_X = 68;
	private static final int LABEL_Y = 6;
	private static final int NUMBER_OF_OFFER_BUTTONS = 7;
	private static final int TRADE_BUTTON_X = 5;
	private static final int TRADE_BUTTON_HEIGHT = 20;
	private static final int TRADE_BUTTON_WIDTH = 89;
	private static final int SCROLLER_HEIGHT = 27;
	private static final int SCROLLER_WIDTH = 6;
	private static final int SCROLL_BAR_HEIGHT = 139;
	private static final int SCROLL_BAR_TOP_POS_Y = 18;
	private static final int SCROLL_BAR_START_X = 94;
	private static final Component TRADES_LABEL = Component.translatable("merchant.trades");
	private static final Component LEVEL_SEPARATOR = Component.literal(" - ");
	private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");
	private int shopItem;
	private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[NUMBER_OF_OFFER_BUTTONS];
	int scrollOff;
	private boolean isDragging;

	public PiglinCuteyMerchantScreen(PiglinCuteyMerchantMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 276;
		this.inventoryLabelX = 107;
	}

	private void postButtonClick() {
		this.menu.setSelectionHint(this.shopItem);
		this.menu.tryMoveItems(this.shopItem);
		this.minecraft.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
	}

	@Override
	protected void init() {
		super.init();
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		int buttonY = top + PROGRESS_BAR_Y + 2;

		for(int i = 0; i < NUMBER_OF_OFFER_BUTTONS; ++i) {
			this.tradeOfferButtons[i] = this.addRenderableWidget(new TradeOfferButton(left + TRADE_BUTTON_X, buttonY, i, button -> {
				if (button instanceof TradeOfferButton) {
					this.shopItem = ((TradeOfferButton)button).getIndex() + this.scrollOff;
					this.postButtonClick();
				}

			}));
			buttonY += TRADE_BUTTON_HEIGHT;
		}
	}

	@Override
	protected void renderLabels(@NotNull PoseStack transform, int x, int y) {
		int level = this.menu.getTraderLevel();
		if (level > 0 && level <= 5 && this.menu.showProgressBar()) {
			Component component = this.title.copy().append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + level));
			int fontWidth = this.font.width(component);
			int k = 49 + this.imageWidth / 2 - fontWidth / 2;
			this.font.draw(transform, component, (float)k, 6.0F, 0x404040);
		} else {
			this.font.draw(transform, this.title, (float)(49 + this.imageWidth / 2 - this.font.width(this.title) / 2), 6.0F, 0x404040);
		}

		this.font.draw(transform, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0x404040);
		int l = this.font.width(TRADES_LABEL);
		this.font.draw(transform, TRADES_LABEL, (float)(5 - l / 2 + 48), 6.0F, 0x404040);
	}

	@Override
	protected void renderBg(@NotNull PoseStack transform, float partialTicks, int x, int y) {
		RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		blit(transform, left, top, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		MerchantOffers merchantoffers = this.menu.getOffers();
		if (!merchantoffers.isEmpty()) {
			int k = this.shopItem;
			if (k < 0 || k >= merchantoffers.size()) {
				return;
			}

			MerchantOffer merchantoffer = merchantoffers.get(k);
			if (merchantoffer.isOutOfStock()) {
				RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
				blit(transform, this.leftPos + 83 + MERCHANT_MENU_PART_X, this.topPos + 35, 0, 311.0F, 0.0F, 28, 21, TEXTURE_WIDTH, TEXTURE_HEIGHT);
			}
		}

	}

	private void renderProgressBar(PoseStack transform, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
		int level = this.menu.getTraderLevel();
		int xp = this.menu.getTraderXp();
		if (level < 5) {
			blit(transform, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y, 0, 0.0F, 186.0F, 102, 5, TEXTURE_WIDTH, TEXTURE_HEIGHT);
			int k = PiglinCuteyData.getMinXpPerLevel(level);
			if (xp >= k && PiglinCuteyData.canLevelUp(level)) {
				float f = 100.0F / (float)(PiglinCuteyData.getMaxXpPerLevel(level) - k);
				int progress = Math.min(Mth.floor(f * (float)(xp - k)), 100);
				blit(transform, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y, 0, 0.0F, 191.0F, progress + 1, 5, TEXTURE_WIDTH, TEXTURE_HEIGHT);
				int addXp = this.menu.getFutureTraderXp();
				if (addXp > 0) {
					int addProgress = Math.min(Mth.floor((float)addXp * f), 100 - progress);
					blit(transform, x + PROGRESS_BAR_X + progress + 1, y + PROGRESS_BAR_Y + 1, 0, 2.0F, 182.0F, addProgress, 3, TEXTURE_WIDTH, TEXTURE_HEIGHT);
				}

			}
		}
	}

	private void renderScroller(PoseStack transform, int x, int y, MerchantOffers offers) {
		int overSize = offers.size() + 1 - NUMBER_OF_OFFER_BUTTONS;
		if (overSize > 1) {
			int overHeight = SCROLL_BAR_HEIGHT - (27 + (overSize - 1) * SCROLL_BAR_HEIGHT / overSize);
			int index = 1 + overHeight / overSize + SCROLL_BAR_HEIGHT / overSize;
			int scrollY = Math.min(113, this.scrollOff * index);
			if (this.scrollOff == overSize - 1) {
				scrollY = 113;
			}

			blit(transform, x + SCROLL_BAR_START_X, y + SCROLL_BAR_TOP_POS_Y + scrollY, 0, 0.0F, 199.0F, SCROLLER_WIDTH, SCROLLER_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		} else {
			blit(transform, x + SCROLL_BAR_START_X, y + SCROLL_BAR_TOP_POS_Y, 0, 6.0F, 199.0F, SCROLLER_WIDTH, SCROLLER_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		}

	}

	@Override
	public void render(@NotNull PoseStack transform, int x, int y, float partialTicks) {
		this.renderBackground(transform);
		super.render(transform, x, y, partialTicks);
		MerchantOffers merchantoffers = this.menu.getOffers();
		if (!merchantoffers.isEmpty()) {
			int left = (this.width - this.imageWidth) / 2;
			int top = (this.height - this.imageHeight) / 2;
			int merchantY = top + PROGRESS_BAR_Y + 1;
			int costAX = left + TRADE_BUTTON_X + SELL_ITEM_1_X;
			RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
			this.renderScroller(transform, left, top, merchantoffers);
			int index = 0;

			for(MerchantOffer merchantoffer : merchantoffers) {
				if (!this.canScroll(merchantoffers.size()) || index >= this.scrollOff && index < 7 + this.scrollOff) {
					ItemStack baseCostA = merchantoffer.getBaseCostA();
					ItemStack costA = merchantoffer.getCostA();
					ItemStack costB = merchantoffer.getCostB();
					ItemStack result = merchantoffer.getResult();
					transform.pushPose();
					transform.translate(0.0F, 0.0F, 100.0F);
					int itemY = merchantY + 2;
					this.renderAndDecorateCostA(transform, costA, baseCostA, costAX, itemY);
					if (!costB.isEmpty()) {
						this.itemRenderer.renderAndDecorateFakeItem(transform, costB, left + SELL_ITEM_1_X + SELL_ITEM_2_X, itemY);
						this.itemRenderer.renderGuiItemDecorations(transform, this.font, costB, left + SELL_ITEM_1_X + SELL_ITEM_2_X, itemY);
					}

					this.renderButtonArrows(transform, merchantoffer, left, itemY);
					this.itemRenderer.renderAndDecorateFakeItem(transform, result, left + SELL_ITEM_1_X + BUY_ITEM_X, itemY);
					this.itemRenderer.renderGuiItemDecorations(transform, this.font, result, left + SELL_ITEM_1_X + BUY_ITEM_X, itemY);
					transform.popPose();
					merchantY += TRADE_BUTTON_HEIGHT;
				}
				++index;
			}

			MerchantOffer currentOffer = merchantoffers.get(this.shopItem);
			if (this.menu.showProgressBar()) {
				this.renderProgressBar(transform, left, top);
			}

			if (currentOffer.isOutOfStock() && this.isHovering(186, 35, 22, 21, x, y) && this.menu.canRestock()) {
				this.renderTooltip(transform, DEPRECATED_TOOLTIP, x, y);
			}

			for(TradeOfferButton tradeOfferButton : this.tradeOfferButtons) {
				if (tradeOfferButton.isHoveredOrFocused()) {
					tradeOfferButton.renderToolTip(transform, x, y);
				}

				tradeOfferButton.visible = tradeOfferButton.index < this.menu.getOffers().size();
			}

			RenderSystem.enableDepthTest();
		}

		this.renderTooltip(transform, x, y);
	}

	private void renderButtonArrows(PoseStack transform, MerchantOffer offer, int x, int y) {
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
		if (offer.isOutOfStock()) {
			blit(transform, x + SELL_ITEM_1_X + SELL_ITEM_2_X + 20, y + 3, 0, 25.0F, 171.0F, 10, 9, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		} else {
			blit(transform, x + SELL_ITEM_1_X + SELL_ITEM_2_X + 20, y + 3, 0, 15.0F, 171.0F, 10, 9, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		}
	}

	private void renderAndDecorateCostA(PoseStack transform, ItemStack baseCostA, ItemStack costA, int x, int y) {
		this.itemRenderer.renderAndDecorateFakeItem(transform, baseCostA, x, y);
		if (costA.getCount() == baseCostA.getCount()) {
			this.itemRenderer.renderGuiItemDecorations(transform, this.font, baseCostA, x, y);
		} else {
			this.itemRenderer.renderGuiItemDecorations(transform, this.font, costA, x, y, costA.getCount() == 1 ? "1" : null);
			PoseStack posestack = new PoseStack();
			posestack.translate(0.0D, 0.0D, 200.0F);
			net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource = net.minecraft.client.renderer.MultiBufferSource.immediate(com.mojang.blaze3d.vertex.Tesselator.getInstance().getBuilder());
			this.font.drawInBatch(
					String.valueOf(baseCostA.getCount()),
					(x + 14) + 19 - 2 - this.font.width(String.valueOf(baseCostA.getCount())), y + LABEL_Y + 3,
					0xFFFFFF, true, posestack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880
			);
			bufferSource.endBatch();
			RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
			transform.pushPose();
			transform.translate(0.0F, 0.0F, 300.0F);
			blit(transform, x + 7, y + 12, 0, 0.0F, 176.0F, 9, 2, 512, 256);
			transform.popPose();
		}

	}

	private boolean canScroll(int size) {
		return size > NUMBER_OF_OFFER_BUTTONS;
	}

	@Override
	public boolean mouseScrolled(double x, double y, double delta) {
		int size = this.menu.getOffers().size();
		if (this.canScroll(size)) {
			int overSize = size - NUMBER_OF_OFFER_BUTTONS;
			this.scrollOff = Mth.clamp((int)((double)this.scrollOff - delta), 0, overSize);
		}

		return true;
	}

	@Override
	public boolean mouseDragged(double fromX, double fromY, int activeButton, double toX, double toY) {
		int size = this.menu.getOffers().size();
		if (this.isDragging) {
			int scrollTop = this.topPos + SCROLL_BAR_TOP_POS_Y;
			int scrollBottom = scrollTop + SCROLL_BAR_HEIGHT;
			int overSize = size - NUMBER_OF_OFFER_BUTTONS;
			float scrollIndex = ((float)fromY - (float)scrollTop - 13.5F) / ((float)(scrollBottom - scrollTop) - 27.0F);
			scrollIndex = scrollIndex * (float)overSize + 0.5F;
			this.scrollOff = Mth.clamp((int)scrollIndex, 0, overSize);
			return true;
		}
		return super.mouseDragged(fromX, fromY, activeButton, toX, toY);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		this.isDragging = false;
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		if (this.canScroll(this.menu.getOffers().size()) &&
				x > (double)(left + SCROLL_BAR_START_X) && x < (double)(left + SCROLL_BAR_START_X + SCROLLER_WIDTH) &&
				y > (double)(top + SCROLL_BAR_TOP_POS_Y) && y <= (double)(top + SCROLL_BAR_TOP_POS_Y + SCROLL_BAR_HEIGHT + 1)) {
			this.isDragging = true;
		}

		return super.mouseClicked(x, y, mouseButton);
	}

	@OnlyIn(Dist.CLIENT)
	class TradeOfferButton extends Button {
		final int index;

		public TradeOfferButton(int x, int y, int index, Button.OnPress onPress) {
			super(x, y, TRADE_BUTTON_WIDTH, TRADE_BUTTON_HEIGHT, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
			this.index = index;
			this.visible = false;
		}

		public int getIndex() {
			return this.index;
		}

		public void renderToolTip(@NotNull PoseStack transform, int x, int y) {
			if (this.isHovered && PiglinCuteyMerchantScreen.this.menu.getOffers().size() > this.index + PiglinCuteyMerchantScreen.this.scrollOff) {
				if (x < this.getX() + 20) {
					ItemStack itemstack = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getCostA();
					PiglinCuteyMerchantScreen.this.renderTooltip(transform, itemstack, x, y);
				} else if (x < this.getX() + 50 && x > this.getX() + 30) {
					ItemStack itemstack2 = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getCostB();
					if (!itemstack2.isEmpty()) {
						PiglinCuteyMerchantScreen.this.renderTooltip(transform, itemstack2, x, y);
					}
				} else if (x > this.getX() + 65) {
					ItemStack itemstack1 = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getResult();
					PiglinCuteyMerchantScreen.this.renderTooltip(transform, itemstack1, x, y);
				}
			}
		}
	}
}
