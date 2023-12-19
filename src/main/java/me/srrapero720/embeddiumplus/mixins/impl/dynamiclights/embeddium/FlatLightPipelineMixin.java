package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights.embeddium;

import me.jellysquid.mods.sodium.client.model.light.flat.FlatLightPipeline;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.EmbedtDynamicLightHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FlatLightPipeline.class, remap = false)
public abstract class FlatLightPipelineMixin {
    @Dynamic
    @Inject(method = "getOffsetLightmap", at = @At(value = "RETURN", ordinal = 1), remap = false, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void lambdynlights$getLightmap(BlockPos pos, Direction face, CallbackInfoReturnable<Integer> cir, int word, int adjWord) {
        int lightmap = EmbedtDynamicLightHandler.lambdynlights$getLightmap(pos, adjWord, cir.getReturnValueI());
        cir.setReturnValue(lightmap);
    }
}