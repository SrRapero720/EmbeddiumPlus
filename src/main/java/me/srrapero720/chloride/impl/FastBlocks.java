package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.api.events.FastModelSettingsUpdate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.srrapero720.chloride.Chloride.SOLID_BEDS_PACK;
import static me.srrapero720.chloride.Chloride.SOLID_CHESTS_PACK;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Chloride.ID)
public class FastBlocks {
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
}
