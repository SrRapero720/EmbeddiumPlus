package me.srrapero720.chloride.features.sodium;

import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.ChlorideConfig.FullScreenMode;
import me.srrapero720.chloride.features.sodium.pages.*;
import me.srrapero720.chloride.features.sodium.storage.ChlorideOptionsStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

import static me.srrapero720.chloride.Chloride.LOGGER;

@Mod.EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChlorideOptions {
    public static final OptionStorage<?> STORAGE = new ChlorideOptionsStorage();

    @SubscribeEvent
    public static void onSodiumPagesRegister(final OptionGUIConstructionEvent e) {
        final var pages = e.getPages();

        pages.add(new InterfacePage());
        pages.add(new WorldPage());
        if (!ChlorideConfig.modpackMode) pages.add(new DarknessPage());
        pages.add(new EntitiesPage());
        if (!ChlorideConfig.modpackMode) pages.add(new ZoomPage());
        if (ChlorideConfig.modpackMode) {
            LOGGER.info("Modpack Mode is enabled, skipping chloride True Darkness and Zoom page registration");
        }
    }

    @SubscribeEvent
    public static void onSodiumPagesRegister(final OptionGroupConstructionEvent e) {
        if (e.getId() != null && e.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
            final var options = e.getOptions();
            for (int i = 0; i < options.size(); i++) {
                final var id = options.get(i).getId();
                if (id != null && id.matches(StandardOptions.Option.FULLSCREEN)) {
                    options.set(i, getFullscreenOption());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSodiumGroupRegister(final OptionPageConstructionEvent e) {
        if (e.getId() != null && e.getId().equals(StandardOptions.Pages.PERFORMANCE)) {
            final var builder = OptionGroup.createBuilder();

            final var fastChest = OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "fast_chests"))
                    .setName(Component.translatable("chloride.performance.fastchest.title"))
                    .setTooltip(Component.translatable("chloride.performance.fastchest.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.fastChests = value,
                            (opts) -> ChlorideConfig.fastChests)
                    .setImpact(OptionImpact.HIGH)
//                .setEnabled(FastModels.canUseOnChests())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            final var fastBeds = OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "fast_beds"))
                    .setName(Component.translatable("chloride.performance.fastbeds.title"))
                    .setTooltip(Component.translatable("chloride.performance.fastbeds.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.fastBeds = value,
                            (opts) -> ChlorideConfig.fastBeds)
                    .setImpact(OptionImpact.LOW)
//                .setEnabled(EmbyTools.isFlywheelOff())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            builder.add(fastChest);
            builder.add(fastBeds);

            e.addGroup(builder.build());
        }
    }

    private static Option<FullScreenMode> getFullscreenOption() {
        return OptionImpl.createBuilder(FullScreenMode.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "fullscreen"))
                .setName(Component.translatable("options.fullscreen"))
                .setTooltip(Component.translatable("chloride.general.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("chloride.general.screen.windowed"),
                        Component.translatable("chloride.general.screen.borderless"),
                        Component.translatable("chloride.general.screen.fullscreen")
                }))
                .setBinding(
                        (s, g) -> ChlorideConfig.setFullScreenMode(g),
                        (opts) -> ChlorideConfig.fullScreen
                ).build();
    }
}
