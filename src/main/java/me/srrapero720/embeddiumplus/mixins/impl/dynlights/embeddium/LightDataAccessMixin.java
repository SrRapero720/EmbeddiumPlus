package me.srrapero720.embeddiumplus.mixins.impl.dynlights.embeddium;

import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.EmbedtDynamicLightHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LightDataAccess.class, remap = false)
public abstract class LightDataAccessMixin {
    @Dynamic
    @Inject(method = "getLightmap", at = @At("RETURN"), remap = false, cancellable = true)
    private static void lambdynlights$getLightmap(int word, CallbackInfoReturnable<Integer> cir) {
        int lightmap = EmbedtDynamicLightHandler.getLightMap(EmbedtDynamicLightHandler.pos.get(), word, cir.getReturnValueI());
        cir.setReturnValue(lightmap);
    }
}