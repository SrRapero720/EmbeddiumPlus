package me.srrapero720.chloride.impl.sodium.screens;

import me.srrapero720.chloride.impl.SettingsScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PopuliSettingsScreen extends StyledSettingsScreen {
    public PopuliSettingsScreen(final Screen prev) {
        super(Component.translatable("chloride.settings.populi"), SettingsScreens.Style.POPULI, prev);
    }
}
