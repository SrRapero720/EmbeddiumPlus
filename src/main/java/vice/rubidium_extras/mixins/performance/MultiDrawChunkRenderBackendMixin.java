package vice.rubidium_extras.mixins.performance;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferTarget;
import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.DrawCommandList;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkCameraContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.time.Duration;
import java.time.Instant;

/*@Mixin(value = MultidrawChunkRenderBackend.class)
public abstract class MultiDrawChunkRenderBackendMixin
{
    @Shadow @Final private ChunkRegionManager<MultidrawGraphicsState> bufferManager;

    @Shadow protected abstract void setupDrawBatches(CommandList commandList, ChunkRenderListIterator<MultidrawGraphicsState> it, ChunkCameraContext camera);

    @Shadow protected abstract void buildCommandBuffer();

    @Shadow @Final private GlMutableBuffer commandBuffer;

    @Shadow @Final private IndirectCommandBufferVector commandClientBufferBuilder;

    @Shadow @Final private ObjectArrayList<ChunkRegion<MultidrawGraphicsState>> pendingBatches;

    @Overwrite(remap = false)
    public void render(CommandList commandList, ChunkRenderListIterator<MultidrawGraphicsState> renders, ChunkCameraContext camera)
    {
        bufferManager.cleanup();

        setupDrawBatches(commandList, renders, camera);
        buildCommandBuffer();

        if (commandBuffer != null) {
            commandList.bindBuffer(GlBufferTarget.DRAW_INDIRECT_BUFFER, this.commandBuffer);
            commandList.uploadData(this.commandBuffer, commandClientBufferBuilder.getBuffer());
        }

        long pointer = this.commandBuffer == null ? this.commandClientBufferBuilder.getBufferAddress() : 0L;

        Instant startedRender = Instant.now();
        for (ChunkRegion<?> region : pendingBatches)
        {
            ChunkDrawCallBatcher batch = region.getDrawBatcher();

            try (DrawCommandList drawCommandList = commandList.beginTessellating(region.getTessellation())) {
                drawCommandList.multiDrawArraysIndirect(pointer, batch.getCount(), 0);
            }

            pointer += batch.getArrayLength();

            if (Duration.between(startedRender, Instant.now()).toMillis() > 10)
            {
                return;
            }
        }

        pendingBatches.clear();
    }
}
*/