package me.srrapero720.embeddiumplus.mixins.impl.frames;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface FpsAccessorMixin {
    @Accessor
    static int getFps() { return 0; }
}