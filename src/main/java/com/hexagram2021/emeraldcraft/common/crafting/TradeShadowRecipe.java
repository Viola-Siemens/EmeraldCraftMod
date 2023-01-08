package com.hexagram2021.emeraldcraft.common.crafting;

import com.google.common.collect.Maps;
import com.hexagram2021.emeraldcraft.common.crafting.cache.CachedRecipeList;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import com.hexagram2021.emeraldcraft.common.register.ECRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TradeShadowRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final ItemStack costA;
	protected final ItemStack costB;
	protected final ItemStack result;
	protected final VillagerProfession profession;
	protected final int villagerLevel;
	protected final int xp;

	protected static final Map<VillagerProfession, Map<Integer, Villager>> LAZY_RENDER_VILLAGERS = Maps.newHashMap();

	public static final CachedRecipeList<TradeShadowRecipe> recipeList = new CachedRecipeList<>(
			ECRecipes.TRADE_SHADOW_TYPE,
			TradeShadowRecipe.class
	);

	public TradeShadowRecipe(ResourceLocation id, ItemStack costA, ItemStack costB, ItemStack result, VillagerProfession profession, int villagerLevel, int xp) {
		this.id = id;
		this.costA = costA;
		this.costB = costB;
		this.result = result;
		this.profession = profession;
		this.villagerLevel = villagerLevel;
		this.xp = xp;
	}

	@Override
	public boolean matches(Container container, @NotNull Level level) {
		return this.costA.sameItem(container.getItem(0)) && this.costB.sameItem(container.getItem(1));
	}

	@Override @NotNull
	public ItemStack assemble(@NotNull Container container) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return false;
	}

	@Override @NotNull
	public ItemStack getResultItem() {
		return this.result;
	}

	@Override @NotNull
	public ResourceLocation getId() {
		return this.id;
	}

	@Override @NotNull
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.TRADE_SHADOW_SERIALIZER.get();
	}

	@Override @NotNull
	public RecipeType<?> getType() {
		return ECRecipes.TRADE_SHADOW_TYPE.get();
	}

	public ItemStack getCostA() {
		return this.costA;
	}

	public ItemStack getCostB() {
		return this.costB;
	}

	public VillagerProfession getProfession() {
		return this.profession;
	}

	public int getVillagerLevel() {
		return this.villagerLevel;
	}

	public int getXp() {
		return this.xp;
	}

	@OnlyIn(Dist.CLIENT)
	public Villager getRenderVillager() {
		assert Minecraft.getInstance().level != null;
		return LAZY_RENDER_VILLAGERS.computeIfAbsent(this.profession, profession1 -> Maps.newHashMap())
				.computeIfAbsent(this.villagerLevel, villagerLevel1 -> {
					Villager villager = EntityType.VILLAGER.create(Minecraft.getInstance().level);
					assert villager != null;
					villager.setNoAi(true);
					villager.setVillagerData(new VillagerData(VillagerType.PLAINS, this.profession, this.villagerLevel));
					return villager;
				});
	}
}