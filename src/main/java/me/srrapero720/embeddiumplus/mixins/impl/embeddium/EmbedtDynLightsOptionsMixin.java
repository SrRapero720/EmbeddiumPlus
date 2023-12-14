package me.srrapero720.embeddiumplus.mixins.impl.embeddium;

import com.google.common.collect.ImmutableList;
import me.srrapero720.dynamiclights.LambDynLights;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig.DynamicLightsQuality;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public abstract class EmbedtDynLightsOptionsMixin {
    @Shadow @Final private List<OptionPage> pages;

    @Unique
    private static final SodiumOptionsStorage embeddiumPlus$dynLightsOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, DynamicLightsQuality> qualityMode = OptionImpl.createBuilder(DynamicLightsQuality.class, embeddiumPlus$dynLightsOpts)
                .setName(Component.translatable("embeddium.plus.options.dynlights.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.speed.desc"))
                .setControl((option) -> new CyclingControl<>(option, DynamicLightsQuality.class, new Component[] {
                                Component.translatable("embeddium.plus.options.common.off"),
                                Component.translatable("embeddium.plus.options.common.slow"),
                                Component.translatable("embeddium.plus.options.common.fast"),
                                Component.translatable("embeddium.plus.options.common.faster"),
                                Component.translatable("embeddium.plus.options.common.realtime")
                        }))
                .setBinding((options, value) -> {
                            EmbeddiumPlusConfig.dynQuality.set(value);
                            LambDynLights.get().clearLightSources();
                        },
                        (options) -> EmbeddiumPlusConfig.dynQuality.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, embeddiumPlus$dynLightsOpts)
                .setName(Component.translatable("embeddium.plus.options.dynlights.entities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.entities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.entityLighting.set(value),
                        (options) -> EmbeddiumPlusConfig.entityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, embeddiumPlus$dynLightsOpts)
                .setName(Component.translatable("embeddium.plus.options.dynlights.blockentities.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.dynlights.blockentities.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> EmbeddiumPlusConfig.tileEntityLighting.set(value),
                        (options) -> EmbeddiumPlusConfig.tileEntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
                .build()
        );

        pages.add(new OptionPage(Component.translatable("embeddium.plus.options.dynlights.group"), ImmutableList.copyOf(groups)));
    }
}