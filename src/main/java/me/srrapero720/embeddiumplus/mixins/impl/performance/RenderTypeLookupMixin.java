package me.srrapero720.embeddiumplus.mixins.impl.performance;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = ItemBlockRenderTypes.class)
public class RenderTypeLookupMixin {
    @Shadow private static boolean renderCutout;
    @Shadow @Final private static Map<Block, RenderType> TYPE_BY_BLOCK;
    @Shadow @Final private static Map<Fluid, RenderType> TYPE_BY_FLUID;

//    @Inject(method = "setRenderLayer(Lnet/minecraft/block/Block;Lnet/minecraft/client/renderer/RenderType;)Z", at = @At("HEAD"), remap = false, cancellable = true)
    private static void render(BlockState state, RenderType type, CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock) {
            cir.setReturnValue(renderCutout ? type == RenderType.cutoutMipped() : type == RenderType.solid());
        } else {
            RenderType rendertype;
            // Predicate<RenderType> renderType;
            // synchronized (RenderTypeLookup.class) {
                rendertype = TYPE_BY_BLOCK.get(block);
            // }
            cir.setReturnValue(rendertype != null ? rendertype.equals(type) : type == RenderType.solid());
        }
    }

//    @Inject(remap = false, at = @At("HEAD"), method = "canRenderInLayer(Lnet/minecraft/fluid/FluidState;Lnet/minecraft/client/renderer/RenderType;)Z", cancellable = true)
    private static void render(FluidState fluid, RenderType type, CallbackInfoReturnable<Boolean> cir) {
        RenderType rendertype;
        // synchronized (RenderTypeLookup.class) {
        rendertype = TYPE_BY_FLUID.get(fluid.getType());
        // }
        cir.setReturnValue(rendertype != null ? rendertype.equals(type) : type == RenderType.solid());
    }
}