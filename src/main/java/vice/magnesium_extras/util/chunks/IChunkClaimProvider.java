package vice.magnesium_extras.util.chunks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChunkClaimProvider
{
    public boolean isInClaimedChunk(World world, BlockPos pos);
}
