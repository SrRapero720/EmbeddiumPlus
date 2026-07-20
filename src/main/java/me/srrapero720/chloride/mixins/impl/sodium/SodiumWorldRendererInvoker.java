package me.srrapero720.chloride.mixins.impl.sodium;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SodiumWorldRenderer.class)
public interface SodiumWorldRendererInvoker {
    @Invoker("deleteRendererState")
    void invokeDeleteRendererState();
}
