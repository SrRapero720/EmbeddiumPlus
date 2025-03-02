package me.srrapero720.chloride.mixins.impl.entitydistance;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.EmbyConfig;
import me.srrapero720.chloride.EmbyTools;
import me.srrapero720.chloride.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class TileDispatcherMixin {
    @Shadow public Camera camera;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends BlockEntity> void render(E tile, float val, PoseStack matrix, MultiBufferSource bufferSource, CallbackInfo ci) {
        if (!EmbyConfig.tileEntityDistanceCullingCache) return;

        boolean isWhitelisted = ((IWhitelistCheck) tile.getType()).embPlus$isWhitelisted();
        if (!isWhitelisted && !EmbyTools.isEntityInRange(tile.getBlockPos(), camera.getPosition(),
                EmbyConfig.tileEntityCullingDistanceYCache,
                EmbyConfig.tileEntityCullingDistanceXCache)
        ) {
            ci.cancel();
        }
    }
}