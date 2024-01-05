package me.srrapero720.embeddiumplus.mixins.impl.fontshadow;

import me.srrapero720.embeddiumplus.EmbyConfig;
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
public class FontMixin {
    @Inject(method = "renderText(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)F", at = @At("HEAD"), cancellable = true)
    public void inject$renderText(String pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords, CallbackInfoReturnable<Float> cir) {
        if (!EmbyConfig.fontShadowsCache && pDropShadow) cir.setReturnValue(0f);
    }

    @Inject(method = "renderText(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)F", at = @At("HEAD"), cancellable = true)
    public void inject$renderText(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords, CallbackInfoReturnable<Float> cir) {
        if (!EmbyConfig.fontShadowsCache && pDropShadow) cir.setReturnValue(0f);
    }

    @Mixin(value = Font.StringRenderOutput.class)
    public static class StringRenderOutputMixin {
        @Shadow @Final private boolean dropShadow;

        @Inject(method = "accept", at = @At("HEAD"), cancellable = true)
        public void inject$accept(int pPositionInCurrentSequence, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir) {
            if (!EmbyConfig.fontShadowsCache && this.dropShadow) cir.setReturnValue(false);
        }
    }
}
