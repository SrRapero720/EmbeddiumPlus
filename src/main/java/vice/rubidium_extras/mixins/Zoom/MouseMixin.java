package vice.rubidium_extras.mixins.Zoom;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.rubidium_extras.WiZoom;

@Mixin(MouseHandler.class)
public class MouseMixin
{
    @Inject(at = {@At("RETURN")}, method = {"onScroll(JDD)V"})
    private void onOnMouseScroll(long long_1, double double_1, double double_2,
                                 CallbackInfo ci)
    {
        WiZoom.INSTANCE.onMouseScroll(double_2);
    }
}