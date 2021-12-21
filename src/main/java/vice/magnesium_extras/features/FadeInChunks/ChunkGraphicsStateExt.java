package vice.magnesium_extras.features.FadeInChunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.util.Objects;

public interface ChunkGraphicsStateExt {
    ChunkRenderContainer<?> getContainer();

    float getLoadTime();
    void setLoadTime(float time);

    default float getFadeInProgress(float currentTime) {
        String mode = MagnesiumExtrasConfig.fadeInQuality.get();


        float fadeTime = 2.5f;
        if (!Objects.equals(mode, "FANCY"))
        {
            if (Objects.equals(mode, "OFF"))
                fadeTime = Float.POSITIVE_INFINITY;
            else if (Objects.equals(mode, "FAST"))
                fadeTime = 5f;
        }

        return (currentTime - getLoadTime()) * fadeTime;
        /* Custom fade progress for icon creation. Seed: -8255308814763093104
        ChunkGraphicsState state = (ChunkGraphicsState) this;
        int dx = Math.abs(state.getX() - 56) / 16;
        int dz = Math.abs(state.getZ() - 24) / 16;
        int d = Math.max(dx, dz);
        return d == 0 ? 1 : d == 1 ? 0.8f : 0;
        */
    }

    static ChunkGraphicsStateExt ext(ChunkGraphicsState self) {
        return (ChunkGraphicsStateExt) self;
    }
}
