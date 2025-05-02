package me.srrapero720.chloride.mixins.impl.leaves_culling;

import me.srrapero720.chloride.features.accessors.IGameLeaves;
import me.srrapero720.chloride.features.LeavesFeatures;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("deprecation")
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends Block implements IGameLeaves {
    // TODO: cull less leaves (maybe delegate to 2.0.0)
    @Unique private ResourceLocation embPlus$resLoc;
    @Unique private int leaves_neighbor;

    public LeavesBlockMixin(final Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
        if (neighborState.getBlock() instanceof final IGameLeaves leaves) {
            return LeavesFeatures.should(this.chloride$cast(), this, (LeavesBlock) leaves, leaves) || super.skipRendering(state, neighborState, direction);
        }
        return super.skipRendering(state, neighborState, direction);
    }

    @Override
    public ResourceLocation chloride$getRL() {
        return this.embPlus$resLoc != null ? this.embPlus$resLoc : (this.embPlus$resLoc = ForgeRegistries.BLOCKS.getKey(this));
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



