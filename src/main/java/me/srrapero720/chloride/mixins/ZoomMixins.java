package me.srrapero720.chloride.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.chloride.foundation.zoom.ZoomFeature;
import net.minecraft.client.Camera;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// RESERVED FOR FABRIC
public class ZoomMixins {

//    @Mixin(GameRenderer.class)
//    public static class GameRendererMixin {
//        @WrapOperation(method = "getFov", at = @At(value = "RETURN", ordinal = 1))
//        private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
//            cir.setReturnValue(ZoomFeature.zoom(cir.getReturnValueD()));
//        }
//    }

//    @Mixin(MouseHandler.class)
//    public class MouseMixin {
//        @Inject(method = "onScroll(JDD)V", at = @At("RETURN"))
//        private void onOnMouseScroll(long window, double xOffset, double yOffset, CallbackInfo ci) {
//            ZoomFeature.scroll(yOffset);
//        }
//    }
}
