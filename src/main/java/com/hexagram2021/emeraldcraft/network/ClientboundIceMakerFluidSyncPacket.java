package com.hexagram2021.emeraldcraft.network;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundIceMakerFluidSyncPacket extends ClientboundFluidSyncPacket {
    public static final PacketType<ClientboundIceMakerFluidSyncPacket> TYPE = PacketType.create(EmeraldCraft.id("s2c_ice_maker_fluid_sync"), ClientboundIceMakerFluidSyncPacket::new);

    public ClientboundIceMakerFluidSyncPacket(String containerType, List<FluidStack> fluidStacks) {
        super(containerType, fluidStacks);
    }

    public ClientboundIceMakerFluidSyncPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
