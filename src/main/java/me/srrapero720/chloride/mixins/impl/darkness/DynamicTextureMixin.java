package me.srrapero720.chloride.mixins.impl.darkness;

import com.mojang.blaze3d.platform.NativeImage;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicTexture.class)
public class DynamicTextureMixin implements Darkness.DynamicTextureHook {
    @Shadow private NativeImage pixels;
    @Unique private boolean chloride$enabled;

    @Inject(method = "upload", at = @At(value = "HEAD"))
    private void inject$onUpload(final CallbackInfo ci) {
        if (!Darkness.enabled || !this.chloride$enabled) return;

        for (int b = 0; b < 16; b++) {
            for (int s = 0; s < 16; s++) {
                final int color = Darkness.darken(this.pixels.getPixelRGBA(b, s), b, s);
                this.pixels.setPixelRGBA(b, s, color);
            }
        }
    }

    // LightMapTextureManager uploads all pixels sets to -1 on the first call
    //  I tested it and without this check it runs well, but doesn't cost much to me keep it
    // NOTE: apparently, LightTexture uploads a new texture at construction, and without that call breaks the maps
    //  so I need to still use the hook
    @Override
    public void chloride$enableDarkness() {
        this.chloride$enabled = true;
    }
}