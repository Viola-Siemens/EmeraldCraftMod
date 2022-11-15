package com.hexagram2021.emeraldcraft.mixin;

import com.google.common.collect.ImmutableMap;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeBookTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeBookSettings.class)
public class RecipeBookStatusMixin {
	@Final
	@Shadow
	@Mutable
	private static Map<RecipeBookType, Pair<String, String>> TAG_FIELDS;
	
	@Inject(method = "<clinit>()V", at = @At(value = "FIELD", shift = At.Shift.AFTER, target = "Lnet/minecraft/stats/RecipeBookSettings;TAG_FIELDS:Ljava/util/Map;"))
	private static void injectTags(CallbackInfo ci) {
		TAG_FIELDS = new ImmutableMap.Builder<RecipeBookType, Pair<String, String>>()
				.putAll(TAG_FIELDS)
				.put(ECRecipeBookTypes.GLASS_KILN, Pair.of("isGlassKilnGuiOpen", "isGlassKilnFilteringCraftable"))
				.build();
	}
}
