package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.nio.file.Path;
import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT, modid = Chloride.ID)
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

    @EventBusSubscriber(value = Dist.CLIENT, modid = Chloride.ID)
    public static final class ModEvents {

        @SubscribeEvent
        public static void registerResourcePacks(AddPackFindersEvent e) {
            Chloride.LOGGER.info("Registering CHLORIDE built-in packs");
            if (e.getPackType() == PackType.CLIENT_RESOURCES) {
                final IModFile modFile = ModList.get().getModFileById(Chloride.ID).getFile();

                SOLID_BEDS_PACK = Pack.readMetaAndCreate(
                        new PackLocationInfo(Chloride.ID + "_solid_beds", Component.literal("Chloride: Solid Chests"), PackSource.BUILT_IN, Optional.empty()),
                        new Pack.ResourcesSupplier() {
                            @Override
                            public PackResources openPrimary(PackLocationInfo p_326301_) {
                                return getPathResources(p_326301_, modFile.getFilePath().resolve("custom_packs/solid_beds"));
                            }

                            @Override
                            public PackResources openFull(PackLocationInfo p_326241_, Pack.Metadata p_325959_) {
                                return getPathResources(p_326241_, modFile.getFilePath().resolve("custom_packs/solid_beds"));

                            }
                        },
                        PackType.CLIENT_RESOURCES,
                        new PackSelectionConfig(false, Pack.Position.TOP, true)
                );
                SOLID_CHESTS_PACK = Pack.readMetaAndCreate(
                        new PackLocationInfo(Chloride.ID + "_solid_chests", Component.literal("Chloride: Solid Chests"), PackSource.BUILT_IN, Optional.empty()),
                        new Pack.ResourcesSupplier() {
                            @Override
                            public PackResources openPrimary(PackLocationInfo p_326301_) {
                                return getPathResources(p_326301_, modFile.getFilePath().resolve("custom_packs/solid_chests"));
                            }

                            @Override
                            public PackResources openFull(PackLocationInfo p_326241_, Pack.Metadata p_325959_) {
                                return getPathResources(p_326241_, modFile.getFilePath().resolve("custom_packs/solid_chests"));

                            }
                        },
                        PackType.CLIENT_RESOURCES,
                        new PackSelectionConfig(false, Pack.Position.TOP, true)
                );

                e.addRepositorySource(consumer -> {
                    if (SOLID_BEDS_PACK != null) consumer.accept(SOLID_BEDS_PACK);
                    else Chloride.LOGGER.warn("SOLID_BEDS_PACK is null");

                    if (SOLID_CHESTS_PACK != null) consumer.accept(SOLID_CHESTS_PACK);
                    else Chloride.LOGGER.warn("SOLID_CHESTS_PACK is null");
                });
            }
        }

    }

    private static PathPackResources getPathResources(PackLocationInfo info, Path path) {
        return new PathPackResources(info, path) {

            @Override public boolean isHidden() {
                return false;
            }
        };
    }
}
