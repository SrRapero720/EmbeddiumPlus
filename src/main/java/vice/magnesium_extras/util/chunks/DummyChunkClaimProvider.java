package vice.magnesium_extras.util.chunks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DummyChunkClaimProvider implements IChunkClaimProvider
{
    @Override
    public boolean isInClaimedChunk(World world, BlockPos pos)
    {
        return false;
    }
}
