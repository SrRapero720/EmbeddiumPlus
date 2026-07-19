package me.srrapero720.chloride.mixins.impl.leaves_culling;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.srrapero720.chloride.api.IGameLeaves;
import me.srrapero720.chloride.impl.LeavesCulling;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LeavesBlock.class, priority = 1100)
public class LeavesBlockMixin extends Block implements IGameLeaves {
    // TODO: cull less leaves (maybe delegate to 2.0.0)
    @Unique private ResourceLocation chloride$id;
    @Unique private int leaves_neighbor;

    public LeavesBlockMixin(final Properties pProperties) {
        super(pProperties);
    }

    // EMBEDDIUM (SODIUM FORK) OVERRIDES LeavesBlock#skipRendering IN ITS OWN MIXIN (DEFAULT PRIORITY 1000).
    // A SECOND OVERRIDE HERE WOULD COLLIDE, SO WE RAISE OUR PRIORITY TO APPLY AFTER EMBEDDIUM AND AUGMENT ITS RESULT
    @ModifyReturnValue(method = "skipRendering", at = @At("RETURN"))
    private boolean chloride$cullLeaves(final boolean sodiumReturned, final BlockState state, final BlockState neighborState, final Direction direction) {
        if (sodiumReturned) return true;
        return neighborState.getBlock() instanceof final IGameLeaves neighbor && LeavesCulling.should(this.chloride$cast(), this, (LeavesBlock) neighbor, neighbor);
    }

    @Override
    public ResourceLocation chloride$getRL() {
        return this.chloride$id != null ? this.chloride$id : (this.chloride$id = ForgeRegistries.BLOCKS.getKey(this));
    }

    @Override
    public int chloride$neighborCount() {
        return this.leaves_neighbor;
    }

    @Unique
    private LeavesBlock chloride$cast() {
        return (LeavesBlock) (Object) this;
    }
}
