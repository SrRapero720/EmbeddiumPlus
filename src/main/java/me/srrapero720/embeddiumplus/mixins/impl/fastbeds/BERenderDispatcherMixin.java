package me.srrapero720.embeddiumplus.mixins.impl.fastbeds;

import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public class BERenderDispatcherMixin {

    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void inject$getRenderer(E blockEntity, CallbackInfoReturnable<BlockEntityRenderer<E>> cir) {
        if (EmbyConfig.fastBedsCache) {
            if (blockEntity instanceof BedBlockEntity) {
                cir.setReturnValue(null);
            }
        }
    }
}
