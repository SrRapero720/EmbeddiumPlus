package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.sodium.screens.ChlorideSettingsScreen;
import me.srrapero720.chloride.impl.sodium.screens.PopuliSettingsScreen;
import me.srrapero720.chloride.impl.sodium.screens.TexTrueSettingsScreen;
import net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Alternative settings screen styles for Sodium's video settings. The non-sodium styles are
 * work-in-progress canvases, so switching between them is locked behind dev environments.
 */
public class SettingsScreens {
    /** True on dev environments; the custom screen styles only activate behind this flag. */
    public static final boolean DEV = !FMLEnvironment.isProduction();

    /**
     * Switches from the current settings screen style to the next one, persisting the choice.
     * No-op in production while the custom screens are under construction.
     */
    public static void next(final Style current, final Screen prev) {
        if (!DEV) return;
        final Style[] styles = Style.values();
        final Style next = styles[(current.ordinal() + 1) % styles.length];
        ChlorideConfig.ui.settingsScreen = next;
        ChlorideConfig.write();
        Minecraft.getInstance().gui.setScreen(next.screen(prev));
    }

    public enum Style {
        SODIUM, CHLORIDE, TEXTRUE, POPULI;

        /** Creates this style's settings screen; {@code prev} is restored when it closes. */
        public Screen screen(final Screen prev) {
            return switch (this) {
                case SODIUM -> VideoSettingsScreen.createScreen(prev);
                case CHLORIDE -> new ChlorideSettingsScreen(prev);
                case TEXTRUE -> new TexTrueSettingsScreen(prev);
                case POPULI -> new PopuliSettingsScreen(prev);
            };
        }
    }
}
