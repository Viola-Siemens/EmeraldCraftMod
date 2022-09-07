package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.RecipeBookCategory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes.*;

@Mixin(RecipeBookCategories.class)
public class RecipeBookCategoriesMixin {
	RecipeBookCategoriesMixin(String name, int ord, ItemStack... icons) {
		throw new UnsupportedOperationException("Replaced by Mixin");
	}

	@Final
	@Shadow
	@Mutable
	private static RecipeBookCategories[] $VALUES;

	@Final
	@Shadow
	@Mutable
	public static Map<RecipeBookCategories, List<RecipeBookCategories>> AGGREGATE_CATEGORIES;

	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/util/RecipeBookCategories;$VALUES:[Lnet/minecraft/client/util/RecipeBookCategories;"))
	private static void injectEnum(CallbackInfo ci) {
		int ordinal = $VALUES.length;
		$VALUES = Arrays.copyOf($VALUES, ordinal + 4);
		GLASS_KILN_SEARCH = $VALUES[ordinal] = (RecipeBookCategories)(Object)new RecipeBookCategoriesMixin("GLASS_KILN_SEARCH", ordinal, new ItemStack(Items.COMPASS));
		GLASS_KILN_SAND = $VALUES[ordinal + 1] = (RecipeBookCategories)(Object)new RecipeBookCategoriesMixin("GLASS_KILN_SAND", ordinal + 1, new ItemStack(Items.SAND));
		GLASS_KILN_CLAY = $VALUES[ordinal + 2] = (RecipeBookCategories)(Object)new RecipeBookCategoriesMixin("GLASS_KILN_CLAY", ordinal + 2, new ItemStack(Items.CLAY));
		GLASS_KILN_TERRACOTTA = $VALUES[ordinal + 3] = (RecipeBookCategories)(Object)new RecipeBookCategoriesMixin("GLASS_KILN_TERRACOTTA", ordinal + 3, new ItemStack(Items.CYAN_GLAZED_TERRACOTTA), new ItemStack(Items.MAGENTA_GLAZED_TERRACOTTA));
	}

	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/util/RecipeBookCategories;AGGREGATE_CATEGORIES:Ljava/util/Map;"))
	private static void injectAggregateCategories(CallbackInfo ci) {
		AGGREGATE_CATEGORIES = new ImmutableMap.Builder<RecipeBookCategories, List<RecipeBookCategories>>()
				.putAll(AGGREGATE_CATEGORIES)
				.put(GLASS_KILN_SEARCH, ImmutableList.of(GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA))
				.build();
	}


	@Inject(method = "getCategories", at = @At(value = "HEAD"), cancellable = true)
	private static void checkCustomCategories(RecipeBookCategory category, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
		if(category == GLASS_KILN) {
			cir.setReturnValue(ImmutableList.of(GLASS_KILN_SEARCH, GLASS_KILN_SAND, GLASS_KILN_CLAY, GLASS_KILN_TERRACOTTA));
		}
	}
}
