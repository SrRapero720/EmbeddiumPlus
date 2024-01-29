package me.srrapero720.embeddiumplus.mixins.impl.zoom;

import me.srrapero720.embeddiumplus.WiZoom;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Inject(at = @At(value = "RETURN", ordinal = 1),
            method = {"getFov"},
            cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov,
                          CallbackInfoReturnable<Double> cir)
    {
        cir.setReturnValue(
                WiZoom.INSTANCE.changeFovBasedOnZoom(cir.getReturnValueD()));
    }
}

