package me.srrapero720.embeddiumplus.foundation.embeddium.storage;

import dev.nolij.zume.common.Zume;
import dev.nolij.zume.common.config.ZumeConfig;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

public class ZumeOptionsStorage implements OptionStorage<ZumeConfig> {
    private final ZumeConfig cloneConfig = Zume.config.clone();

    @Override
    public ZumeConfig getData() {
        return cloneConfig;
    }

    @Override
    public void save() {
        try {
            ZumeConfig.replace(cloneConfig);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot store ZUME config");
        }
    }
}