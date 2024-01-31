package me.srrapero720.embeddiumplus.mixins;

import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EmbyMixinPlugin implements IMixinConfigPlugin {
    private static final Marker IT = MarkerManager.getMarker("MixinPlugin");

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String target, String mixin) {
        if (FMLEnvironment.dist.isDedicatedServer()) return false;

        final var mixinName = EmbyTools.getLastValue(mixin.split("\\."));

        if (mixin.endsWith("fastchests.BERenderDispatcherMixin") || mixin.endsWith("fastchests.ChestBlockMixin") || mixin.endsWith("fastchests.EnderChestBlockMixin")) {
            if (EmbyTools.isModInstalled("enhancedblockentities")) {
                LOGGER.warn(IT, "Disabled FastChest feature mixin '{}' because EBE is installed", mixinName);
                return false;
            } else {
                return true;
            }
        }

        if (mixin.endsWith("JeiOverlayMixin")) {
            if (!EmbyTools.isModInstalled("jei")) {
                LOGGER.warn(IT, "Disabled JeiOverlayMixin because JEI is not installed");
                return false;
            } else {
                LOGGER.warn(IT, "Enabled JeiOverlayMixin...");
                if (EmbyTools.isModInstalled("rei")) throw new IllegalStateException("REI and JEI detected... forcing shutting down");
                return true;
            }
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}