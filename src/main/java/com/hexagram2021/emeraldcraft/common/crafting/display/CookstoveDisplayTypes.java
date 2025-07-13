package com.hexagram2021.emeraldcraft.common.crafting.display;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static com.hexagram2021.emeraldcraft.EmeraldCraft.MODID;

public final class CookstoveDisplayTypes {
    public static final ICookstoveDisplayType ITEMS = register("items",
            new ICookstoveDisplayType() {
                @Override
                public Codec<? extends ICookstoveDisplay> codec() {
                    return CookstoveItemsDisplay.CODEC;
                }

                @Override
                public ICookstoveDisplay fromNetwork(FriendlyByteBuf buf) {
                    return CookstoveItemsDisplay.fromNetwork(buf);
                }
            });
    public static final ICookstoveDisplayType BLOCK = register("block",
            new ICookstoveDisplayType() {
                @Override
                public Codec<? extends ICookstoveDisplay> codec() {
                    return CookstoveBlockDisplay.CODEC;
                }

                @Override
                public ICookstoveDisplay fromNetwork(FriendlyByteBuf buf) {
                    return CookstoveBlockDisplay.fromNetwork(buf);
                }
            });

    private CookstoveDisplayTypes() {
    }

    public static void init() {
    }

    private static ICookstoveDisplayType register(String name, ICookstoveDisplayType type) {
        ResourceLocation id = new ResourceLocation(MODID, name);
        ICookstoveDisplayType.registerCookstoveDisplayType(id, type);
        return type;
    }
}
