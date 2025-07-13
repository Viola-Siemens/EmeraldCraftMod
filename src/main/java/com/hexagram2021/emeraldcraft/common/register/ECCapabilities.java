package com.hexagram2021.emeraldcraft.common.register;

import com.hexagram2021.emeraldcraft.common.items.capabilities.ItemStackFoodHandler;
import com.hexagram2021.emeraldcraft.common.items.foods.FarciFoodItem;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

@SuppressWarnings("UnstableApiUsage")
public final class ECCapabilities implements ItemComponentInitializer {
    public static final ResourceLocation FOOD_CAPABILITY_ID = new ResourceLocation(MODID, "food_storage");

    public static final ComponentKey<ItemStackFoodHandler> FOOD_CAPABILITY = ComponentRegistry.getOrCreate(FOOD_CAPABILITY_ID, ItemStackFoodHandler.class);

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof FarciFoodItem, FOOD_CAPABILITY, ItemStackFoodHandler::new);
    }
}
