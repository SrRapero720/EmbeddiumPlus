package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FastBlocks {
    private static final String BEDS = Chloride.ID + ":solid_beds";
    private static final String CHESTS = Chloride.ID + ":solid_chests";

    public static boolean canUseOnChests() {
        return !Chloride.installed("enhancedblockentities");
    }

    public static void registerPacks() {
        Chloride.LOGGER.info("Registering CHLORIDE built-in packs");
        final var mod = FabricLoader.getInstance().getModContainer(Chloride.ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(Identifier.fromNamespaceAndPath(Chloride.ID, "solid_beds"),
                mod, Component.literal("Chloride: Solid Beds"), ResourcePackActivationType.NORMAL);
        ResourceManagerHelper.registerBuiltinResourcePack(Identifier.fromNamespaceAndPath(Chloride.ID, "solid_chests"),
                mod, Component.literal("Chloride: Solid Chests"), ResourcePackActivationType.NORMAL);
    }

    public static void applyChests() {
        toggle(CHESTS, canUseOnChests() && ChlorideConfig.fastBlocks.chests);
    }

    public static void applyBeds() {
        toggle(BEDS, ChlorideConfig.fastBlocks.beds);
    }

    private static void toggle(final String id, final boolean enabled) {
        final var repo = Minecraft.getInstance().getResourcePackRepository();
        if (enabled) repo.addPack(id);
        else repo.removePack(id);
    }
}
