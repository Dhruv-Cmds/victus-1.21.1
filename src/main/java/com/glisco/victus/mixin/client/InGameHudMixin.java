package com.glisco.victus.mixin.client;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.HeartAspect;
import com.glisco.victus.hearts.HeartAspectComponent;
import com.glisco.victus.hearts.OverlaySpriteProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private int heartX, heartY, heartIndex;

    @Unique
    private HeartAspectComponent aspectComponent = null;

    @Inject(method = "renderHealthBar", at = @At("HEAD"))
    private void storeAspectComponent(
            DrawContext context,
            PlayerEntity player,
            int x,
            int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci
    ) {
        this.aspectComponent = Victus.ASPECTS.get(player);
    }

    @Inject(method = "renderHealthBar", at = @At("RETURN"))
    private void releaseAspectComponent(
            DrawContext context,
            PlayerEntity player,
            int x,
            int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci
    ) {
        this.aspectComponent = null;
    }

    @Inject(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void storeLocals(
            DrawContext context,
            PlayerEntity player,
            int x,
            int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci,
            InGameHud.HeartType heartType,
            boolean bl,
            int i,
            int j,
            int k,
            int l,
            int m,
            int n,
            int o,
            int p
    ) {
        this.heartX = o;
        this.heartY = p;
        this.heartIndex = l;
    }

    @Inject(method = "drawHeart", at = @At("TAIL"))
    private void renderRechargingOutline(
            DrawContext context,
            InGameHud.HeartType type,
            int x,
            int y,
            boolean hardcore,
            boolean blinking,
            boolean half,
            CallbackInfo ci
    ) {
        if (type != InGameHud.HeartType.CONTAINER) return;
        if (aspectComponent == null) return;
        if (!aspectComponent.recharging()) return;
        if (aspectComponent.getAspect(heartIndex) == null) return;

        context.drawTexture(
                HeartAspect.HEART_ATLAS_TEXTURE,
                heartX,
                heartY,
                55,
                55,
                9,
                9,
                64,
                64
        );
    }

    @Inject(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void renderOverlay(
            DrawContext context,
            PlayerEntity player,
            int x,
            int y,
            int lines,
            int regeneratingHeartIndex,
            float maxHealth,
            int lastHealth,
            int health,
            int absorption,
            boolean blinking,
            CallbackInfo ci
    ) {
        if (aspectComponent == null) return;

        final var aspect = aspectComponent.getAspect(heartIndex);
        if (aspect == null) return;

        RenderSystem.setShaderTexture(0, aspect.getAtlas());

        renderAspect(
                context,
                heartX,
                heartY,
                aspect.getTextureIndex(),
                aspect.getRechargeProgress()
        );

        if (aspect instanceof OverlaySpriteProvider spriteProvider
                && spriteProvider.shouldRenderOverlay()) {
            int color = spriteProvider.getOverlayTint();

            RenderSystem.setShaderColor(
                    getComponent(color, 16),
                    getComponent(color, 8),
                    getComponent(color, 0),
                    1
            );

            renderAspect(
                    context,
                    heartX,
                    heartY,
                    spriteProvider.getOverlayIndex(),
                    aspect.getRechargeProgress()
            );

            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    private static void renderAspect(
            DrawContext context,
            int x,
            int y,
            int textureIndex,
            float rechargeProgress
    ) {
        int u = textureIndex % 8 * 8;
        int v = textureIndex / 8 * 8;

        context.drawTexture(
                HeartAspect.HEART_ATLAS_TEXTURE,
                x + 1,
                y + 1,
                u,
                v,
                Math.round(rechargeProgress * 7),
                7,
                64,
                64
        );
    }

    private static float getComponent(int rgb, int shift) {
        return ((rgb >> shift) & 0xFF) / 255f;
    }
}
