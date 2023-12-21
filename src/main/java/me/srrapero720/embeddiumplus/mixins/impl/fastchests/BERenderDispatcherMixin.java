package me.srrapero720.embeddiumplus.mixins.impl.fastchests;

import me.srrapero720.embeddiumplus.EmbPlusConfig;
import me.srrapero720.embeddiumplus.EmbPlusTools;
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
public class BERenderDispatcherMixin {
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void disableChestRender(E blockEntity, CallbackInfoReturnable<BlockEntityRenderer<E>> cir) {
        if (EmbPlusTools.flwIsOff() &&  EmbPlusConfig.fastChestsEnabled.get()) {
            Class<?> beClass = blockEntity.getClass();

            if (beClass == ChestBlockEntity.class || beClass == TrappedChestBlockEntity.class || beClass == EnderChestBlockEntity.class ||
                    beClass.getSuperclass().getName().equals("io.github.cyberanner.ironchests.blocks.blockentities.GenericChestEntity")) {
                cir.setReturnValue(null);
            }
        }
    }
}