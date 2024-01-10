package me.srrapero720.embeddiumplus.mixins.impl.frames;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.frames.accessors.IUsageGPU;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class GpuUsageMixin implements IUsageGPU {
    @Shadow private double gpuUtilization;
    @Unique private double embPlus$gpuUsageCooldown = 0;

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;isRecording()Z"))
    private boolean redirect$isRecording(MetricsRecorder instance) {
        return EmbyConfig.fpsDisplaySystemMode.get().gpu() || instance.isRecording();
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fpsString:Ljava/lang/String;"))
    private void inject$fpsStringAssign(boolean pRenderLevel, CallbackInfo ci) {
        this.embPlus$gpuUsageCooldown = this.gpuUtilization;
    }

    @Override
    public double embPlus$getSyncGpu() {
        return embPlus$gpuUsageCooldown;
    }
}
