package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.sugar.Local;
import me.srrapero720.chloride.impl.Fog;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"))
    private void chloride$fogDistance(final Camera camera, final int renderDistance, final DeltaTracker deltaTracker, final float darkenWorldAmount, final ClientLevel level, final CallbackInfoReturnable<Vector4f> cir, @Local final FogData fogData) {
        if (camera.getFluidInCamera() != FogType.NONE) return;
        if (camera.entity() instanceof LivingEntity entity && (entity.hasEffect(MobEffects.BLINDNESS) || entity.hasEffect(MobEffects.DARKNESS))) return;
        Fog.apply(fogData, level.dimension());
    }
}
