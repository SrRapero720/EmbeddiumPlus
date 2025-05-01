package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.shaders.FogShape;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.Tools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class WorldPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "skies")));
    public WorldPage() {
        super(ID, Component.translatable("chloride.world"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var band = OptionGroup.createBuilder();
        band.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.blueband.title"))
                .setTooltip(Component.translatable("chloride.world.blueband.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, v) -> ChlorideConfig.blueBand = v, opt -> ChlorideConfig.blueBand)
                .build()
        );


        final var customFog = OptionGroup.createBuilder();
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.fog.title"))
                .setTooltip(Component.translatable("chloride.world.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog = value, opt -> ChlorideConfig.fog)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.customFog = value,
                        opt -> ChlorideConfig.customFog)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.start.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.start.desc"))
                .setControl(option -> new SliderControl(option, -1000, 1000, 10, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fogStart = current,
                        options -> ChlorideConfig.fogStart)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.end.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.end.desc"))
                .setControl(option -> new SliderControl(option, 100, 10000, 50, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fogEnd = current, options -> ChlorideConfig.fogEnd)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(FogShape.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.shape.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.shape.desc"))
                .setControl(option -> new CyclingControl<>(option, FogShape.class, Tools.tEnumComponent("chloride.world.custom_fog.shape", FogShape.class)))
                .setBinding((opts, value) -> ChlorideConfig.fogShape = value, opts -> ChlorideConfig.fogShape)
                .build()
        );

        final var worldVisuals = OptionGroup.createBuilder();
        worldVisuals.add(OptionImpl.createBuilder(ChlorideConfig.LeavesCullingMode.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "leaves_culling"))
                .setName(Component.translatable("chloride.world.leaves_culling.title"))
                .setTooltip(Component.translatable("chloride.world.leaves_culling.desc"))
                .setControl(opt -> new CyclingControl<>(opt, ChlorideConfig.LeavesCullingMode.class, Tools.tEnumComponent("chloride.world.leaves_culling", ChlorideConfig.LeavesCullingMode.class)))
                .setBinding((opt, v) -> ChlorideConfig.leavesCulling = v, opts -> ChlorideConfig.leavesCulling)
                .setImpact(OptionImpact.HIGH)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build()
        );
        worldVisuals.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.clouds.height.title"))
                .setTooltip(Component.translatable("chloride.world.clouds.height.desc"))
                .setControl(opt -> new SliderControl(opt, 64, 364, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.cloudsHeight = value,
                        opt -> ChlorideConfig.cloudsHeight)
                .build()
        );

        final var worldAmazings = OptionGroup.createBuilder();
        worldAmazings.add(OptionImpl.createBuilder(ChlorideConfig.ChunkFadeSpeed.class, STORAGE)
                .setName(Component.translatable("chloride.world.fade.title"))
                .setTooltip(Component.translatable("chloride.world.fade.desc"))
                .setControl(option -> new CyclingControl<>(option, ChlorideConfig.ChunkFadeSpeed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"), // a literal fade
                        Component.translatable("options.graphics.fancy") // chunks comes from the ground
                }))
                .setBinding((opts, value) -> ChlorideConfig.chunkFadeSpeed = value,
                        opts -> ChlorideConfig.chunkFadeSpeed)
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build()
        );


        groups.add(band.build());
        groups.add(customFog.build());
        groups.add(worldVisuals.build());
        groups.add(worldAmazings.build());

        return ImmutableList.copyOf(groups);
    }
}
