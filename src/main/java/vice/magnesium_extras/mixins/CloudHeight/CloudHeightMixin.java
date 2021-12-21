package vice.magnesium_extras.mixins.CloudHeight;

import net.minecraft.client.world.DimensionRenderInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

@Mixin(value = DimensionRenderInfo.class)
public class CloudHeightMixin
{
    @Shadow @Final private float cloudLevel;

    @Inject(at = @At("HEAD"), method = "getCloudHeight", cancellable = true)
    private void getCloudHeight(CallbackInfoReturnable<Float> cir)
    {
        if (cloudLevel == 128.0F)
            cir.setReturnValue(MagnesiumExtrasConfig.cloudHeight.get().floatValue());
    }
}