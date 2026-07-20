package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import me.srrapero720.chloride.impl.GraphicsEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ArrayListDeque;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HotswapMixins {

    @Mixin(Minecraft.class)
    public static class FrameMixin {
        @Inject(method = "runTick", at = @At("TAIL"))
        private void chloride$onFrame(final boolean renderLevel, final CallbackInfo ci) {
            GraphicsEngine.onFrame();
        }
    }

    @Mixin(RenderSystem.class)
    public interface RenderSystemAccessor {
        @Accessor("DEVICE")
        static void setDevice(final GpuDevice device) {
            throw new AssertionError();
        }

        @Accessor("projectionMatrixBuffer")
        static void setProjectionMatrixBuffer(final GpuBufferSlice slice) {
            throw new AssertionError();
        }

        @Accessor("savedProjectionMatrixBuffer")
        static void setSavedProjectionMatrixBuffer(final GpuBufferSlice slice) {
            throw new AssertionError();
        }

        @Accessor("shaderFog")
        static void setShaderFog(final GpuBufferSlice slice) {
            throw new AssertionError();
        }

        @Accessor("shaderLightDirections")
        static void setShaderLightDirections(final GpuBufferSlice slice) {
            throw new AssertionError();
        }

        @Accessor("globalSettingsUniform")
        static void setGlobalSettingsUniform(final GpuBuffer buffer) {
            throw new AssertionError();
        }

        @Accessor("sharedSequential")
        static RenderSystem.AutoStorageIndexBuffer sharedSequential() {
            throw new AssertionError();
        }

        @Accessor("sharedSequentialQuad")
        static RenderSystem.AutoStorageIndexBuffer sharedSequentialQuad() {
            throw new AssertionError();
        }

        @Accessor("sharedSequentialLines")
        static RenderSystem.AutoStorageIndexBuffer sharedSequentialLines() {
            throw new AssertionError();
        }

        @Accessor("PENDING_FENCES")
        static ArrayListDeque<?> pendingFences() {
            throw new AssertionError();
        }
    }

    @Mixin(RenderSystem.AutoStorageIndexBuffer.class)
    public interface AutoIndexBufferAccessor {
        @Accessor("buffer")
        void setBuffer(final GpuBuffer buffer);

        @Accessor("indexCount")
        void setIndexCount(final int indexCount);
    }
}
