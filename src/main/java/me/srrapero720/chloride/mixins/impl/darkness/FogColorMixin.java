package me.srrapero720.chloride.mixins.impl.darkness;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public class FogColorMixin {
    @ModifyReturnValue(method = "computeFogColor", at = @At("RETURN"))
    private Vector4f chloride$dimFog(final Vector4f color) {
        return Darkness.dimFog(color);
    }
}
