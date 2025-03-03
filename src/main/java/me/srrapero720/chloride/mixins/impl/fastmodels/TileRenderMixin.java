package me.srrapero720.chloride.mixins.impl.fastmodels;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.foundation.fastmodels.FastModels;
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
        Class<?> beClass = blockEntity.getClass();
        if (ChlorideConfig.fastChests && FastModels.canUseOnChests()) {
            if (beClass == ChestBlockEntity.class || beClass == EnderChestBlockEntity.class) {
                cir.setReturnValue(null);
            }
        }

        // FAST BEDS (OR BETTER BEDS)
        if (ChlorideConfig.fastBeds) {
            if (beClass == BedBlockEntity.class) {
                cir.setReturnValue(null);
            }
        }
    }
}