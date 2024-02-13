package me.srrapero720.embeddiumplus.mixins;

import me.srrapero720.embeddiumplus.EmbyTools;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (targetClassName.contains("zume.Zume") && !EmbyTools.isModInstalled("zume")) return false;
        if (targetClassName.contains("jei.Jei") && !EmbyTools.isModInstalled("jei")) return false;

        // go ahead
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
