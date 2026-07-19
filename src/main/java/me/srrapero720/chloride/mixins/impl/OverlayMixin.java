package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Overlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class OverlayMixin {
    @Shadow public static int fps;
    @Shadow public ClientLevel level;
    @Shadow private double gpuUtilization;

    @Unique private double chloride$gpuUsage = 0;

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/metrics/profiling/MetricsRecorder;isRecording()Z"))
    private boolean redirect$renderDebug(final MetricsRecorder instance) {
        return this.level == null ? instance.isRecording() : ChlorideConfig.fpsDisplay.systemDetails.gpu() || instance.isRecording();
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gpuUtilization:D", opcode = Opcodes.PUTFIELD))
    private void redirect$assign(final Minecraft instance, final double value) {
        this.chloride$gpuUsage = value;
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fps:I", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private void inject$getGPU(final boolean pRenderLevel, final CallbackInfo ci) {
        this.gpuUtilization = this.chloride$gpuUsage;
        Overlay.pushAvgFps(fps);
    }
}
