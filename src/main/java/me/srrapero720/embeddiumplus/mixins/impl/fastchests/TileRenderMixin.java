package me.srrapero720.embeddiumplus.mixins.impl.fastchests;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public class TileRenderMixin {
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void inject$disableRenderer(E blockEntity, CallbackInfoReturnable<BlockEntityRenderer<E>> cir) {
        if (!EmbyTools.canUseFastChests() || !EmbyConfig.fastChestsCache) return;

        final Class<?> beClazz = blockEntity.getClass();
        if ((blockEntity instanceof ChestBlockEntity) || (blockEntity instanceof EnderChestBlockEntity)) {
            cir.setReturnValue(null);
        }
        if (beClazz == ChestBlockEntity.class || beClazz == TrappedChestBlockEntity.class || beClazz == EnderChestBlockEntity.class) {
            cir.setReturnValue(null);
        }
    }
}