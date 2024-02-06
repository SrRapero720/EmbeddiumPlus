package me.srrapero720.embeddiumplus.mixins.impl.zume.accessors;

import dev.nolij.zume.common.Zume;
import dev.nolij.zume.common.config.ZumeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Zume.class)
public interface ZumeAccessor {
    @Accessor
    static void setConfig(ZumeConfig config) {
        throw new IllegalStateException("Stub!");
    }

    @Accessor
    static void setInverseSmoothness(double value) {
        throw new IllegalStateException("Stub!");
    }

    @Accessor
    static void setToggle(boolean value) {
        throw new IllegalStateException("Stub!");
    }
}
