package com.hexagram2021.emeraldcraft.common.items.capabilities;

import com.google.common.collect.Lists;
import com.hexagram2021.emeraldcraft.api.events.FarciFoodComputeNutritionEvent;
import com.hexagram2021.emeraldcraft.common.items.foods.AbstractMincedMeatItem;
import com.hexagram2021.emeraldcraft.common.items.foods.FarciFoodItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

//Read-only capability.
public class FarciFoodStorage implements IFoodStorage, INBTSerializable<ListTag> {
	private final List<Item> fillingFoods;
	private final FarciFoodItem item;
	private FoodProperties foodProperties;

	public static final String TAG_FILLINGS = "emeraldcraft:fillings";

	@SuppressWarnings("deprecation")
	private static FoodProperties buildFoodProperties(List<Item> fillingFoods, boolean cooked, int nutrition, float saturationModifier,
													  List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
		List<Pair<Supplier<MobEffectInstance>, Float>> newEffects = Lists.newArrayList();
		for(Item item: fillingFoods) {
			if(item != null) {
				int nutritionAdder = 0;
				FoodProperties foodProperties = item.getFoodProperties();
				if(foodProperties != null) {
					nutritionAdder = (foodProperties.getNutrition() + 1) / 2;
					foodProperties.getEffects().forEach(pair -> newEffects.add(Pair.of(pair::getFirst, pair.getSecond())));
				} else if(item.builtInRegistryHolder().is(Tags.Items.MUSHROOMS)) {
					nutritionAdder = 1;
				} else if(item.builtInRegistryHolder().is(Tags.Items.EGGS)) {
					nutritionAdder = cooked ? 2 : 1;
				} else if(item instanceof AbstractMincedMeatItem mincedMeatItem) {
					FoodProperties meatFoodProperties = (cooked ? mincedMeatItem.cookedMeatItem() : mincedMeatItem.rawMeatItem()).getFoodProperties();
					if(meatFoodProperties != null) {
						nutritionAdder = (meatFoodProperties.getNutrition() + 1) / 2;
						meatFoodProperties.getEffects().forEach(pair -> newEffects.add(Pair.of(pair::getFirst, pair.getSecond())));
					}
				}
				FarciFoodComputeNutritionEvent event = new FarciFoodComputeNutritionEvent(item, nutritionAdder, newEffects);
				MinecraftForge.EVENT_BUS.post(event);
				nutrition += event.getNutritionAdder();
			}
		}
		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationModifier);
		effects.forEach(pair -> builder.effect(pair.getFirst(), pair.getSecond()));
		newEffects.forEach(pair -> builder.effect(pair.getFirst(), pair.getSecond()));

		return builder.build();
	}

	private static List<Item> buildFillingFoods(@Nullable CompoundTag nbt) {
		List<Item> fillingFoods = Lists.newArrayList();
		if(nbt != null && nbt.contains(TAG_FILLINGS, Tag.TAG_LIST)) {
			for(Tag tag: nbt.getList(TAG_FILLINGS, Tag.TAG_STRING)) {
				fillingFoods.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getAsString())));
			}
		}
		return fillingFoods;
	}

	public FarciFoodStorage(List<Item> fillingFoods, FarciFoodItem item, FoodProperties foodProperties) {
		this.fillingFoods = fillingFoods;
		this.item = item;
		this.foodProperties = foodProperties;
	}
	public FarciFoodStorage(ItemStack self, FarciFoodItem item) {
		this(buildFillingFoods(self.getTag()), item);
	}
	public FarciFoodStorage(List<Item> fillingFoods, FarciFoodItem item) {
		this(fillingFoods, item, buildFoodProperties(
				fillingFoods,
				item.isCooked(), item.getNutrition(), item.getSaturationModifier(),
				item.getEffects()
		));
	}

	@Override
	public FoodProperties foodProperties() {
		return this.foodProperties;
	}

	@Override
	public void setFoodTag(ListTag listTag) {
		List<Item> fillingFoods = Lists.newArrayList();
		for(Tag tag: listTag) {
			fillingFoods.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getAsString())));
		}
		this.foodProperties = buildFoodProperties(
				fillingFoods, this.item.isCooked(),
				this.item.getNutrition(), this.item.getSaturationModifier(), this.item.getEffects()
		);
	}

	@Override
	public ListTag serializeNBT() {
		ListTag listTag = new ListTag();
		this.fillingFoods.forEach(item -> listTag.add(StringTag.valueOf(getRegistryName(item).toString())));
		return listTag;
	}

	@Override
	public void deserializeNBT(ListTag tag) {
		this.setFoodTag(tag);
	}
}
