package me.srrapero720.chloride.impl.sodium;

import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

public class SodiumFeatures {
    public static final OptionStorage<?> STORAGE = new OptionStorage<>() {
        @Override public Object getData() { return new Object(); }
        @Override public void save() { ChlorideConfig.write(); }
    };
}
