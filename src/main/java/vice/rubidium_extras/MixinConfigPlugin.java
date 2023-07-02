package vice.rubidium_extras;

import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin
{
    private boolean hasFarSight() { return LoadingModList.get().getModFileById("farsight_view") != null; }
    private boolean hasBobby() { return LoadingModList.get().getModFileById("bobby") != null; }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!FMLEnvironment.dist.isClient())
            return false;


        if (mixinClassName.contains("ExtendedServerViewDistance") && (hasFarSight() || hasBobby())) {
            MagnesiumExtras.LOGGER.info("Disabling ExtendedServerViewDistance mixin due to Farsight/Bobby being installed.");
            return false;
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
