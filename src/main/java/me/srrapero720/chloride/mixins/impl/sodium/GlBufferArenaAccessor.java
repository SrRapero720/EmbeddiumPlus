package me.srrapero720.chloride.mixins.impl.sodium;

import com.mojang.blaze3d.buffers.GpuBuffer;
import net.caffeinemc.mods.sodium.client.gpu.arena.GlBufferArena;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlBufferArena.class)
public interface GlBufferArenaAccessor {
    @Accessor("freeBuffers")
    static GpuBuffer[] freeBuffers() {
        throw new AssertionError();
    }

    @Accessor("freeBufferCount")
    static void setFreeBufferCount(final int count) {
        throw new AssertionError();
    }
}
