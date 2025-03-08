package me.srrapero720.chloride.mixins.impl.accessors;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Window.class)
public interface WindowAccessors {
    @Accessor
    void setDirty(boolean value);
}