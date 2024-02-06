package me.srrapero720.embeddiumplus.mixins.impl.zume.accessors;

import dev.nolij.zume.common.config.ZumeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;

@Mixin(ZumeConfig.class)
public interface ZumeConfigAccessor {
    @Invoker
    void callWriteToFile(File file);
}
