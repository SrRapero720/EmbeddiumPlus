package me.srrapero720.chloride.impl.sodium.screens;

import me.srrapero720.chloride.impl.SettingsScreens;
import net.caffeinemc.mods.sodium.client.gui.Colors;
import net.caffeinemc.mods.sodium.client.gui.Layout;
import net.caffeinemc.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class StyledSettingsScreen extends Screen {
    protected final SettingsScreens.Style style;
    protected final Screen prev;

    protected StyledSettingsScreen(final Component title, final SettingsScreens.Style style, final Screen prev) {
        super(title);
        this.style = style;
        this.prev = prev;
    }

    @Override
    protected void init() {
        // SAME SQUARE SWITCHER AS THE SODIUM SCREEN, SO THE DEV CYCLE CAN CONTINUE FROM HERE
        this.addRenderableWidget(new StyleSwitchButton(new Dim2i(0, 0, Layout.BUTTON_SHORT, Layout.BUTTON_SHORT), this.style, this.prev));
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2, Colors.FOREGROUND);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.prev);
    }
}
