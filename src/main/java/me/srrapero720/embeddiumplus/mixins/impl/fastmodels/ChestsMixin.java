package me.srrapero720.embeddiumplus.mixins.impl.fastmodels;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.fastmodels.FastModels;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// WE CAN APPLY the SAME MIXIN TO BOTH CLASSES
@Mixin(value = { ChestBlock.class, EnderChestBlock.class })
public class ChestsMixin {
    @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
    private <T extends BlockEntity> void inject$removeTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (FastModels.canUseOnChests() && EmbyConfig.fastChestsCache) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void inject$replaceRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
        if (FastModels.canUseOnChests() && EmbyConfig.fastChestsCache) {
            cir.setReturnValue(RenderShape.MODEL);
        }
    }
}