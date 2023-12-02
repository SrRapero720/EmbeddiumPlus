package me.disabled720.dynamiclights.mixin.sodium;

import me.disabled720.dynamiclights.SodiumDynamicLightHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess", remap = false)
public abstract class LightDataAccessMixin {
    @Dynamic
    @Inject(method = "getLightmap", at = @At("RETURN"), remap = false, cancellable = true)
    private static void lambdynlights$getLightmap(int word, CallbackInfoReturnable<Integer> cir) {
        int lightmap = SodiumDynamicLightHandler.lambdynlights$getLightmap(SodiumDynamicLightHandler.lambdynlights$pos.get(), word, cir.getReturnValueI());
        cir.setReturnValue(lightmap);
    }
}