package me.srrapero720.chloride.mixins.impl.sodium;

import net.caffeinemc.mods.sodium.client.gpu.device.backend.DrawBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawBackend.class)
public interface DrawBackendAccessor {
    @Mutable
    @Accessor("BACKEND")
    static void setBackend(final DrawBackend backend) {
        throw new AssertionError();
    }

    @Invoker("chooseBackend")
    static DrawBackend invokeChooseBackend() {
        throw new AssertionError();
    }
}
