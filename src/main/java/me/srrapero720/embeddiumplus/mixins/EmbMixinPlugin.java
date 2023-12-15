package me.srrapero720.embeddiumplus.mixins;

import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

public class EmbMixinPlugin implements IMixinConfigPlugin {
    private static final Marker IT = MarkerManager.getMarker("MixinPlugin");
    private static boolean isPresent(String modid) { return FMLLoader.getLoadingModList().getModFileById(modid) != null; }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String target, String mixin) {
        if (FMLEnvironment.dist.isDedicatedServer()) return false;


        if (mixin.contains("ExtendedServerViewDistance") && (isPresent("farsight_view") || isPresent("bobby"))) {
            LOGGER.warn(IT,"Disabling ExtendedServerViewDistance mixin due to Farsight/Bobby being installed.");
            return false;
        }
        if (mixin.contains("JeiOverlayMixin") && !isPresent("jei")) {
            LOGGER.warn(IT, "Disabling JeiOverlay mixin due to JEI being NOT installed");
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}