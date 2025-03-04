package me.srrapero720.chloride.foundation.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.ChlorideConfig.FullScreenMode;
import me.srrapero720.chloride.Tools;
import me.srrapero720.chloride.foundation.embeddium.pages.*;
import me.srrapero720.chloride.foundation.embeddium.storage.ChlorideOptionsStorage;
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
    public static void onSodiumPagesRegister(OptionGUIConstructionEvent e) {
        var pages = e.getPages();

        if (!ChlorideConfig.modpackMode) {
            pages.add(new OverlayPage());
            pages.add(new DetailsPage());
            pages.add(new TrueDarknessPage());
            pages.add(new EntityCullingPage());
            pages.add(new ZoomPage());
            pages.add(new OthersPage());
        } else {
            LOGGER.info("Modpack Mode is enabled, skipping chloride page registration");
        }
    }

    @SubscribeEvent
    public static void onSodiumPagesRegister(OptionGroupConstructionEvent e) {
        if (e.getId() != null && e.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
            var options = e.getOptions();
            for (int i = 0; i < options.size(); i++) {
                var id = options.get(i).getId();
                if (id != null && id.matches(StandardOptions.Option.FULLSCREEN)) {
                    options.set(i, getFullscreenOption());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSodiumGroupRegister(OptionPageConstructionEvent e) {
        if (e.getId() != null && e.getId().equals(StandardOptions.Pages.PERFORMANCE)) {
            var builder = OptionGroup.createBuilder();
            var sodiumOpts = SodiumGameOptionPages.getVanillaOpts();
            var fontShadow = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "font_shadow"))
                    .setName(Component.translatable("chloride.options.fontshadow.title"))
                    .setTooltip(Component.translatable("chloride.options.fontshadow.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.fontShadows = value,
                            (opts) -> ChlorideConfig.fontShadows)
                    .setImpact(OptionImpact.VARIES)
                    .build();

            var leavesCulling = OptionImpl.createBuilder(ChlorideConfig.LeavesCullingMode.class, sodiumOpts)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "leaves_culling"))
                    .setName(Component.translatable("chloride.options.leaves_culling.title"))
                    .setTooltip(Component.translatable("chloride.options.leaves_culling.desc"))
                    .setControl(opt -> new CyclingControl<>(opt, ChlorideConfig.LeavesCullingMode.class, new Component[] {
                            Component.translatable("chloride.options.leaves_culling.all"),
                            Component.translatable("chloride.options.leaves_culling.off")
                    }))
                    .setBinding((opt, v) -> ChlorideConfig.leavesCulling = v,
                            (opts) -> ChlorideConfig.leavesCulling)
                    .setImpact(OptionImpact.HIGH)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var fastChest = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "fast_chests"))
                    .setName(Component.translatable("chloride.options.fastchest.title"))
                    .setTooltip(Component.translatable("chloride.options.fastchest.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.fastChests = value,
                            (opts) -> ChlorideConfig.fastChests)
                    .setImpact(OptionImpact.HIGH)
//                .setEnabled(FastModels.canUseOnChests())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var fastBeds = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "fast_beds"))
                    .setName(Component.translatable("chloride.options.fastbeds.title"))
                    .setTooltip(Component.translatable("chloride.options.fastbeds.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.fastBeds = value,
                            (opts) -> ChlorideConfig.fastBeds)
                    .setImpact(OptionImpact.LOW)
//                .setEnabled(EmbyTools.isFlywheelOff())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var hideJEI = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(ResourceLocation.tryBuild(Chloride.ID, "hide_jremi"))
                    .setName(Component.translatable("chloride.options.jei.title"))
                    .setTooltip(Component.translatable("chloride.options.jei.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (opts, value) -> ChlorideConfig.hideJREMI = value,
                            (opts) -> ChlorideConfig.hideJREMI)
                    .setImpact(OptionImpact.LOW)
                    .setEnabled(Tools.isModInstalled("jei") || Tools.isModInstalled("roughlyenoughitems") || Tools.isModInstalled("emi"))
                    .build();

            builder.add(leavesCulling);
            builder.add(fontShadow);
            builder.add(fastChest);
            builder.add(fastBeds);
            builder.add(hideJEI);

            e.addGroup(builder.build());
        }
    }

    private static Option<FullScreenMode> getFullscreenOption() {
        var options = SodiumGameOptionPages.getVanillaOpts();
        return OptionImpl.createBuilder(FullScreenMode.class, options)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "fullscreen"))
                .setName(Component.translatable("chloride.options.screen.title"))
                .setTooltip(Component.translatable("chloride.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("chloride.options.screen.windowed"),
                        Component.translatable("chloride.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(ChlorideConfig::setFullScreenMode, (opts) -> ChlorideConfig.fullScreen).build();
    }
}
