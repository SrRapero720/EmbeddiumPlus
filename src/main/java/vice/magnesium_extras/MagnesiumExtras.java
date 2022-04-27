package vice.magnesium_extras;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.block.Block;
import net.minecraft.client.AbstractOption;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.features.Zoom.ZoomUtils;
import vice.magnesium_extras.keybinds.KeyboardInput;
import vice.magnesium_extras.util.chunks.DummyChunkClaimProvider;
import vice.magnesium_extras.util.chunks.IChunkClaimProvider;

import java.lang.reflect.Field;

@Mod("magnesium_extras")
public class MagnesiumExtras
{
    public static final String MODID = "magnesium_extras";
    public static final Logger LOGGER = LogManager.getLogger();
    public static IChunkClaimProvider chunkClaimProvider;

    public MagnesiumExtras() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        MagnesiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("magnesium_extras.toml"));

        //MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get()
                .registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        try {
            final Field sodiumOptsField = SodiumGameOptionPages.class.getDeclaredField("sodiumOpts");
            sodiumOptsField.setAccessible(true);
            SodiumOptionsStorage sodiumOpts = (SodiumOptionsStorage) sodiumOptsField.get(null);
            sodiumOpts.getData().experimental.displayFps = false;
            sodiumOpts.save();
        }
        catch (Throwable t) {
            LOGGER.error("Could not retrieve sodiumOptsField");
        }


    }

    private void setup(final FMLCommonSetupEvent event)
    {
        if (ModList.get().isLoaded("ftbchunks"))
        {
            try
            {
                chunkClaimProvider = Class.forName("vice.magnesium_extras.util.chunks.ActiveChunkClaimProvider").asSubclass(IChunkClaimProvider.class).newInstance();
                LOGGER.info("Found FTB Chunks, enabling MagnesiumExtras integration.");
            }
            catch (Exception e)
            {
                LOGGER.error(e);
            }
        }
        else
        {
            chunkClaimProvider = new DummyChunkClaimProvider();
        }
    }

}