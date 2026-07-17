package me.srrapero720.chloride.impl.sodium;

import com.mojang.blaze3d.platform.Monitor;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.Borderless;
import me.srrapero720.chloride.impl.sodium.pages.*;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ModOptionsBuilder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static me.srrapero720.chloride.Chloride.LOGGER;
import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;
import static me.srrapero720.chloride.Chloride.id;

/**
 * Registers Chloride's option pages through Sodium's public config API (replaces the old {@code SodiumOptionsGUIMixin}
 * and {@code SodiumOptionsMixin}, which targeted classes that no longer exist as of Sodium 0.8.x).
 *
 * <p>Discovered by Sodium via the {@link ConfigEntryPointForge} annotation. {@code registerConfigLate} runs at
 * Minecraft post-init, which is after {@code Chloride.earlyLoad()} has populated {@link ChlorideConfig}, so the
 * option bindings can safely read the current config values.
 *
 * <p>Sodium's own pages are integrated here: the vanilla fullscreen toggle is swapped for Chloride's three-way
 * borderless mode ({@code registerOptionReplacement}), and the fullscreen-resolution option is re-pointed at that
 * new option via {@code registerOptionOverlay} (otherwise it would depend on the now-removed
 * {@code sodium:general.fullscreen} and Sodium's dependency validation crashes). The additive toggles (borderless
 * optimization, fast chests/beds) are injected with {@code SodiumConfigBuilderMixin}, since the public API can only
 * replace/overlay existing options, not add brand-new ones to another mod's page.
 */
@ConfigEntryPointForge("chloride")
public class ChlorideConfigIntegration implements ConfigEntryPoint {

    private static final ResourceLocation SODIUM_FULLSCREEN = ResourceLocation.parse("sodium:general.fullscreen");
    private static final ResourceLocation SODIUM_FULLSCREEN_RESOLUTION = ResourceLocation.parse("sodium:general.fullscreen_resolution");
    /** Id of Chloride's replacement option; both the replacement and the resolution overlay's dependency point here. */
    private static final ResourceLocation FULL_SCREEN = id("fullScreen");
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Chloride.ID, "textures/gui/logo.png");
    /** Neon green accent, in the spirit of Sodium's mint theme. Sodium derives the lighter/darker shades from it. */
    private static final int NEON_GREEN = 0x39FF14;

    @Override
    public void registerConfigLate(final ConfigBuilder builder) {
        // setIcon (not setNonTintedIcon) tints the white logo to the group's theme colour set below.
        final ModOptionsBuilder mod = builder.registerOwnModOptions()
                .setIcon(ICON)
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(NEON_GREEN));

        // Replace Sodium's vanilla "fullscreen" toggle with Chloride's three-way borderless mode selector.
        mod.registerOptionReplacement(SODIUM_FULLSCREEN, builder.createEnumOption(FULL_SCREEN, Borderless.Mode.class)
                .setName(Component.translatable("options.fullscreen"))
                .setTooltip(Component.translatable("chloride.general.screen.desc"))
                .setElementNameProvider(EnumOptionBuilder.nameProviderFrom(
                        Component.translatable("chloride.general.screen.windowed"),
                        Component.translatable("chloride.general.screen.borderless"),
                        Component.translatable("chloride.general.screen.fullscreen")))
                .setStorageHandler(STORAGE)
                .setDefaultValue(Borderless.Mode.WINDOWED)
                .setBinding(Borderless::setFullScreenMode, () -> ChlorideConfig.fullscreen.mode));

        // The fullscreen-resolution option originally depended on sodium:general.fullscreen (a boolean). Re-point it
        // at our enum so it (a) keeps a valid dependency and (b) is only usable in true FULLSCREEN mode (borderless
        // uses the desktop resolution). Everything else (binding, validator, formatter, flags) is inherited from the
        // base option via the overlay. The monitor/OS guards mirror Sodium's original enabled provider.
        mod.registerOptionOverlay(SODIUM_FULLSCREEN_RESOLUTION, builder.createIntegerOption(SODIUM_FULLSCREEN_RESOLUTION)
                .setEnabledProvider(state -> {
                    final Monitor monitor = Minecraft.getInstance().getWindow().findBestMonitor();
                    if (monitor == null || monitor.getModeCount() <= 0) return false;
                    final Util.OS os = Util.getPlatform();
                    if (os != Util.OS.WINDOWS && os != Util.OS.OSX) return false;
                    return state.readEnumOption(FULL_SCREEN, Borderless.Mode.class) == Borderless.Mode.FULLSCREEN;
                }, FULL_SCREEN));

        mod.addPage(InterfacePage.build(builder));
        mod.addPage(WorldPage.build(builder));
        if (!ChlorideConfig.modpackMode) mod.addPage(DarknessPage.build(builder));
        mod.addPage(ParticlesPage.build(builder));
        // EXTERNAL PAGE: THE PER-PARTICLE TOGGLES OPEN IN THEIR OWN SCROLL-CULLED SCREEN (ISSUE #174)
        mod.addPage(builder.createExternalPage()
                .setName(Component.translatable("chloride.particles.list"))
                .setScreenConsumer(current -> Minecraft.getInstance().setScreen(new ParticleListScreen(current))));
        mod.addPage(EntitiesPage.build(builder));
        if (!ChlorideConfig.modpackMode) mod.addPage(ZoomPage.build(builder));

        if (ChlorideConfig.modpackMode) {
            LOGGER.info("Modpack Mode is enabled, skipping chloride True Darkness and Zoom page registration");
        }
    }
}
