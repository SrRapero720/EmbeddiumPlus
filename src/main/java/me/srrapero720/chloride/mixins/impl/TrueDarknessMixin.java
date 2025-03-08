package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.foundation.darkness.DarknessPlus;
import me.srrapero720.chloride.mixins.impl.accessors.LightTextureAccessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class TrueDarknessMixin {

    @Mixin(GameRenderer.class)
    public static abstract class GameRendererMixin {
        @Shadow @Final
        public LightTexture lightTexture;
        @Shadow @Final private Minecraft minecraft;

        @Inject(method = "renderLevel", at = @At(value = "HEAD"))
        private void inject$renderLevel(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
            final var lightTexAccessor = (LightTextureAccessors) lightTexture;

            if (lightTexAccessor.embPlus$isDirty()) {
                minecraft.getProfiler().push("lightTex");
                DarknessPlus.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightTexAccessor.embPlus$getFlicker());
                minecraft.getProfiler().pop();
            }
        }
    }

    @Mixin(DynamicTexture.class)
    public static class DynamicTextureMixin {
        @Shadow
        private NativeImage pixels;
        @Unique
        private boolean chloride$firstCall = true;

        @Inject(method = "upload", at = @At(value = "HEAD"))
        private void inject$onUpload(CallbackInfo ci) {
            if (!DarknessPlus.enabled) return;
            // LightMapTextureManager uploads all pixels sets to -1 on the first call
            //  I tested it and without this check it runs well, but doesn't cost much to me keep it
            if (chloride$firstCall) {
                chloride$firstCall = false;
                return;
            }

            final NativeImage img = pixels;
            for (int b = 0; b < 16; b++) {
                for (int s = 0; s < 16; s++) {
                    final int color = DarknessPlus.darken(img.getPixelRGBA(b, s), b, s);
                    img.setPixelRGBA(b, s, color);
                }
            }
        }
    }

    @Mixin(DimensionSpecialEffects.class)
    public static class DimensionEffectsMixin {

        @Mixin(DimensionSpecialEffects.NetherEffects.class)
        public static class NetherMixin {
            @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
            private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
                if (ChlorideConfig.darknessMode == ChlorideConfig.DarknessMode.OFF) return;
                if (!ChlorideConfig.darknessOnNether) return;

                cir.setReturnValue(DarknessPlus.getDarkFogColor(
                        cir.getReturnValue(),
                        ChlorideConfig.darknessNetherFogBright)
                );
            }
        }

        @Mixin(DimensionSpecialEffects.EndEffects.class)
        public static class EndMixin {
            @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
            private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
                if (ChlorideConfig.darknessMode == ChlorideConfig.DarknessMode.OFF) return;
                if (!ChlorideConfig.darknessOnEnd) return;

                cir.setReturnValue(DarknessPlus.getDarkFogColor(
                        cir.getReturnValue(),
                        ChlorideConfig.darknessEndFogBright)
                );
            }
        }
    }

}
