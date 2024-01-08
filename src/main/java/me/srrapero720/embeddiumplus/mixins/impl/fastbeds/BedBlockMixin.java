package me.srrapero720.embeddiumplus.mixins.impl.fastbeds;

import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BedBlock.class)
public class BedBlockMixin extends HorizontalDirectionalBlock {

    protected BedBlockMixin(Properties pProperties) { super(pProperties); }

    @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
    private void inject$getRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
        if (EmbyConfig.fastBedsCache) {
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
