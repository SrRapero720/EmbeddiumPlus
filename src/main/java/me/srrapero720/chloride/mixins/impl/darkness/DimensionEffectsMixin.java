package me.srrapero720.chloride.mixins.impl.darkness;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Darkness;
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
        private void inject$brightFogColor(final CallbackInfoReturnable<Vec3> cir) {
            if (ChlorideConfig.darkness.mode == Darkness.DarkMode.VANILLA || !ChlorideConfig.darkness.onNether) return;

            cir.setReturnValue(Darkness.getFogColor(cir.getReturnValue(), ChlorideConfig.darkness.netherFogBright));
        }
    }

    @Mixin(DimensionSpecialEffects.EndEffects.class)
    public static class EndMixin {
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(final CallbackInfoReturnable<Vec3> cir) {
            if (ChlorideConfig.darkness.mode == Darkness.DarkMode.VANILLA || !ChlorideConfig.darkness.onEnd) return;

            cir.setReturnValue(Darkness.getFogColor(cir.getReturnValue(), ChlorideConfig.darkness.endFogBright));
        }
    }
}