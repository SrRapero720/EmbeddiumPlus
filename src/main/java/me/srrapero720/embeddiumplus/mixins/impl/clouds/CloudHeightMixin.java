package me.srrapero720.embeddiumplus.mixins.impl.clouds;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.srrapero720.embeddiumplus.EmbPlusConfig;

@Mixin(value = DimensionSpecialEffects.class)
public class CloudHeightMixin {
    @Shadow @Final private float cloudLevel;

    @Inject(at = @At("HEAD"), method = "getCloudHeight", cancellable = true)
    private void getCloudHeight(CallbackInfoReturnable<Float> cir) {
        if (cloudLevel == 192.0F)
            cir.setReturnValue(EmbPlusConfig.cloudHeight.get().floatValue());
    }
}