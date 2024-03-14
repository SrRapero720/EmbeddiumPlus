package me.srrapero720.embeddiumplus;

import me.srrapero720.embeddiumplus.util.XenonInstalledException;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EmbeddiumPlus.ID)
public class EmbeddiumPlus {
    public static final String ID = "embeddiumplus";
    public static final Logger LOGGER = LogManager.getLogger("Embeddium++");

    public EmbeddiumPlus() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGameStarting);
    }

    @SubscribeEvent
    public void onGameStarting(FMLClientSetupEvent event) {
        if (EmbyTools.isModInstalled("xenon")) throw new XenonInstalledException();
    }
}