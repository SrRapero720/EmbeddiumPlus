package me.srrapero720.embeddiumplus.mixins.impl.frames;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.frames.accessors.IUsageGPU;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class GpuUsageMixin implements IUsageGPU {
    @Shadow private double gpuUtilization;
    @Shadow @Nullable public MultiPlayerGameMode gameMode;
    @Unique private double embPlus$gpuUsage = 0;

    @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;isRecording()Z"))
    private boolean redirect$isRecording(MetricsRecorder instance, Operation<Boolean> original) {
        return gameMode == null ? original.call(instance) : EmbyConfig.fpsDisplaySystemMode.get().gpu() || original.call(instance);
    }

    // NEEDED TO GET GPU USAGE WITH A VANILLA COOLDOWN
    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fpsString:Ljava/lang/String;"))
    private void inject$fpsStringAssign(boolean pRenderLevel, CallbackInfo ci) {
        this.embPlus$gpuUsage = this.gpuUtilization;
    }

    @Override
    public double embPlus$getSyncGpu() {
        return embPlus$gpuUsage;
    }
}
