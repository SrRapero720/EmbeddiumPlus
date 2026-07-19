package me.srrapero720.chloride.mixins.impl.darkness;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 26.1.2 RENDERS THE LIGHTMAP ON GPU FROM A LightmapRenderState UBO (lightmap.fsh). WHEN DARKNESS IS ON, THIS
// REPLICATES THAT SHADER ON CPU, CLAMPS EACH CELL'S LUMINANCE TO Darkness'S TARGET AND UPLOADS THE TEXTURE.
@Mixin(Lightmap.class)
public abstract class LightmapMixin {
    @Shadow @Final private GpuTexture texture;

    @Unique private NativeImage chloride$pixels;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void inject$darkenedRender(final LightmapRenderState renderState, final CallbackInfo ci) {
        final Minecraft mc = Minecraft.getInstance();
        Darkness.updateLuminance(mc, renderState.skyFactor, renderState.bossOverlayWorldDarkening, renderState.blockFactor - 1.4F);

        if (!Darkness.enabled) return;
        ci.cancel();
        if (!renderState.needsUpdate) return;

        final ClientLevel level = mc.level;
        if (level == null) return;

        Profiler.get().push("lightTex");

        final float skyFactor = renderState.skyFactor;
        final float blockFactor = renderState.blockFactor;
        final Vector3fc blockTint = renderState.blockLightTint;
        final Vector3fc skyColor = renderState.skyLightColor;
        final Vector3fc ambient = renderState.ambientColor;
        final Vector3fc nvColor = renderState.nightVisionColor;
        final float nvFactor = renderState.nightVisionEffectIntensity;
        final float boss = renderState.bossOverlayWorldDarkening;
        final float darkScale = renderState.darknessEffectScale;
        final float brightness = renderState.brightness;

        if (this.chloride$pixels == null) this.chloride$pixels = new NativeImage(16, 16, false);

        for (int sky = 0; sky < 16; sky++) {
            final float skyLevel = sky / 15.0F;
            final float skyBright = (skyLevel / (4.0F - 3.0F * skyLevel)) * skyFactor;

            for (int block = 0; block < 16; block++) {
                final float blockLevel = block / 15.0F;
                final float blockBright = (blockLevel / (4.0F - 3.0F * blockLevel)) * blockFactor;

                float r = Math.max(ambient.x(), nvColor.x() * nvFactor);
                float g = Math.max(ambient.y(), nvColor.y() * nvFactor);
                float b = Math.max(ambient.z(), nvColor.z() * nvFactor);

                r += skyColor.x() * skyBright;
                g += skyColor.y() * skyBright;
                b += skyColor.z() * skyBright;

                final float p = 2.0F * blockLevel - 1.0F;
                final float mixT = 0.9F * (p * p);
                r += Mth.lerp(mixT, blockTint.x(), 1.0F) * blockBright;
                g += Mth.lerp(mixT, blockTint.y(), 1.0F) * blockBright;
                b += Mth.lerp(mixT, blockTint.z(), 1.0F) * blockBright;

                r = Mth.lerp(boss, r, r * 0.7F);
                g = Mth.lerp(boss, g, g * 0.6F);
                b = Mth.lerp(boss, b, b * 0.6F);

                r -= darkScale;
                g -= darkScale;
                b -= darkScale;

                r = Mth.clamp(r, 0.0F, 1.0F);
                g = Mth.clamp(g, 0.0F, 1.0F);
                b = Mth.clamp(b, 0.0F, 1.0F);

                final float max = Math.max(r, Math.max(g, b));
                if (max > 0.0F) {
                    final float inv = 1.0F - max;
                    final float scale = (1.0F - inv * inv * inv * inv) / max;
                    r = Mth.lerp(brightness, r, r * scale);
                    g = Mth.lerp(brightness, g, g * scale);
                    b = Mth.lerp(brightness, b, b * scale);
                }

                final int abgr = 0xFF000000
                        | ((int) (b * 255.0F + 0.5F) << 16)
                        | ((int) (g * 255.0F + 0.5F) << 8)
                        | (int) (r * 255.0F + 0.5F);
                this.chloride$pixels.setPixelABGR(block, sky, Darkness.darken(abgr, block, sky));
            }
        }

        RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.texture, this.chloride$pixels);
        Profiler.get().pop();
    }

    @Inject(method = "close", at = @At("TAIL"))
    public void inject$close(final CallbackInfo ci) {
        if (this.chloride$pixels != null) {
            this.chloride$pixels.close();
            this.chloride$pixels = null;
        }
    }
}
