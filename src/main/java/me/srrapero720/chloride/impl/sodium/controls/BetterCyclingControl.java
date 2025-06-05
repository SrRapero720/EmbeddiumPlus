package me.srrapero720.chloride.impl.sodium.controls;

import net.caffeinemc.mods.sodium.client.gui.options.Option;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.minecraft.network.chat.Component;

public class BetterCyclingControl<T extends Enum<T>> extends CyclingControl<T> {
    public BetterCyclingControl(Option<T> option, Class<T> enumType) {
        super(option, enumType);
    }

    public BetterCyclingControl(Option<T> option, Class<T> enumType, String translation) {
        super(option, enumType, tEnumComponent(translation, enumType));
    }

    public BetterCyclingControl(Option<T> option, Class<T> enumType, T[] allowedValues) {
        super(option, enumType, allowedValues);
    }

    public static <T extends Enum<T>> Component[] tEnumComponent(final String translation, final Class<T> clazz) {
        final T[] constants = clazz.getEnumConstants();
        final Component[] result = new Component[constants.length];
        for (int i = 0; i < constants.length; i++) {
            result[i] = Component.translatable(translation + "." + constants[i].name().toLowerCase());
        }
        return result;
    }
}
