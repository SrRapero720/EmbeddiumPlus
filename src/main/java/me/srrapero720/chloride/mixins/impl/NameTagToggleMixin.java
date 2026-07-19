package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class NameTagToggleMixin {
    @Inject(method = "submitNameTag", at = @At("HEAD"), cancellable = true)
    private void chloride$hide(final EntityRenderState renderState, final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final CameraRenderState cameraRenderState, final CallbackInfo ci) {
        final Object self = this;
        if (!ChlorideConfig.nametags.items && (self instanceof ItemFrameRenderer || self instanceof ItemEntityRenderer)) {
            ci.cancel();
            return;
        }
        if (!ChlorideConfig.nametags.players && self instanceof AvatarRenderer) {
            ci.cancel();
            return;
        }
        if (!ChlorideConfig.nametags.entities) {
            ci.cancel();
        }
    }
}
