package com.hexagram2021.emeraldcraft.client.screens;

import com.hexagram2021.emeraldcraft.common.crafting.menu.GlassKilnMenu;
import com.hexagram2021.emeraldcraft.common.crafting.recipebook.GlassKilnRecipeBookComponent;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@OnlyIn(Dist.CLIENT)
public class GlassKilnScreen extends AbstractFurnaceScreen<GlassKilnMenu> {
	private static final ResourceLocation LIT_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/glass_kiln/lit_progress");
	private static final ResourceLocation BURN_PROGRESS_SPRITE = new ResourceLocation(MODID, "container/glass_kiln/burn_progress");
	private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/container/glass_kiln.png");

	public GlassKilnScreen(GlassKilnMenu glassKilnMenu, Inventory inventory, Component component) {
		super(glassKilnMenu, new GlassKilnRecipeBookComponent(), inventory, component, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
	}
}
