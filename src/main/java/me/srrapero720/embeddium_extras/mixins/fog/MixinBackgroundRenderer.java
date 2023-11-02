package me.srrapero720.embeddium_extras.mixins.fog;

import me.srrapero720.embeddium_extras.config.EmbeddiumExtrasConfig;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
//    @ModifyArg(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), index = 0)
    private static float modifySetShaderFogStart(float original) {
        return 0;
//        return original * ((float) EmbeddiumExtrasConfig.fogStart / 100);
    }
}