package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AtmosphericFogEnvironment.class, priority = 910)
public class FogAndBandMixin {
    // BLUE BAND OFF = THE FOG BASE COLOR BECOMES THE PLAIN SKY COLOR, SKIPPING THE SUNRISE TINT,
    // WEATHER DARKEN AND DISTANCE BLEND THAT PAINT THE HORIZON BAND; THE FOG THEN MELTS INTO THE SKY
    @Inject(method = "getBaseColor", at = @At("HEAD"), cancellable = true)
    public void inject$blueBand(final ClientLevel level, final Camera camera, final int renderDistance, final float partialTick, final CallbackInfoReturnable<Integer> cir) {
        if (!ChlorideConfig.fog.blueBand && level.dimensionType().hasSkyLight()) {
            cir.setReturnValue(camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTick));
        }
    }
}
