package me.srrapero720.embeddiumplus.mixins.impl.frames;

import com.mojang.blaze3d.systems.TimerQuery;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.frames.accessors.IGpuUsage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class GpuUsageMixin implements IGpuUsage {
    @Shadow private double gpuUtilization;
    @Shadow @Nullable private TimerQuery.FrameProfile currentFrameProfile;

    @Shadow private long savedCpuDuration;
    @Shadow private long lastNanoTime;
    @Unique private double embPlus$gpuUsage = 0;
    @Unique private long embPlus$nextTime = 0;
    @Unique private boolean embPlus$begin = false;

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gpuUtilization:D", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER, ordinal = 1))
    private void inject$defineGpuUsage(boolean pRenderLevel, CallbackInfo ci) {
        embPlus$gpuUsage = this.gpuUtilization;
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject$beginProfiler(boolean pRenderLevel, CallbackInfo ci, long i, Runnable runnable, long j1, boolean flag) {
        if (flag || Util.getMillis() < embPlus$nextTime) return; // IS WORKING BTW, OR IS ON COOL DOWN

        embPlus$nextTime = Util.getMillis() + 1000;
        switch (EmbyConfig.fpsDisplaySystemMode.get()) { // PERFORMANCE
            case GPU, ON -> {
                embPlus$begin = this.currentFrameProfile == null || this.currentFrameProfile.isDone();
                if (embPlus$begin) {
                    TimerQuery.getInstance().ifPresent(TimerQuery::beginProfile);
                }
            }
        }
    }


    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 1, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject$endProfile(boolean pRenderLevel, CallbackInfo ci, long i, Runnable runnable, long j1, boolean flag) {
        if (embPlus$begin) {
            TimerQuery.getInstance().ifPresent((p_231363_) -> this.currentFrameProfile = p_231363_.endProfile());
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FrameTimer;logFrameDuration(J)V", shift = At.Shift.BEFORE))
    private void inject$saveCPUDuration(boolean pRenderLevel, CallbackInfo ci) {
        if (embPlus$begin) {
            long l = Util.getNanos();
            this.savedCpuDuration = l - this.lastNanoTime;
            embPlus$begin = false;
        }
    }


    @Override
    public double embPlus$getGPU() {
        return embPlus$gpuUsage;
    }
}
