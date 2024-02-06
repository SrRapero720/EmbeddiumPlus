package me.srrapero720.embeddiumplus.mixins.impl.zume;

import com.mojang.blaze3d.platform.InputConstants;
import dev.nolij.zume.lexforge18.ZumeKeyBind;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// nolij will hate me for do that XD
@Mixin(ZumeKeyBind.class)
public class ZumeKeyBindsForge {
    @Shadow @Mutable @Final public KeyMapping value;

    @Redirect(
            method = "<init>(Ljava/lang/String;ILjava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V"
            )
    )
    private void modify$keyBindId(KeyMapping instance, String pName, InputConstants.Type pType, int pKeyCode, String pCategory) {
        if (pKeyCode == GLFW.GLFW_KEY_Z) {
            value = new KeyMapping(pName, pType, GLFW.GLFW_KEY_C, pCategory);
        }
    }
}
