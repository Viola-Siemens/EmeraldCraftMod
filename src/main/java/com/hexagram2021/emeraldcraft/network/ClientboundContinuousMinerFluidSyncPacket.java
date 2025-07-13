package com.hexagram2021.emeraldcraft.network;

import cn.sh1rocu.emeraldcraft.util.fluid.FluidStack;
import com.hexagram2021.emeraldcraft.EmeraldCraft;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundContinuousMinerFluidSyncPacket extends ClientboundFluidSyncPacket {
    public static final PacketType<ClientboundContinuousMinerFluidSyncPacket> TYPE = PacketType.create(EmeraldCraft.id("s2c_continuous_miner_fluid_sync"), ClientboundContinuousMinerFluidSyncPacket::new);

    public ClientboundContinuousMinerFluidSyncPacket(String container, List<FluidStack> fluidStacks) {
        super(container, fluidStacks);
    }

    public ClientboundContinuousMinerFluidSyncPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
