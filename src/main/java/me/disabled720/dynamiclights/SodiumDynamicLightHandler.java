package me.disabled720.dynamiclights;

import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface SodiumDynamicLightHandler {
    // Stores the current light position being used by ArrayLightDataCache#get
    // We use ThreadLocal because Sodium's chunk builder is multithreaded, otherwise it will break
    // catastrophically.
    ThreadLocal<BlockPos.MutableBlockPos> lambdynlights$pos = ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

    static int lambdynlights$getLightmap(BlockPos pos, int word, int lightmap) {
        if (!LambDynLights.isEnabled())
            return lightmap;

        // Equivalent to world.getBlockState(pos).isOpaqueFullCube(world, pos)
        if (LightDataAccess.unpackFO(word))
            return lightmap;

        double dynamic = LambDynLights.get().getDynamicLightLevel(pos);
        return LambDynLights.get().getLightmapWithDynamicLight(dynamic, lightmap);
    }
}