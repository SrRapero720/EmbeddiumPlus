package me.srrapero720.chloride.impl.sodium;

import me.srrapero720.chloride.ChlorideConfig;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.ControlValueFormatter;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class SodiumFeatures {
    /**
     * Shared storage handler. Sodium collects pending handlers in a Set before flushing them, so a single shared
     * instance guarantees {@link ChlorideConfig#write()} runs exactly once per "Apply", regardless of how many of
     * our options changed.
     */
    public static final StorageEventHandler STORAGE = ChlorideConfig::write;

    /**
     * Replacement for the old {@code BetterCyclingControl.tEnumComponent}: maps each enum constant to the
     * translation {@code translation + "." + name().toLowerCase()}. Order independent (keyed by name).
     */
    public static <E extends Enum<E>> Function<E, Component> enumNames(final String translation) {
        return e -> Component.translatable(translation + "." + e.name().toLowerCase());
    }

    public static final ControlValueFormatter VOID_HORIZON = v -> v == 63.0f ? Component.translatable("chloride.world.void_horizon.vanilla") : Component.literal(String.valueOf(v));
    public static final ControlValueFormatter NUMBER = v -> Component.literal(String.valueOf(v));
    public static final ControlValueFormatter PERCENT = v -> Component.literal(v + "%");
    public static final ControlValueFormatter BLOCKS = v -> Component.literal(v + " blocks");

    public static ControlValueFormatter suffix(final String suffix) {
        return v -> Component.literal(v + suffix);
    }
}
