package me.srrapero720.embeddiumplus.foundation.embeddium.storage;

import dev.nolij.zume.common.Zume;
import dev.nolij.zume.common.config.ZumeConfig;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.mixins.impl.zume.accessors.ZumeAccessor;
import me.srrapero720.embeddiumplus.mixins.impl.zume.accessors.ZumeConfigAccessor;

public class ZumeOptionsStorage implements OptionStorage<ZumeConfig> {
    @Override
    public ZumeConfig getData() {
        return Zume.config;
    }

    @Override
    public void save() {
        // WE CAN'T SAVE
    }
}