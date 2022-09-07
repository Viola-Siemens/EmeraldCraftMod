package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeBookStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeBookStatus.class)
public class RecipeBookStatusMixin {
	@Final
	@Shadow
	@Mutable
	private static Map<RecipeBookCategory, Pair<String, String>> TAG_FIELDS;

	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/item/crafting/RecipeBookStatus;TAG_FIELDS:Ljava/util/Map;"))
	private static void injectTags(CallbackInfo ci) {
		TAG_FIELDS = new ImmutableMap.Builder<RecipeBookCategory, Pair<String, String>>()
				.putAll(TAG_FIELDS)
				.put(ECRecipeBookTypes.GLASS_KILN, Pair.of("isGlassKilnGuiOpen", "isGlassKilnFilteringCraftable"))
				.build();
	}
}
