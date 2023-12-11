package me.srrapero720.embeddiumplus.mixins.impl.fog;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;

@Mixin(value = FogRenderer.class, priority = 910)
public abstract class BackgroundRendererMixin {
    @Unique private static final float FOG_START = -8.0F;
    @Unique private static final float FOG_END = 1_000_000.0F;

    @Inject(method = "setupFog", at = @At("RETURN"))
    private static void applyFogModifyDistance(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if (!EmbeddiumPlusConfig.fog.get()) {
            RenderSystem.setShaderFogStart(FOG_START);
            RenderSystem.setShaderFogEnd(FOG_END);
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }
}