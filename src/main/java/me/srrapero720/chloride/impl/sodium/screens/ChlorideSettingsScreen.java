package me.srrapero720.chloride.impl.sodium.screens;

import me.srrapero720.chloride.impl.SettingsScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ChlorideSettingsScreen extends StyledSettingsScreen {
    public ChlorideSettingsScreen(final Screen prev) {
        super(Component.translatable("chloride.settings.chloride"), SettingsScreens.Style.CHLORIDE, prev);
    }
}
