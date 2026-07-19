package me.srrapero720.chloride.mixins.impl.darkness;

import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogColorMixin {
    @Inject(method = "computeFogColor", at = @At("TAIL"))
    private void chloride$dimFog(final Camera camera, final float partialTicks, final ClientLevel level, final int renderDistance, final float darkenWorldAmount, final Vector4f dest, final CallbackInfo ci) {
        Darkness.dimFog(dest);
    }
}
