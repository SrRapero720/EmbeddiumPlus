package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.GpuBackend;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import me.srrapero720.chloride.impl.GraphicsEngine;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.loading.EarlyLoadingScreenController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.util.ArrayListDeque;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HotswapMixins {

    @Mixin(Minecraft.class)
    public static class EarlyFinishMixin {
        @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/loading/ClientModLoader;finish()V"))
        private void chloride$beforeModLoaderFinish(final CallbackInfo ci) {
            GraphicsEngine.prepareEarlyWindowClose();
        }
    }

    @Mixin(Window.class)
    public static class WindowMixin {
        @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/loading/EarlyLoadingScreenController;current()Lnet/neoforged/fml/loading/EarlyLoadingScreenController;"))
        private static EarlyLoadingScreenController chloride$earlyOnInit(final Operation<EarlyLoadingScreenController> original, @Local(argsOnly = true) final GpuBackend backend) {
            return GraphicsEngine.earlyWindow(backend, original::call);
        }

        @WrapOperation(method = "createGlfwWindow", at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/loading/EarlyLoadingScreenController;current()Lnet/neoforged/fml/loading/EarlyLoadingScreenController;"))
        private static EarlyLoadingScreenController chloride$earlyOnCreate(final Operation<EarlyLoadingScreenController> original, @Local(argsOnly = true) final GpuBackend backend) {
            return GraphicsEngine.earlyWindow(backend, original::call);
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
