package me.srrapero720.chloride.mixins.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.chloride.Chloride;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @WrapOperation(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fpsString:Ljava/lang/String;"))
    private void inject$init(final Minecraft instance, final String value, final Operation<Void> original) {
        original.call(instance, value);
        Chloride.earlyLoad();
    }
}
