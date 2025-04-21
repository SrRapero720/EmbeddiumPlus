package me.srrapero720.chloride.features.sodium;


import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.ChlorideConfig.FullScreenMode;
import me.srrapero720.chloride.features.sodium.storage.ChlorideOptionsStorage;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

public class ChlorideOptions {
    public static final ChlorideOptionsStorage STORAGE = new ChlorideOptionsStorage();

    public static Option<FullScreenMode> getFullscreenOption() {
        return OptionImpl.createBuilder(FullScreenMode.class, STORAGE)
                .setName(Component.translatable("chloride.options.screen.title"))
                .setTooltip(Component.translatable("chloride.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("chloride.options.screen.windowed"),
                        Component.translatable("chloride.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(
                        (s, g) -> ChlorideConfig.setFullScreenMode(g),
                        (opts) -> ChlorideConfig.fullScreen
                ).build();
    }
}
