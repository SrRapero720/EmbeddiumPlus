package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

public class SkyMixins {
    @Mixin(ClientLevel.ClientLevelData.class)
    public static class HorizonMixin {
        // 63 = vanilla sentinel: keeps the original value so superflat worlds (min build height) stay untouched
        @Unique private static final double VANILLA_HORIZON = 63.0d;

        @ModifyReturnValue(method = "getHorizonHeight", at = @At("RETURN"))
        private double inject$lowerVoidHorizon(final double original) {
            final double horizon = ChlorideConfig.world.lowerVoidHorizon;
            return horizon == VANILLA_HORIZON ? original : horizon;
        }
    }

    @Mixin(GameRenderer.class)
    public static class DepthFarMixin {
        @Unique private static final float MIN_SKY_DEPTH = 2048.0F;

        @ModifyReturnValue(method = "getDepthFar", at = @At("RETURN"))
        private float inject$farSkybox(final float original) {
            return ChlorideConfig.world.farSkybox && original < MIN_SKY_DEPTH ? MIN_SKY_DEPTH : original;
        }
    }
}
