package me.srrapero720.embeddiumplus.mixins.impl.fastchests;

import me.srrapero720.embeddiumplus.internal.EmbPlusTools;
import me.srrapero720.embeddiumplus.internal.EmbyConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
    private <T extends BlockEntity> void removeTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (EmbPlusTools.flwIsOff() && EmbyConfig.fastChestsCache) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void replaceRenderType(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
        if (EmbPlusTools.flwIsOff() && EmbyConfig.fastChestsCache) {
            cir.setReturnValue(RenderShape.MODEL);
        }
    }
}