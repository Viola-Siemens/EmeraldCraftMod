package cn.sh1rocu.emeraldcraft.util.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidStack {
    public static final FluidStack EMPTY = new FluidStack(FluidVariant.blank(), 0);
    public static final Codec<FluidStack> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                    BuiltInRegistries.FLUID.byNameCodec().fieldOf("FluidName").forGetter(FluidStack::getFluid),
                    Codec.LONG.fieldOf("Amount").forGetter(FluidStack::getAmount),
                    CompoundTag.CODEC.optionalFieldOf("Tag").forGetter((stack) -> Optional.ofNullable(stack.getFluidVariant().getNbt()))
            ).apply(instance, (fluid, amount, tag) -> {
                FluidStack stack = new FluidStack(FluidVariant.of(fluid), amount);
                Objects.requireNonNull(stack);
                tag.ifPresent(nbt -> stack.setFluidVariant(FluidVariant.of(fluid, tag.get())));
                return stack;
            }));
    private FluidVariant fluidVariant;
    private long amount;

    public FluidStack(FluidVariant fluidVariant, long amount) {
        this.fluidVariant = fluidVariant;
        this.amount = amount;
    }

    public Fluid getFluid() {
        return fluidVariant.getFluid();
    }

    public FluidVariant getFluidVariant() {
        return fluidVariant;
    }

    public void setFluidVariant(FluidVariant fluidVariant) {
        this.fluidVariant = fluidVariant;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public FluidStack copy() {
        return new FluidStack(fluidVariant, amount);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.fluidVariant.isBlank() || this.amount <= 0;
    }

    public void shrink(long amount) {
        this.amount -= amount;
    }

    public void grow(long amount) {
        this.amount += amount;
    }

    public boolean isFluidEqual(@NotNull FluidStack other) {
        return this.fluidVariant.getFluid() == other.fluidVariant.getFluid();
    }

    public boolean containsFluid(@NotNull FluidStack other) {
        return this.isFluidEqual(other) && this.amount >= other.amount;
    }

    public static long convertDropletsToMb(long droplets) {
        return (droplets / 81);
    }

    public static long convertMbToDroplets(long mb) {
        return mb * 81;
    }

    public static FluidStack readFromPacket(FriendlyByteBuf buf) {
        return new FluidStack(FluidVariant.fromPacket(buf), buf.readVarLong());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        this.fluidVariant.toPacket(buf);
        buf.writeVarLong(amount);
    }
}