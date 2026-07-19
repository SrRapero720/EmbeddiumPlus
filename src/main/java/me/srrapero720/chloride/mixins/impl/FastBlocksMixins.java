package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.FastBlocks;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FastBlocksMixins {
    @Mixin(BedBlock.class)
    public static abstract class BedMixin extends BlockBehaviour {
        public BedMixin(final Properties pProperties) { super(pProperties); }

        // I DON'T LIKE DO THIS WITH MIXINS, BUT IS NECESSARY :P
        @Override
        @SuppressWarnings("deprecation")
        public boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
            return ChlorideConfig.fastBlocks.beds && neighborState.getBlock() instanceof BedBlock;
        }
    }

    // WE CAN APPLY the SAME MIXIN TO BOTH CLASSES
    @Mixin(value = { ChestBlock.class, EnderChestBlock.class })
    public static class ChestsMixin {
        @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
        private <T extends BlockEntity> void inject$removeTicker(final Level level, final BlockState state, final BlockEntityType<T> type, final CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
            if (FastBlocks.canUseOnChests() && ChlorideConfig.fastBlocks.chests) {
                cir.setReturnValue(null);
            }
        }
    }

    @Mixin(BlockEntityRenderDispatcher.class)
    public static class TileRenderMixin {
        @Inject(method = "getRenderer(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;", at = @At("HEAD"), cancellable = true)
        private <E extends BlockEntity, S extends BlockEntityRenderState> void inject$disableRenderer(final E blockEntity, final CallbackInfoReturnable<BlockEntityRenderer<E, S>> cir) {
            // FAST CHESTS (needs FLYWHEEL HANDLING)
            final Class<?> beClass = blockEntity.getClass();
            if (ChlorideConfig.fastBlocks.chests && FastBlocks.canUseOnChests()) {
                if (beClass == ChestBlockEntity.class || beClass == EnderChestBlockEntity.class) {
                    cir.setReturnValue(null);
                }
            }

            // FAST BEDS (OR BETTER BEDS)
            if (ChlorideConfig.fastBlocks.beds) {
                if (beClass == BedBlockEntity.class) {
                    cir.setReturnValue(null);
                }
            }
        }
    }
}
