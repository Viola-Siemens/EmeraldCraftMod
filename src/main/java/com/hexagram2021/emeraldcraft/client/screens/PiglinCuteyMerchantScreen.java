package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.PiglinCuteyMerchantMenu;
import com.hexagram2021.emeraldcraft.common.entities.mobs.PiglinCuteyData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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

@OnlyIn(Dist.CLIENT)
public class PiglinCuteyMerchantScreen extends AbstractContainerScreen<PiglinCuteyMerchantMenu> {
	private static final ResourceLocation OUT_OF_STOCK_SPRITE = new ResourceLocation("container/villager/out_of_stock");
	private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = new ResourceLocation("container/villager/experience_bar_background");
	private static final ResourceLocation EXPERIENCE_BAR_CURRENT_SPRITE = new ResourceLocation("container/villager/experience_bar_current");
	private static final ResourceLocation EXPERIENCE_BAR_RESULT_SPRITE = new ResourceLocation("container/villager/experience_bar_result");
	private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("container/villager/scroller");
	private static final ResourceLocation SCROLLER_DISABLED_SPRITE = new ResourceLocation("container/villager/scroller_disabled");
	private static final ResourceLocation TRADE_ARROW_OUT_OF_STOCK_SPRITE = new ResourceLocation("container/villager/trade_arrow_out_of_stock");
	private static final ResourceLocation TRADE_ARROW_SPRITE = new ResourceLocation("container/villager/trade_arrow");
	private static final ResourceLocation DISCOUNT_STRIKETHRUOGH_SPRITE = new ResourceLocation("container/villager/discount_strikethrough");
	private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager.png");
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

