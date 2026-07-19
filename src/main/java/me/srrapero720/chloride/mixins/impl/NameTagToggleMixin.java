package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class NameTagToggleMixin<T extends Entity> {

    // HIDE NAMETAGS PER CONFIG. PARENTHESES KEEP && FROM BINDING BEFORE ||: ITEM-FRAME AND DROPPED-ITEM
    // NAMETAGS HIDE ONLY WHEN THE items TOGGLE IS OFF, INSTEAD OF DROPPED ITEMS ALWAYS HIDING
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void chloride$hide(final T entity, final Component displayName, final PoseStack poseStack, final MultiBufferSource buffer, final int packedLight, final float partialTick, final CallbackInfo ci) {
        final Object self = this;
        if (!ChlorideConfig.nametags.items && (self instanceof ItemFrameRenderer || self instanceof ItemEntityRenderer)) {
            ci.cancel();
            return;
        }
        if (!ChlorideConfig.nametags.players && self instanceof PlayerRenderer) {
            ci.cancel();
            return;
        }
        if (!ChlorideConfig.nametags.entities) {
            ci.cancel();
        }
    }
}
