package me.srrapero720.embeddiumplus.mixins.impl.fog;

//@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
//    @ModifyArg(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), index = 0)
    private static float modifySetShaderFogStart(float original) {
        return 0;
//        return original * ((float) EmbeddiumExtrasConfig.fogStart / 100);
    }
}