package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FogRenderer.class, priority = 910)
public abstract class FogAndBandMixin {
    @Unique private static final float FOG_END = 1_000_000.0F;

    @WrapOperation(method = "setupFog", at = @At(value = "NEW", target = "(FFLcom/mojang/blaze3d/shaders/FogShape;FFFF)Lnet/minecraft/client/renderer/FogParameters;"))
    private static FogParameters inject$fogToggle_fogDistance(float start, float end, FogShape shape, float red, float green, float blue, float alpha, Operation<FogParameters> original, Camera camera, @Local FogRenderer.FogData fogrenderer$fogdata, @Local FogRenderer.MobEffectFogFunction fogrenderer$mobeffectfogfunction) {
        if (camera.getFluidInCamera() != FogType.NONE) return null;
        if (fogrenderer$mobeffectfogfunction != null) return null;


        if (!ChlorideConfig.fog) { // FOG IS DISABLED
            fogrenderer$fogdata.start = FOG_END;
            fogrenderer$fogdata.end = FOG_END;
            fogrenderer$fogdata.shape = FogShape.SPHERE;
            return original.call(start, end, shape, red, green, blue, alpha);
        } else if (ChlorideConfig.customFog) { // OVERRIDE FOG AT ALL
            fogrenderer$fogdata.start = ChlorideConfig.fogStart;
            fogrenderer$fogdata.end = ChlorideConfig.fogEnd;
            fogrenderer$fogdata.shape = ChlorideConfig.fogShape;
            return original.call(start, end, shape, red, green, blue, alpha);
        }

        // TOGGLE PER LEVEL
        final Entity entity = camera.getEntity();
        if (entity instanceof final Player player) {
            final Level level = player.level();
            if ((level.dimension() == Level.OVERWORLD && !ChlorideConfig.fogOnOverworld)
                    || (level.dimension() == Level.NETHER && !ChlorideConfig.fogOnNether)
                    || (level.dimension() == Level.END && !ChlorideConfig.fogOnEnd)) {
                fogrenderer$fogdata.start = FOG_END;
                fogrenderer$fogdata.end = FOG_END;
                fogrenderer$fogdata.shape = FogShape.SPHERE;
            }

        }
        return null;
    }

    @WrapOperation(method = "computeFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 redirect$blueband_gaussianSampleColor(final Vec3 vec, final CubicSampler.Vec3Fetcher fetcher, final Operation<Vec3> original) {
        if (!ChlorideConfig.blueBand) {
            final Minecraft mc = Minecraft.getInstance();

            if (mc.level.dimensionType().hasSkyLight())
                return Vec3.fromRGB24(mc.level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), mc.getFrameTimeNs()));
        }
        return original.call(vec, fetcher);
    }

    @WrapOperation(method = "computeFogColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F", remap = false))
    private static float redirect$blueband_dot(final Vector3f instance, final Vector3fc v, final Operation<Float> original) {
        if (!ChlorideConfig.blueBand) return 0;
        return original.call(instance, v);
    }

    @WrapOperation(method = "computeFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    private static float redirect$blueband_getRainLevel(final ClientLevel instance, final float v, final Operation<Float> original) {
        if (!ChlorideConfig.blueBand) return 0;
        return original.call(instance, v);
    }

    @WrapOperation(method = "computeFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    private static float redirect$blueband_getThunderLevel(final ClientLevel instance, final float v, final Operation<Float> original) {
        if (!ChlorideConfig.blueBand) return 0;
        return original.call(instance, v);
    }
}