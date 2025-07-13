package com.hexagram2021.emeraldcraft;

import com.hexagram2021.emeraldcraft.client.ClientEntityEventSubscriber;
import com.hexagram2021.emeraldcraft.client.ClientProxy;
import net.fabricmc.api.ClientModInitializer;

public class EmeraldCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientProxy.modConstruction();
        ClientEntityEventSubscriber.onRegisterLayers();
        ClientEntityEventSubscriber.onRegisterRenderer();
    }
}
