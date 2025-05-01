package me.srrapero720.chloride.mixins.impl.leaves_culling;

import me.srrapero720.chloride.api.leaves.IGameLeaves;
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

    public LeavesBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        if (neighborState.getBlock() instanceof IGameLeaves leaves) {
            return LeavesFeatures.should(chloride$cast(), this, (LeavesBlock) leaves, leaves) || super.skipRendering(state, neighborState, direction);
        }
        return super.skipRendering(state, neighborState, direction);
    }

    @Override
    public ResourceLocation chloride$getRL() {
        return embPlus$resLoc != null ? embPlus$resLoc : (embPlus$resLoc = ForgeRegistries.BLOCKS.getKey(this));
    }

    @Override
    public int chloride$neighborCount() {
        return leaves_neighbor;
    }

    @Unique
    private LeavesBlock chloride$cast() {
        return (LeavesBlock) (Object) this;
    }
}



