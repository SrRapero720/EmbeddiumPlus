package me.srrapero720.embeddiumplus.features.dynlights.accessors;

import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface EmbedtDynamicLightHandler {
    // Stores the current light position being used by ArrayLightDataCache#get
    // We use ThreadLocal because Sodium's chunk builder is multithreaded, otherwise it will break
    // catastrophically.
    ThreadLocal<BlockPos.MutableBlockPos> lambdynlights$pos = ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

    static int lambdynlights$getLightmap(BlockPos pos, int word, int lightmap) {
        if (!DynLightsPlus.isEnabled())
            return lightmap;

        // Equivalent to world.getBlockState(pos).isOpaqueFullCube(world, pos)
        if (LightDataAccess.unpackFO(word))
            return lightmap;

        double dynamic = DynLightsPlus.get().getDynamicLightLevel(pos);
        return DynLightsPlus.get().getLightmapWithDynamicLight(dynamic, lightmap);
    }
}