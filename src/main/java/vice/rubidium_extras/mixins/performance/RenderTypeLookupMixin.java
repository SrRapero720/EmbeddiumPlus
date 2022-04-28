package vice.rubidium_extras.mixins.performance;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.IRegistryDelegate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Predicate;
/*
@Mixin(value = ItemBlockRenderTypes.class)
public class RenderTypeLookupMixin
{
    @Shadow private static boolean renderCutout;

    @Shadow @Final private static Map<IRegistryDelegate<Block>, Predicate<RenderType>> blockRenderChecks;

    @Shadow @Final private static Map<IRegistryDelegate<Fluid>, Predicate<RenderType>> fluidRenderChecks;

    @Inject(remap = false, at = @At("HEAD"), method = "canRenderInLayer(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/renderer/RenderType;)Z", cancellable = true)
    private static void render(BlockState state, RenderType type, CallbackInfoReturnable<Boolean> cir)
    {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock)
        {
            cir.setReturnValue(renderCutout ? type == RenderType.cutoutMipped() : type == RenderType.solid());
        }
        else
        {
            Predicate<RenderType> rendertype;
            //synchronized (RenderTypeLookup.class) {
                rendertype = blockRenderChecks.get(block.delegate);
            //}
            cir.setReturnValue(rendertype != null ? rendertype.test(type) : type == RenderType.solid());
        }
    }

    @Inject(remap = false, at = @At("HEAD"), method = "canRenderInLayer(Lnet/minecraft/fluid/FluidState;Lnet/minecraft/client/renderer/RenderType;)Z", cancellable = true)
    private static void render(FluidState fluid, RenderType type, CallbackInfoReturnable<Boolean> cir)
    {
        Predicate<RenderType> rendertype;
        //synchronized (RenderTypeLookup.class) {
        rendertype = fluidRenderChecks.get(fluid.getType().delegate);
        //}
        cir.setReturnValue(rendertype != null ? rendertype.test(type) : type == RenderType.solid());
    }
}
*/