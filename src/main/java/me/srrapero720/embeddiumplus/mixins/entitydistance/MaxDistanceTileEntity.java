package me.srrapero720.embeddiumplus.mixins.entitydistance;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import me.srrapero720.embeddiumplus.util.DistanceUtility;

@Mixin(BlockEntityRenderDispatcher.class)
public class MaxDistanceTileEntity {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends BlockEntity> void render(E entity, float val, PoseStack matrix, MultiBufferSource p_228850_4_, CallbackInfo ci) {
        if (!EmbeddiumPlusConfig.enableDistanceChecks.get()) return;

        BlockEntityRenderDispatcher thisObj = (BlockEntityRenderDispatcher) (Object) this;

        if (!DistanceUtility.isEntityWithinDistance(
                entity.getBlockPos(),
                thisObj.camera.getPosition(),
                EmbeddiumPlusConfig.maxTileEntityRenderDistanceY.get(),
                EmbeddiumPlusConfig.maxTileEntityRenderDistanceSquare.get()
        )) {
            ci.cancel();
        }
    }

}