package me.srrapero720.chloride.mixins.impl.leaves_culling;

import me.srrapero720.chloride.features.leaves_culling.ICulleableLeaves;
import me.srrapero720.chloride.features.leaves_culling.LeavesCulling;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.NeoForgeRegistriesSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("deprecation")
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends Block implements ICulleableLeaves {
    // TODO: cull less leaves (maybe delegate to 2.0.0)
    @Unique private ResourceLocation embPlus$resLoc;
    @Unique private int leaves_neighbor;

    public LeavesBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        if (adjacentState.getBlock() instanceof ICulleableLeaves leaves) {
            return LeavesCulling.should(embplus$cast(), this, (LeavesBlock) leaves, leaves) || super.skipRendering(state, adjacentState, direction);
        }
        return super.skipRendering(state, adjacentState, direction);
    }

    @Override
    public ResourceLocation embplus$getResourceLocation() {
        return embPlus$resLoc != null ? embPlus$resLoc : (embPlus$resLoc = BuiltInRegistries.BLOCK.getKey(this));
    }

    @Override
    public int embplus$activeNeighbors() {
        return leaves_neighbor;
    }

    public LeavesBlock embplus$cast() {
        return (LeavesBlock) (Object) this;
    }
}



