package com.glisco.victus.network;

import com.glisco.victus.Victus;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AspectBrokenPayload(int index, boolean callHandler) implements CustomPayload {

    public static final Id<AspectBrokenPayload> ID = new Id<>(Victus.id("aspect_broken"));

    public static final PacketCodec<RegistryByteBuf, AspectBrokenPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, AspectBrokenPayload::index,
            PacketCodecs.BOOL, AspectBrokenPayload::callHandler,
            AspectBrokenPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
