package me.srrapero720.chloride.mixins.impl.mipmap;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelManager.class)
public interface ModelManagerAccessor {
    @Invoker ModelBakery callPrepare(ResourceManager p_119413_, ProfilerFiller p_119414_);
    @Invoker void callApply(ModelBakery modelLoader, ResourceManager resourceManager, ProfilerFiller profiler);
}