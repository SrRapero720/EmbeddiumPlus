package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CloudRenderer.class)
public class CloudHeightMixin {
    @Unique
    private static final float CHLORIDE_VANILLA_CLOUD_HEIGHT = 192.33f;

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    public float modify$cloudHeight(final float height) {
        return height == CHLORIDE_VANILLA_CLOUD_HEIGHT ? ChlorideConfig.world.cloudsHeight : height;
    }
}