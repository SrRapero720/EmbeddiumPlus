package me.srrapero720.chloride.mixins.impl.darkness;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.features.TrueDarknessFeature;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public class DimensionEffectsMixin {

    @Mixin(DimensionSpecialEffects.NetherEffects.class)
    public static class NetherMixin {
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (ChlorideConfig.darknessMode == ChlorideConfig.DarknessMode.VANILLA || !ChlorideConfig.darknessOnNether) return;

            cir.setReturnValue(TrueDarknessFeature.getFogColor(cir.getReturnValue(), ChlorideConfig.darknessNetherFogBright));
        }
    }

    @Mixin(DimensionSpecialEffects.EndEffects.class)
    public static class EndMixin {
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (ChlorideConfig.darknessMode == ChlorideConfig.DarknessMode.VANILLA || !ChlorideConfig.darknessOnEnd) return;

            cir.setReturnValue(TrueDarknessFeature.getFogColor(cir.getReturnValue(), ChlorideConfig.darknessEndFogBright));
        }
    }
}