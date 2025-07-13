package com.hexagram2021.emeraldcraft.network;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundMelterFluidSyncPacket extends ClientboundFluidSyncPacket {
    public static final PacketType<ClientboundMelterFluidSyncPacket> TYPE = PacketType.create(EmeraldCraft.id("s2c_melter_fluid_sync"), ClientboundMelterFluidSyncPacket::new);

    public ClientboundMelterFluidSyncPacket(String containerType, List<FluidStack> fluidStacks) {
        super(containerType, fluidStacks);
    }

    public ClientboundMelterFluidSyncPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
