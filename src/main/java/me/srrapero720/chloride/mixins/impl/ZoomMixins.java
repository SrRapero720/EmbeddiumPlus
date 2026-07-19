package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Zoom;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ZoomMixins {

    @Mixin(GameRenderer.class)
    public static class GameRendererMixin {
        @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
        private float chloride$zoom(final float fov) {
            if (Zoom.canUseZoom() && ChlorideConfig.zoom.enabled) return (float) Zoom.zoom(fov);
            return fov;
        }
    }

    @Mixin(MouseHandler.class)
    public static class MouseMixin {
        @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
        private void chloride$zoomScroll(final long window, final double xOffset, final double yOffset, final CallbackInfo ci) {
            if (Zoom.canUseZoom() && ChlorideConfig.zoom.enabled && Zoom.scroll(yOffset)) ci.cancel();
        }
    }
}