	@SuppressWarnings("DataFlowIssue")
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
	protected void renderLabels(GuiGraphics transform, int x, int y) {
		int level = this.menu.getTraderLevel();
		if (level > 0 && level <= 5 && this.menu.showProgressBar()) {
			Component component = this.title.copy().append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + level));
			int fontWidth = this.font.width(component);
			int k = 49 + this.imageWidth / 2 - fontWidth / 2;
			transform.drawString(this.font, component, k, 6, 0x404040, false);
		} else {
			transform.drawString(this.font, this.title, 49 + this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 0x404040, false);
		}

		transform.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
		int l = this.font.width(TRADES_LABEL);
		transform.drawString(this.font, TRADES_LABEL, 5 - l / 2 + 48, 6, 0x404040, false);
	}

	@Override
	protected void renderBg(GuiGraphics transform, float partialTicks, int x, int y) {
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		transform.blit(VILLAGER_LOCATION, left, top, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		MerchantOffers merchantoffers = this.menu.getOffers();
		if (!merchantoffers.isEmpty()) {
			int k = this.shopItem;
			if (k < 0 || k >= merchantoffers.size()) {
				return;
			}

			MerchantOffer merchantoffer = merchantoffers.get(k);
			if (merchantoffer.isOutOfStock()) {
				transform.blitSprite(OUT_OF_STOCK_SPRITE, this.leftPos + 83 + MERCHANT_MENU_PART_X, this.topPos + 35, 0, 28, 21);
			}
		}

	}

	private void renderProgressBar(GuiGraphics transform, int x, int y) {
		int level = this.menu.getTraderLevel();
		int xp = this.menu.getTraderXp();
		if (level < 5) {
			transform.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y, 0, 102, 5);
			int k = PiglinCuteyData.getMinXpPerLevel(level);
			if (xp >= k && PiglinCuteyData.canLevelUp(level)) {
				float f = 102.0F / (float)(PiglinCuteyData.getMaxXpPerLevel(level) - k);
				int progress = Math.min(Mth.floor(f * (float)(xp - k)), 102);
				transform.blitSprite(EXPERIENCE_BAR_CURRENT_SPRITE, 102, 5, 0, 0, x + PROGRESS_BAR_X, y + PROGRESS_BAR_Y, 0, progress, 5);
				int addXp = this.menu.getFutureTraderXp();
				if (addXp > 0) {
					int addProgress = Math.min(Mth.floor((float)addXp * f), 102 - progress);
					transform.blitSprite(EXPERIENCE_BAR_RESULT_SPRITE, 102, 5, 0, 0, x + PROGRESS_BAR_X + progress, y + PROGRESS_BAR_Y, 0, addProgress, 3);
				}

			}
		}
	}

	private void renderScroller(GuiGraphics transform, int x, int y, MerchantOffers offers) {
		int overSize = offers.size() + 1 - NUMBER_OF_OFFER_BUTTONS;
		if (overSize > 1) {
			int overHeight = SCROLL_BAR_HEIGHT - (27 + (overSize - 1) * SCROLL_BAR_HEIGHT / overSize);
			int index = 1 + overHeight / overSize + SCROLL_BAR_HEIGHT / overSize;
			int scrollY = Math.min(113, this.scrollOff * index);
			if (this.scrollOff == overSize - 1) {
				scrollY = 113;
			}

			transform.blitSprite(SCROLLER_SPRITE, x + SCROLL_BAR_START_X, y + SCROLL_BAR_TOP_POS_Y + scrollY, 0, SCROLLER_WIDTH, SCROLLER_HEIGHT);
		} else {
			transform.blitSprite(SCROLLER_DISABLED_SPRITE, x + SCROLL_BAR_START_X, y + SCROLL_BAR_TOP_POS_Y, 0, SCROLLER_WIDTH, SCROLLER_HEIGHT);
		}

	}

	@Override
	public void render(GuiGraphics transform, int x, int y, float partialTicks) {
		super.render(transform, x, y, partialTicks);
		MerchantOffers merchantoffers = this.menu.getOffers();
		if (!merchantoffers.isEmpty()) {
			int left = (this.width - this.imageWidth) / 2;
			int top = (this.height - this.imageHeight) / 2;
			int merchantY = top + PROGRESS_BAR_Y + 1;
			int costAX = left + TRADE_BUTTON_X + SELL_ITEM_1_X;
			this.renderScroller(transform, left, top, merchantoffers);
			int index = 0;

			for(MerchantOffer merchantoffer : merchantoffers) {
				if (!this.canScroll(merchantoffers.size()) || index >= this.scrollOff && index < 7 + this.scrollOff) {
					ItemStack baseCostA = merchantoffer.getBaseCostA();
					ItemStack costA = merchantoffer.getCostA();
					ItemStack costB = merchantoffer.getCostB();
					ItemStack result = merchantoffer.getResult();
					transform.pose().pushPose();
					transform.pose().translate(0.0F, 0.0F, 100.0F);
					int itemY = merchantY + 2;
					this.renderAndDecorateCostA(transform, costA, baseCostA, costAX, itemY);
					if (!costB.isEmpty()) {
						transform.renderFakeItem(costB, left + SELL_ITEM_1_X + SELL_ITEM_2_X, itemY);
						transform.renderItemDecorations(this.font, costB, left + SELL_ITEM_1_X + SELL_ITEM_2_X, itemY);
					}

					this.renderButtonArrows(transform, merchantoffer, left, itemY);
					transform.renderFakeItem(result, left + SELL_ITEM_1_X + BUY_ITEM_X, itemY);
					transform.renderItemDecorations(this.font, result, left + SELL_ITEM_1_X + BUY_ITEM_X, itemY);
					transform.pose().popPose();
					merchantY += TRADE_BUTTON_HEIGHT;
				}
				++index;
			}

			MerchantOffer currentOffer = merchantoffers.get(this.shopItem);
			if (this.menu.showProgressBar()) {
				this.renderProgressBar(transform, left, top);
			}

			if (currentOffer.isOutOfStock() && this.isHovering(186, 35, 22, 21, x, y) && this.menu.canRestock()) {
				transform.renderTooltip(this.font, DEPRECATED_TOOLTIP, x, y);
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

	private void renderButtonArrows(GuiGraphics transform, MerchantOffer offer, int x, int y) {
		RenderSystem.enableBlend();
		if (offer.isOutOfStock()) {
			transform.blitSprite(TRADE_ARROW_OUT_OF_STOCK_SPRITE, x + SELL_ITEM_1_X + SELL_ITEM_2_X + 20, y + 3, 0, 10, 9);
		} else {
			transform.blitSprite(TRADE_ARROW_SPRITE, x + SELL_ITEM_1_X + SELL_ITEM_2_X + 20, y + 3, 0, 10, 9);
		}
	}

	private void renderAndDecorateCostA(GuiGraphics transform, ItemStack baseCostA, ItemStack costA, int x, int y) {
		transform.renderFakeItem(baseCostA, x, y);
		if (costA.getCount() == baseCostA.getCount()) {
			transform.renderItemDecorations(this.font, baseCostA, x, y);
		} else {
			transform.renderItemDecorations(this.font, costA, x, y, costA.getCount() == 1 ? "1" : null);
			transform.pose().pushPose();
			transform.pose().translate(0.0F, 0.0F, 200.0F);
			String count = baseCostA.getCount() == 1 ? "1" : String.valueOf(baseCostA.getCount());
			this.font.drawInBatch(
					count, (float)(x + 14) + 19.0F - 2.0F - (float)this.font.width(count),
					(float)(y + LABEL_Y) + 3.0F, 0xFFFFFF, true,
					transform.pose().last().pose(), transform.bufferSource(),
					Font.DisplayMode.NORMAL, 0, 15728880, false
			);
			transform.pose().popPose();
			transform.pose().pushPose();
			transform.pose().translate(0.0F, 0.0F, 300.0F);
			transform.blitSprite(DISCOUNT_STRIKETHRUOGH_SPRITE, x + 7, y + 12, 0, 9, 2);
			transform.pose().popPose();
		}
	}

	private boolean canScroll(int size) {
		return size > NUMBER_OF_OFFER_BUTTONS;
	}

	@Override
	public boolean mouseScrolled(double x, double y, double deltaX, double deltaY) {
		int size = this.menu.getOffers().size();
		if (this.canScroll(size)) {
			int overSize = size - NUMBER_OF_OFFER_BUTTONS;
			this.scrollOff = Mth.clamp((int)((double)this.scrollOff - deltaY), 0, overSize);
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

		public void renderToolTip(GuiGraphics transform, int x, int y) {
			if (this.isHovered && PiglinCuteyMerchantScreen.this.menu.getOffers().size() > this.index + PiglinCuteyMerchantScreen.this.scrollOff) {
				if (x < this.getX() + 20) {
					ItemStack itemstack = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getCostA();
					transform.renderTooltip(PiglinCuteyMerchantScreen.this.font, itemstack, x, y);
				} else if (x < this.getX() + 50 && x > this.getX() + 30) {
					ItemStack itemstack2 = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getCostB();
					if (!itemstack2.isEmpty()) {
						transform.renderTooltip(PiglinCuteyMerchantScreen.this.font, itemstack2, x, y);
					}
				} else if (x > this.getX() + 65) {
					ItemStack itemstack1 = PiglinCuteyMerchantScreen.this.menu.getOffers().get(this.index + PiglinCuteyMerchantScreen.this.scrollOff).getResult();
					transform.renderTooltip(PiglinCuteyMerchantScreen.this.font, itemstack1, x, y);
				}
			}
		}
	}
}
