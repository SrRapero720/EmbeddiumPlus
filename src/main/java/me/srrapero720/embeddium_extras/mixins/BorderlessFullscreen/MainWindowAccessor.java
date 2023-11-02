package me.srrapero720.embeddium_extras.mixins.BorderlessFullscreen;

import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Window.class)
public interface MainWindowAccessor {
    @Accessor()
    void setDirty(boolean value);
}