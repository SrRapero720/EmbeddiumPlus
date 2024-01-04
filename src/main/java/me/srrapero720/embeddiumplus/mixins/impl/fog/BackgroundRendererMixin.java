package me.srrapero720.embeddiumplus.mixins.impl.fog;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FogRenderer.class, priority = 910)
public abstract class BackgroundRendererMixin {
    @Unique private static final float FOG_START = -8.0F;
    @Unique private static final float FOG_END = 1_000_000.0F;

    @Inject(method = "setupFog", at = @At("RETURN"))
    private static void applyFogModifyDistance(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if (!EmbyConfig.fogCache) {
            RenderSystem.setShaderFogStart(FOG_START);
            RenderSystem.setShaderFogEnd(FOG_END);
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }
}