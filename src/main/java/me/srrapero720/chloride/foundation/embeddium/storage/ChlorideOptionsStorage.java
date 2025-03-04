package me.srrapero720.chloride.foundation.embeddium.storage;

import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.chloride.ChlorideConfig;

public class ChlorideOptionsStorage implements OptionStorage<Object> {
    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        ChlorideConfig.write();
    }
}
