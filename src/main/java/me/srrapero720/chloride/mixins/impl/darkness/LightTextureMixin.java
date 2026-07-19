package me.srrapero720.chloride.mixins.impl.darkness;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 1.21.11 COMPUTES THE LIGHTMAP ON GPU (lightmap.fsh + LightmapInfo UBO), SO DARKNESS CAN'T TOUCH ITS PIXELS ANYMORE.
// WHEN DARKNESS IS ON, THIS REPLICATES THE SHADER MATH ON CPU, APPLIES THE LUMINANCE CLAMP AND UPLOADS THE TEXTURE.
@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
    @Shadow @Final private GpuTexture texture;
    @Shadow @Final private Minecraft minecraft;
    @Shadow public boolean updateLightTexture;
    @Shadow public float blockLightRedFlicker;

    @Unique private NativeImage chloride$pixels;

    // THE LIGHTMAP TEXTURE IS CREATED WITHOUT USAGE_COPY_DST; ADD IT SO writeToTexture IS ALLOWED
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;"), index = 1)
    public int modify$textureUsage(final int usage) {
        return usage | GpuTexture.USAGE_COPY_DST;
    }

    @Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true)
    public void inject$darkenedUpdate(final float partialTicks, final CallbackInfo ci) {
        if (!Darkness.enabled) return;

        ci.cancel();
        if (!this.updateLightTexture) return;
        this.updateLightTexture = false;

        final ClientLevel level = this.minecraft.level;
        if (level == null) return;

        Profiler.get().push("lightTex");

        // SAME INPUTS THE VANILLA PASS FEEDS INTO THE LightmapInfo UBO
        final var player = this.minecraft.player;
        final var probe = this.minecraft.gameRenderer.getMainCamera().attributeProbe();
        final int skyColor = probe.getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, partialTicks);
        final float ambientLight = level.dimensionType().ambientLight();
        float skyLightFactor = probe.getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, partialTicks);

        float ambientR = 1.0F, ambientG = 1.0F, ambientB = 1.0F;
        final EndFlashState endFlash = level.endFlashState();
        if (endFlash != null) {
            ambientR = 0.99F;
            ambientG = 1.12F;
            ambientB = 1.0F;
            if (!this.minecraft.options.hideLightningFlash().get()) {
                final float intensity = endFlash.getIntensity(partialTicks);
                skyLightFactor += this.minecraft.gui.getBossOverlay().shouldCreateWorldFog() ? intensity / 3.0F : intensity;
            }
        }

        final float effectScale = this.minecraft.options.darknessEffectScale().get().floatValue();
        final float darknessGamma = player.getEffectBlendFactor(MobEffects.DARKNESS, partialTicks) * effectScale;
        final float darknessScale = Math.max(0.0F, Mth.cos((player.tickCount - partialTicks) * (float) Math.PI * 0.025F) * (0.45F * darknessGamma)) * effectScale;

        final float waterVision = player.getWaterVision();
        final float nightVision;
        if (player.hasEffect(MobEffects.NIGHT_VISION)) {
            nightVision = GameRenderer.getNightVisionScale(player, partialTicks);
        } else if (waterVision > 0.0F && player.hasEffect(MobEffects.CONDUIT_POWER)) {
            nightVision = waterVision;
        } else {
            nightVision = 0.0F;
        }

        final float blockFactor = this.blockLightRedFlicker + 1.5F;
        final float brightness = Math.max(0.0F, this.minecraft.options.gamma().get().floatValue() - darknessGamma);
        final float darkenWorld = this.minecraft.gameRenderer.getDarkenWorldAmount(partialTicks);
        final float skyR = ARGB.redFloat(skyColor);
        final float skyG = ARGB.greenFloat(skyColor);
        final float skyB = ARGB.blueFloat(skyColor);

        if (this.chloride$pixels == null) this.chloride$pixels = new NativeImage(16, 16, false);

        for (int sky = 0; sky < 16; sky++) {
            final float skyBright = LightTexture.getBrightness(0.0F, sky) * skyLightFactor;

            for (int block = 0; block < 16; block++) {
                final float blockBright = LightTexture.getBrightness(0.0F, block) * blockFactor;

                // "CUBIC NONSENSE": BLOCK LIGHT DIPS TO YELLOWISH IN THE MIDDLE, WHITE WHEN SATURATED
                float r = blockBright;
                float g = blockBright * ((blockBright * 0.6F + 0.4F) * 0.6F + 0.4F);
                float b = blockBright * (blockBright * blockBright * 0.6F + 0.4F);

                r = Mth.lerp(ambientLight, r, ambientR) + skyR * skyBright;
                g = Mth.lerp(ambientLight, g, ambientG) + skyG * skyBright;
                b = Mth.lerp(ambientLight, b, ambientB) + skyB * skyBright;

                r = Mth.lerp(0.04F, r, 0.75F);
                g = Mth.lerp(0.04F, g, 0.75F);
                b = Mth.lerp(0.04F, b, 0.75F);

                if (ambientLight == 0.0F) {
                    r = Mth.lerp(darkenWorld, r, r * 0.7F);
                    g = Mth.lerp(darkenWorld, g, g * 0.6F);
                    b = Mth.lerp(darkenWorld, b, b * 0.6F);
                }

                if (nightVision > 0.0F) {
                    // SCALE UP UNIFORMLY UNTIL 1.0 IS HIT BY ONE OF THE CHANNELS
                    final float max = Math.max(r, Math.max(g, b));
                    if (max > 0.0F && max < 1.0F) {
                        final float scale = 1.0F / max;
                        r = Mth.lerp(nightVision, r, r * scale);
                        g = Mth.lerp(nightVision, g, g * scale);
                        b = Mth.lerp(nightVision, b, b * scale);
                    }
                }

                if (ambientLight == 0.0F) {
                    r -= darknessScale;
                    g -= darknessScale;
                    b -= darknessScale;
                }

                r = Mth.clamp(r, 0.0F, 1.0F);
                g = Mth.clamp(g, 0.0F, 1.0F);
                b = Mth.clamp(b, 0.0F, 1.0F);

                // notGamma() MIXED IN BY THE BRIGHTNESS SETTING
                final float max = Math.max(r, Math.max(g, b));
                if (max > 0.0F) {
                    final float maxInv = 1.0F - max;
                    final float scale = (1.0F - maxInv * maxInv * maxInv * maxInv) / max;
                    r = Mth.lerp(brightness, r, r * scale);
                    g = Mth.lerp(brightness, g, g * scale);
                    b = Mth.lerp(brightness, b, b * scale);
                }

                r = Mth.lerp(0.04F, r, 0.75F);
                g = Mth.lerp(0.04F, g, 0.75F);
                b = Mth.lerp(0.04F, b, 0.75F);

                // ABGR PACKING, THE LAYOUT BOTH NativeImage AND Darkness.darken WORK WITH
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
