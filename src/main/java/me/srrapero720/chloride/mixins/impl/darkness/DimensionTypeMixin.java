package me.srrapero720.chloride.mixins.impl.darkness;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = "hasSkyLight", at = @At("RETURN"), cancellable = true)
    private void inject$hasSkyLight(final CallbackInfoReturnable<Boolean> cir) {
        if (ChlorideConfig.darkness.blockLightOnly) {
            cir.setReturnValue(true);
        }
    }
}
