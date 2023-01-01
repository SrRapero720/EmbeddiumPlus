package vice.rubidium_extras.mixins.Zoom;

import net.minecraft.client.Camera;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.rubidium_extras.WiZoom;

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

