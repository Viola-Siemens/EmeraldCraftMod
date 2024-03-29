package com.hexagram2021.emeraldcraft.common.items.foods;

import com.hexagram2021.emeraldcraft.common.items.capabilities.DefaultFarciFoodStorageWrapper;
import com.hexagram2021.emeraldcraft.common.items.capabilities.FarciFoodStorage;
import com.hexagram2021.emeraldcraft.common.register.ECCapabilities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class FarciFoodItem extends Item {
	private final int nutrition;
	private final float saturationModifier;
	private final boolean cooked;
	private final List<Pair<Supplier<MobEffectInstance>, Float>> effects;
	private final DefaultFarciFoodStorageWrapper defaultStorage;

	public FarciFoodItem(Properties properties, int nutrition, float saturationModifier, boolean cooked, List<Pair<Supplier<MobEffectInstance>, Float>> effects) {
		super(properties);
		this.nutrition = nutrition;
		this.saturationModifier = saturationModifier;
		this.cooked = cooked;
		this.effects = effects;

		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationModifier);
		effects.forEach(pair -> builder.effect(pair.getFirst(), pair.getSecond()));
		this.defaultStorage = new DefaultFarciFoodStorageWrapper(builder.build());
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		if(nbt != null && nbt.contains(FarciFoodStorage.TAG_FILLINGS, Tag.TAG_LIST)) {
			stack.getCapability(ECCapabilities.FOOD_CAPABILITY).ifPresent(ifs -> ifs.setFoodTag(nbt.getList(FarciFoodStorage.TAG_FILLINGS, Tag.TAG_STRING)));
		}
		super.readShareTag(stack, nbt);
	}

	@Override
	public boolean isEdible() {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FoodProperties getFoodProperties() {
		return this.defaultStorage.foodProperties();
	}

	@Override
	public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		return stack.getCapability(ECCapabilities.FOOD_CAPABILITY).orElse(this.defaultStorage).foodProperties();
	}

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		super.appendHoverText(itemStack, level, components, flag);
		CompoundTag nbt = itemStack.getTag();
		if(nbt != null && nbt.contains(FarciFoodStorage.TAG_FILLINGS, Tag.TAG_LIST)) {
			ListTag listTag = nbt.getList(FarciFoodStorage.TAG_FILLINGS, Tag.TAG_STRING);
			if(!listTag.isEmpty()) {
				Item item = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(listTag.getString(0))));
				Component filling = Component.translatable(item.getDescriptionId());
				for(int i = 1; i < listTag.size(); ++i) {
					item = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(listTag.getString(i))));
					filling = Component.translatable("item.emeraldcraft.dumpling.filling_combination", filling, Component.translatable(item.getDescriptionId()));
				}
				components.add(Component.translatable("item.emeraldcraft.dumpling.filling", filling));
			}
		}
	}

	public int getNutrition() {
		return this.nutrition;
	}
	public float getSaturationModifier() {
		return this.saturationModifier;
	}
	public boolean isCooked() {
		return this.cooked;
	}
	public List<Pair<Supplier<MobEffectInstance>, Float>> getEffects() {
		return this.effects;
	}
}
