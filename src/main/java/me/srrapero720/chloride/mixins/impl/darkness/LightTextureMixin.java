package me.srrapero720.chloride.mixins.impl.darkness;

import me.srrapero720.chloride.features.TrueDarknessFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @Shadow @Final private DynamicTexture lightTexture;

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void inject$init(final GameRenderer pRenderer, final Minecraft pMinecraft, final CallbackInfo ci) {
        ((TrueDarknessFeature.DynamicTextureHook) this.lightTexture).chloride$enableDarkness();
    }
}