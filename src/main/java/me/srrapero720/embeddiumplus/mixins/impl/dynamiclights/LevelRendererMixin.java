package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderLevel")
    public void render(PoseStack outlinebuffersource, float i, long j, boolean k, Camera l, GameRenderer i1, LightTexture lightTexture, Matrix4f multibuffersource, CallbackInfo ci) {
        Minecraft.getInstance().getProfiler().popPush("dynamic_lighting");
        DynLightsPlus.get().updateAll((LevelRenderer) (Object) this);
    }
}