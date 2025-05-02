package me.srrapero720.chloride;

import me.srrapero720.chloride.features.ZoomFeature;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Mod(Chloride.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Chloride {
    public static final String ID = "chloride";
    public static final Logger LOGGER = LogManager.getLogger("chloride");
    public static final Marker IT = MarkerManager.getMarker("Main");
    public static Pack SOLID_BEDS_PACK;
    public static Pack SOLID_CHESTS_PACK;

    public Chloride() {
        if (FMLLoader.getDist().isClient()) {
            LOGGER.info(IT, "Chloride is here, lets make your experience taste-able");
        } else {
            LOGGER.info(IT, "Chloride is not intended to be on servers, loaded in inner mode");
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void load(final FMLClientSetupEvent event) {
        if (Tools.isModInstalled("xenon")) throw new RuntimeException("Xenon is incompatible with Chloride, please use Embeddium or Sodium instead");
        if (Tools.isModInstalled("embeddiumextras")) throw new RuntimeException("Embeddium/Sodium Extras is incompatible with Chloride, chloride replaces it");
        if (Tools.isModInstalled("embeddiumplus")) throw new RuntimeException("You have a old-duplicated version of chloride, please remove Embeddium++ (old chloride)");
        LOGGER.info("LOADED CHLORIDE");
    }

    @OnlyIn(Dist.CLIENT)
    public static void earlyLoad() {
        ChlorideConfig.load(FMLLoader.getGamePath().resolve("config"));
    }

    @SubscribeEvent
    public static void registerResourcePacks(AddPackFindersEvent e) {
        LOGGER.info("registered CHLORIDE packs");
        if (e.getPackType() == PackType.CLIENT_RESOURCES) {
            SOLID_BEDS_PACK = Pack.readMetaAndCreate(ID + "_solid_beds",
                    Component.literal("Chloride: Solid Beds"),
                    false,
                    id -> getPathResources(ID, "custom_packs/solid_beds"),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN);

            SOLID_CHESTS_PACK = Pack.readMetaAndCreate(ID + "_solid_chests",
                    Component.literal("Chloride: Solid Chests"),
                    false,
                    id -> getPathResources(ID, "custom_packs/solid_chests"),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN);

            e.addRepositorySource(consumer -> {
                consumer.accept(SOLID_BEDS_PACK);
                consumer.accept(SOLID_CHESTS_PACK);
            });
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerKeys(final RegisterKeyMappingsEvent event) {
        event.register(ZoomFeature.KEY);
    }

    private static PathPackResources getPathResources(String name, String path) {
        final IModFile modFile = ModList.get().getModFileById(ID).getFile();
        return new PathPackResources(name, true, modFile.findResource(path)) {
            @NotNull
            protected Path resolve(String... paths) {
                final String[] allPaths = new String[paths.length + 1];
                allPaths[0] = path;
                System.arraycopy(paths, 0, allPaths, 1, paths.length);
                return modFile.findResource(allPaths);
            }

            @Override
            public boolean isHidden() {
                return false;
            }
        };
    }
}