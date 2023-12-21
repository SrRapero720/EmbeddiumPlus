package me.srrapero720.embeddiumplus.mixins;

import me.srrapero720.embeddiumplus.EmbPlusTools;
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

        final var mixinName = EmbPlusTools.getLastValue(mixin.split("\\."));
        return switch (mixinName) {
            case "ExtendedServerViewDistance" -> {
                LOGGER.warn(IT,"Disabling ExtendedServerViewDistance mixin due to Farsight/Bobby being installed.");
                yield  false;
            }

            case "JeiOverlayMixin" -> {
                if (!isPresent("jei")) {
                    LOGGER.warn(IT, "Disabling JeiOverlayMixin due to JEI being NOT installed");
                    yield false;
                } else {
                    LOGGER.warn(IT, "Enabling JeiOverlayMixin...");
                    if (isPresent("rei")) throw new IllegalStateException("REI and JEI detected... forcing shutting down");
                    yield true;
                }
            }

            default -> true;
        };
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