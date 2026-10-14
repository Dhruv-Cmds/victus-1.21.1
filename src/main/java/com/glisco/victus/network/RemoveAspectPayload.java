package com.glisco.victus.network;

import com.glisco.victus.Victus;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record RemoveAspectPayload() implements CustomPayload {

    public static final Id<RemoveAspectPayload> ID = new Id<>(Victus.id("remove_first_aspect"));

    public static final PacketCodec<RegistryByteBuf, RemoveAspectPayload> CODEC = PacketCodec.unit(new RemoveAspectPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
