package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.Options;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import me.srrapero720.embeddiumplus.mixins.impl.borderless.MainWindowAccessor;

import java.util.*;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class EmbedGameOptionsMixin {
    @Shadow(remap = false)
    @Final
    private static SodiumOptionsStorage sodiumOpts;
    @Shadow(remap = false)
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Redirect(method = "advanced",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"
            ))
    private static ImmutableList<OptionGroup> injectAdvanced(Collection<OptionGroup> groups) {
        Option<EmbeddiumPlusConfig.Complexity> displayFps = OptionImpl.createBuilder(EmbeddiumPlusConfig.Complexity.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbeddiumPlusConfig.Complexity.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.simple"),
                        Component.translatable("embeddium.plus.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> EmbeddiumPlusConfig.fpsCounterMode.set(value.toString()),
                        (opts) -> EmbeddiumPlusConfig.Complexity.valueOf(EmbeddiumPlusConfig.fpsCounterMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();


        Option<Integer> displayFpsPos = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.displayfps.position.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.position.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 2, ControlValueFormatter.translateVariable("Pixels")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> EmbeddiumPlusConfig.fpsCounterPosition.set(value),
                        (opts) -> EmbeddiumPlusConfig.fpsCounterPosition.get())
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(displayFps)
                .add(displayFpsPos)
                .build());


        OptionImpl<SodiumGameOptions, Boolean> totalDarkness = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.darkness.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.trueDarknessEnabled.set(value),
                        (options) -> EmbeddiumPlusConfig.trueDarknessEnabled.get())
                .setImpact(OptionImpact.LOW)
                .build();

        Option<EmbeddiumPlusConfig.DarknessOption> totalDarknessSetting = OptionImpl.createBuilder(EmbeddiumPlusConfig.DarknessOption.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.darkness.mode.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.darkness.mode.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbeddiumPlusConfig.DarknessOption.class, new Component[]{
                        Component.translatable("embeddium.plus.options.darkness.mode.pitchblack"),
                        Component.translatable("embeddium.plus.options.darkness.mode.reallydark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dark"),
                        Component.translatable("embeddium.plus.options.darkness.mode.dim")
                }))
                .setBinding(
                        (opts, value) -> EmbeddiumPlusConfig.darknessOption.set(value),
                        (opts) -> EmbeddiumPlusConfig.darknessOption.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(totalDarkness)
                .add(totalDarknessSetting)
                .build());


        Option<EmbeddiumPlusConfig.Quality> fadeInQuality = OptionImpl.createBuilder(EmbeddiumPlusConfig.Quality.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.chunkfadeinquality.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.chunkfadeinquality.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbeddiumPlusConfig.Quality.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding(
                        (opts, value) -> EmbeddiumPlusConfig.fadeInQuality.set(value.toString()),
                        (opts) -> EmbeddiumPlusConfig.Quality.valueOf(EmbeddiumPlusConfig.fadeInQuality.get()))
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> fog = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.fog.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.fog.set(value),
                        (options) -> EmbeddiumPlusConfig.fog.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> hideJEI = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.jei.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.jei.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.hideJEI.set(value),
                        (options) -> EmbeddiumPlusConfig.hideJEI.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Integer> cloudHeight = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.clouds.height.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.clouds.height.desc"))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> {
                            EmbeddiumPlusConfig.cloudHeight.set(value);
                        },
                        (options) -> EmbeddiumPlusConfig.cloudHeight.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup.createBuilder()
                .add(hideJEI)
                .add(fadeInQuality)
                .add(fog)
                .add(cloudHeight)
                .build());


        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.enableDistanceChecks.set(value),
                        (options) -> EmbeddiumPlusConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbeddiumPlusConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.maxEntityRenderDistanceY.set(value),
                        (options) -> EmbeddiumPlusConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );


        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.h.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.h.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.maxTileEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbeddiumPlusConfig.maxTileEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.v.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.v.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.translateVariable("Blocks")))
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.maxTileEntityRenderDistanceY.set(value),
                        (options) -> EmbeddiumPlusConfig.maxTileEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );
        return ImmutableList.copyOf(groups);
    }

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static ImmutableList<OptionGroup> redirectGeneralOptions(Collection<OptionGroup> groups) {
        OptionImpl<Options, EmbeddiumPlusConfig.FullscreenMode> fullscreenMode = OptionImpl.createBuilder( EmbeddiumPlusConfig.FullscreenMode.class, vanillaOpts)
                .setName(Component.translatable("embeddium.plus.options.fullscreen.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.fullscreen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, EmbeddiumPlusConfig.FullscreenMode.class, new Component[] {
                        Component.translatable("embeddium.plus.options.fullscreen.windowed"),
                        Component.translatable("embeddium.plus.options.fullscreen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(
                        (opts, value) -> {
                            EmbeddiumPlusConfig.fullScreenMode.set(value);
                            opts.fullscreen.set(value != EmbeddiumPlusConfig.FullscreenMode.WINDOWED);

                            Minecraft client = Minecraft.getInstance();
                            Window window = client.getWindow();
                            if (window.isFullscreen() != opts.fullscreen.get())
                            {
                                window.toggleFullScreen();
                                opts.fullscreen.set(window.isFullscreen());
                            }

                            if (opts.fullscreen.get())
                            {
                                ((MainWindowAccessor) (Object) window).setDirty(true);
                                window.changeFullscreenVideoMode();
                            }
                        },
                        (opts) -> EmbeddiumPlusConfig.fullScreenMode.get())
                .build();

        List<OptionGroup> newList = new ArrayList<>();

        for (OptionGroup optionGroup : groups) {
            OptionGroup.Builder builder = OptionGroup.createBuilder();

            for (Option<?> option : optionGroup.getOptions()) {
                builder.add(Objects.equals(option.getName().getString(), "Fullscreen") ? fullscreenMode : option);
            }

            newList.add(builder.build());
        }

        groups.clear();
        groups.addAll(newList);

        return ImmutableList.copyOf(groups);
    }

    @Redirect(method = "advanced", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 6))
    private static MutableComponent ChangeCategoryName(String old) {
        return Component.translatable("embeddium.plus.options.plus");
    }
}