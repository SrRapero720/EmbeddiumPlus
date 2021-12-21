package vice.magnesium_extras.util.chunks;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActiveChunkClaimProvider implements IChunkClaimProvider
{
    @Override
    public boolean isInClaimedChunk(World world, BlockPos pos)
    {
        if (!FTBChunksAPI.isManagerLoaded())
            return true;

        ClaimedChunk chunk = FTBChunksAPI.getManager().getChunk(new ChunkDimPos(world, pos));

        return chunk != null;
    }
}
