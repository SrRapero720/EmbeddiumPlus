package me.srrapero720.chloride.mixins.impl;

import it.unimi.dsi.fastutil.ints.IntList;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.IParticleTypeData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ParticlesMixins {

    // SMALL OPTIMIZATION
    @Mixin(ParticleType.class)
    public static class ParticleTypeMixin implements IParticleTypeData {
        @Unique private Identifier chloride$id;

        @Override
        public Identifier getId() {
            if (this.chloride$id == null) {
                this.chloride$id = BuiltInRegistries.PARTICLE_TYPE.getKey((ParticleType<?>) (Object) this);
            }
            return this.chloride$id;
        }
    }

    @Mixin(WeatherEffectRenderer.class)
    public static class LevelRendererMixin {
        @Inject(method = "render(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V", at = @At(value = "HEAD"), cancellable = true)
        private void inject$render(Vec3 cameraPosition, WeatherRenderState renderState, CallbackInfo ci) {
            if (!ChlorideConfig.particles.rain) {
                ci.cancel();
            }
        }
    }

    @Mixin(ParticleEngine.class)
    public static class EngineMixin {
        @Inject(method = "createParticle", at = @At(value = "HEAD"), cancellable = true)
        public void inject$create(ParticleOptions parameters, double x, double y, double z, double speeX, double speeY, double speeZ, CallbackInfoReturnable<Particle> cir) {
            final var id = ((IParticleTypeData) parameters.getType()).getId();
            if (ChlorideConfig.particles.disabled.contains(id)) {
                cir.setReturnValue(null);
            }
        }
    }

    // BLOCK-BREAK/HIT PARTICLE TOGGLES LIVE IN ClientLevel, NOT IN ParticleEngine
    @Mixin(ClientLevel.class)
    public static class ClientLevelMixin {
        @Inject(method = "tickWeatherEffects", at = @At(value = "HEAD"), cancellable = true)
        public void inject$tickRain(CallbackInfo ci) {
            if (!ChlorideConfig.particles.rainDrops) {
                ci.cancel();
            }
        }

        @Inject(method = "addDestroyBlockEffect", at = @At(value = "HEAD"), cancellable = true)
        public void inject$destroy(BlockPos pos, BlockState state, CallbackInfo ci) {
            if (!ChlorideConfig.particles.blockDestroyed) {
                ci.cancel();
            }
        }

        @Inject(method = "addBreakingBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V", at = @At(value = "HEAD"), cancellable = true)
        public void inject$crack(BlockPos pos, Direction direction, CallbackInfo ci) {
            if (!ChlorideConfig.particles.blockCracking) {
                ci.cancel();
            }
        }
    }

    @Mixin(FireworkParticles.Starter.class)
    public static final class FireworkStarterMixin {
        @Unique private Identifier chloride$id;

        @Inject(method = "createParticle", at = @At(value = "HEAD"), cancellable = true)
        public void inject$create(double x, double y, double z, double velocityX, double velocityY, double velocityZ, IntList colors, IntList fadeColors, boolean trail, boolean flicker, CallbackInfo ci) {
            if (ChlorideConfig.particles.disabled.contains(this.getId())) {
                ci.cancel();
            }
        }

        @Unique
        private Identifier getId() {
            if (this.chloride$id == null) {
                this.chloride$id = BuiltInRegistries.PARTICLE_TYPE.getKey(ParticleTypes.FIREWORK.getType());
            }
            return this.chloride$id;
        }
    }
}
