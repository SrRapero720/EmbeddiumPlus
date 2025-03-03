package me.srrapero720.chloride.mixins.impl.borderless;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.ChlorideConfig.*;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.srrapero720.chloride.ChlorideConfig.fullScreen;

@Mixin(KeyboardHandler.class)
public class KeyboardF11Mixin {
    @Shadow @Final public Minecraft minecraft;

    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;toggleFullScreen()V"), cancellable = true)
    public void redirect$handleFullScreenToggle(long pWindowPointer, int pKey, int pScanCode, int pAction, int pModifiers, CallbackInfo ci) {
        switch (ChlorideConfig.borderlessAttachModeF11) {
            case ATTACH -> ChlorideConfig.setFullScreenMode(minecraft.options, FullScreenMode.nextOf(fullScreen));
            case REPLACE -> ChlorideConfig.setFullScreenMode(minecraft.options, FullScreenMode.nextBorderless(fullScreen));
            case OFF -> ChlorideConfig.setFullScreenMode(minecraft.options, FullScreenMode.nextFullscreen(fullScreen));
        }
        ci.cancel();
    }
}
