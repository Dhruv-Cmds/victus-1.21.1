package com.glisco.victus.network;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.HeartAspect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class VictusPackets {

    /**
     * Must run on both sides (called from the common initializer)
     */
    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(AspectBrokenPayload.ID, AspectBrokenPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RemoveAspectPayload.ID, RemoveAspectPayload.CODEC);
    }

    public static void registerClientListeners() {
        ClientPlayNetworking.registerGlobalReceiver(AspectBrokenPayload.ID, (payload, context) -> {
            final var client = context.client();
            client.execute(HeartAspect.createBreakEvent(client, payload.index(), payload.callHandler()));
        });
    }

    public static void registerServerListeners() {
        ServerPlayNetworking.registerGlobalReceiver(RemoveAspectPayload.ID, (payload, context) -> {
            final var player = context.player();
            if (!player.isCreative()) return;
            final var removed = Victus.ASPECTS.get(player).removeAspect();
            if (removed != null) player.dropItem(removed);
        });
    }

    public static void sendAspectBreak(ServerPlayerEntity player, int index, boolean callHandler) {
        ServerPlayNetworking.send(player, new AspectBrokenPayload(index, callHandler));
    }

    public static void requestAspectRemoval() {
        ClientPlayNetworking.send(new RemoveAspectPayload());
    }
}
