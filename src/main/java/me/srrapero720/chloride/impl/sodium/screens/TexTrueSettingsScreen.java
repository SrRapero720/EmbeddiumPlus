package me.srrapero720.chloride.impl.sodium.screens;

import me.srrapero720.chloride.impl.SettingsScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TexTrueSettingsScreen extends StyledSettingsScreen {
    public TexTrueSettingsScreen(final Screen prev) {
        super(Component.translatable("chloride.settings.textrue"), SettingsScreens.Style.TEXTRUE, prev);
    }
}
