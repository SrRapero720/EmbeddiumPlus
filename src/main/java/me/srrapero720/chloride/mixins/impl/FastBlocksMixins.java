package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.foundation.fastmodels.FastModels;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FastBlocksMixins {
    @Mixin(BedBlock.class)
    public abstract static class BedMixin extends BlockBehaviour {
        public BedMixin(Properties pProperties) { super(pProperties); }

        @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
        private void inject$replaceRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
            if (ChlorideConfig.fastBeds) {
                cir.setReturnValue(RenderShape.MODEL);
            }
        }

        // I DON'T LIKE DO THIS WITH MIXINS, BUT IS NECESSARY :P
        @Override
        @SuppressWarnings("deprecation")
        public boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
            return neighborState.getBlock() instanceof BedBlock;
        }
    }

    // WE CAN APPLY the SAME MIXIN TO BOTH CLASSES
    @Mixin(value = { ChestBlock.class, EnderChestBlock.class })
    public static class ChestsMixin {
        @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
        private <T extends BlockEntity> void inject$removeTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
            if (FastModels.canUseOnChests() && ChlorideConfig.fastChests) {
                cir.setReturnValue(null);
            }
        }

        @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
        private void inject$replaceRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
            if (FastModels.canUseOnChests() && ChlorideConfig.fastChests) {
                cir.setReturnValue(RenderShape.MODEL);
            }
        }
    }

    @Mixin(BlockEntityRenderDispatcher.class)
    public static class TileRenderMixin {
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
}
