package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Font.class)
public class FontShadowMixin {
    @Inject(method = "renderText(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)F", at = @At("HEAD"), cancellable = true)
    public void inject$renderText(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, Font.DisplayMode displayMode, int backgroundColor, int packedLightCoords, boolean p_381721_, CallbackInfoReturnable<Float> cir) {
        if (!ChlorideConfig.fontShadows && dropShadow) cir.setReturnValue(0f);
    }

    @Inject(method = "renderText(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)F", at = @At("HEAD"), cancellable = true)
    public void inject$renderText(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, Font.DisplayMode displayMode, int backgroundColor, int packedLightCoords, boolean p_381718_, CallbackInfoReturnable<Float> cir) {
        if (!ChlorideConfig.fontShadows && dropShadow) cir.setReturnValue(0f);
    }

    @Mixin(value = Font.StringRenderOutput.class)
    public static class StringRenderOutputMixin {
        @Shadow @Final private boolean drawShadow;

        @Inject(method = "accept", at = @At("HEAD"), cancellable = true)
        public void inject$accept(final int pPositionInCurrentSequence, final Style pStyle, final int pCodePoint, final CallbackInfoReturnable<Boolean> cir) {
            if (!ChlorideConfig.fontShadows && this.drawShadow) cir.setReturnValue(false);
        }
    }
}
