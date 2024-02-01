package me.srrapero720.embeddiumplus.mixins.impl.fastmodels;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.fastmodels.ChestHandler;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public class TileRenderMixin {
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void inject$disableRenderer(E blockEntity, CallbackInfoReturnable<BlockEntityRenderer<E>> cir) {
        // FAST CHESTS (needs FLYWHEEL HANDLING)
        if (ChestHandler.canEnable() && EmbyConfig.fastChestsCache) {
            if ((blockEntity instanceof ChestBlockEntity) || (blockEntity instanceof EnderChestBlockEntity)) {
                cir.setReturnValue(null);
            }
        }

        // FAST BEDS (OR BETTER BEDS)
        if (EmbyConfig.fastBedsCache) {
            if (blockEntity instanceof BedBlockEntity) {
                cir.setReturnValue(null);
            }
        }
    }
}