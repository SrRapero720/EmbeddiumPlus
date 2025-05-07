package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Chloride.ID)
public class FastBlocks {
    public static Pack SOLID_BEDS_PACK;
    public static Pack SOLID_CHESTS_PACK;

    public static boolean canUseOnChests() {
        return !Chloride.installed("enhancedblockentities");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChestSettingUpdateEvent(FastModelSettingsUpdate.ChestEvent e) {
        if (e.isEnabled()) {
            Minecraft.getInstance().getResourcePackRepository().addPack(SOLID_CHESTS_PACK.getId());
        } else {
            Minecraft.getInstance().getResourcePackRepository().removePack(SOLID_CHESTS_PACK.getId());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBedSettingUpdateEvent(FastModelSettingsUpdate.BedEvent e) {
        if (e.isEnabled()) {
            Minecraft.getInstance().getResourcePackRepository().addPack(SOLID_BEDS_PACK.getId());
        } else {
            Minecraft.getInstance().getResourcePackRepository().removePack(SOLID_BEDS_PACK.getId());
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Chloride.ID)
    public static final class ModEvents {

        @SubscribeEvent
        public static void registerResourcePacks(AddPackFindersEvent e) {
            Chloride.LOGGER.info("Register CHLORIDE packs");
            if (e.getPackType() == PackType.CLIENT_RESOURCES) {
                SOLID_BEDS_PACK = Pack.readMetaAndCreate(Chloride.ID + "_solid_beds",
                        Component.literal("Chloride: Solid Beds"),
                        false,
                        id -> getPathResources(Chloride.ID, "custom_packs/solid_beds"),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN);

                SOLID_CHESTS_PACK = Pack.readMetaAndCreate(Chloride.ID + "_solid_chests",
                        Component.literal("Chloride: Solid Chests"),
                        false,
                        id -> getPathResources(Chloride.ID, "custom_packs/solid_chests"),
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.TOP,
                        PackSource.BUILT_IN);

                e.addRepositorySource(consumer -> {
                    consumer.accept(SOLID_BEDS_PACK);
                    consumer.accept(SOLID_CHESTS_PACK);
                });
            }
        }

    }

    private static PathPackResources getPathResources(String name, String path) {
        final IModFile modFile = ModList.get().getModFileById(Chloride.ID).getFile();
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
                return true;
            }
        };
    }
}
