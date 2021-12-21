package vice.magnesium_extras.features.FadeInChunks;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;

public interface ChunkDrawParamsVectorExt {
    /**
     * Pushes the fadeInProgress parameter onto the buffer.
     * This must be called after {@link ChunkDrawParamsVector#pushChunkDrawParams}!
     */
    void pushChunkDrawParamFadeIn(float progress);

    static ChunkDrawParamsVectorExt ext(ChunkDrawParamsVector self) {
        return (ChunkDrawParamsVectorExt) self;
    }
}
