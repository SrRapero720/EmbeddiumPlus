package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.shaders.FogShape;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.ChunkFade;
import me.srrapero720.chloride.impl.LeavesCulling;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

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
                .setBinding((opt, v) -> ChlorideConfig.fog.blueBand = v, opt -> ChlorideConfig.fog.blueBand)
                .build()
        );


        final var customFog = OptionGroup.createBuilder();
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.fog.title"))
                .setTooltip(Component.translatable("chloride.world.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog.enabled = value, opt -> ChlorideConfig.fog.enabled)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.fog.overworld.title"))
                .setTooltip(Component.translatable("chloride.world.fog.overworld.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog.onOverworld = value, opt -> ChlorideConfig.fog.onOverworld)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.fog.nether.title"))
                .setTooltip(Component.translatable("chloride.world.fog.nether.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog.onNether = value, opt -> ChlorideConfig.fog.onNether)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.fog.end.title"))
                .setTooltip(Component.translatable("chloride.world.fog.end.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog.onEnd = value, opt -> ChlorideConfig.fog.onEnd)
                .setImpact(OptionImpact.LOW)
                .build()
        );
        customFog.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.fog.custom = value,
                        opt -> ChlorideConfig.fog.custom)
                .setImpact(OptionImpact.LOW)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.start.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.start.desc"))
                .setControl(option -> new SliderControl(option, -1000, 1000, 10, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fog.start = current,
                        options -> ChlorideConfig.fog.start)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.end.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.end.desc"))
                .setControl(option -> new SliderControl(option, 100, 10000, 50, ControlValueFormatter.number()))
                .setBinding((options, current) -> ChlorideConfig.fog.end = current, options -> ChlorideConfig.fog.end)
                .build()
        );

        customFog.add(OptionImpl.createBuilder(FogShape.class, STORAGE)
                .setName(Component.translatable("chloride.world.custom_fog.shape.title"))
                .setTooltip(Component.translatable("chloride.world.custom_fog.shape.desc"))
                .setControl(option -> new CyclingControl<>(option, FogShape.class, enumNames("chloride.world.custom_fog.shape", FogShape.class)))
                .setBinding((opts, value) -> ChlorideConfig.fog.shape = value, opts -> ChlorideConfig.fog.shape)
                .build()
        );

        final var worldVisuals = OptionGroup.createBuilder();
        worldVisuals.add(OptionImpl.createBuilder(LeavesCulling.LeavesCullingMode.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "leaves_culling"))
                .setName(Component.translatable("chloride.world.leaves_culling.title"))
                .setTooltip(Component.translatable("chloride.world.leaves_culling.desc"))
                .setControl(opt -> new CyclingControl<>(opt, LeavesCulling.LeavesCullingMode.class, enumNames("chloride.world.leaves_culling", LeavesCulling.LeavesCullingMode.class)))
                .setBinding((opt, v) -> ChlorideConfig.world.leavesCulling = v, opts -> ChlorideConfig.world.leavesCulling)
                .setImpact(OptionImpact.HIGH)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build()
        );
        worldVisuals.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.world.clouds.height.title"))
                .setTooltip(Component.translatable("chloride.world.clouds.height.desc"))
                .setControl(opt -> new SliderControl(opt, 64, 364, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.world.cloudsHeight = value,
                        opt -> ChlorideConfig.world.cloudsHeight)
                .build()
        );

        // LOWER VOID HORIZON: Y LEVEL WHERE THE DARK VOID PLANE STARTS; VOID_HORIZON SHOWS "VANILLA" AT 63
        worldVisuals.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "lower_void_horizon"))
                .setName(Component.translatable("chloride.world.void_horizon.title"))
                .setTooltip(Component.translatable("chloride.world.void_horizon.desc"))
                .setControl(opt -> new SliderControl(opt, -64, 256, 1, VOID_HORIZON))
                .setBinding((opt, value) -> ChlorideConfig.world.lowerVoidHorizon = value,
                        opt -> ChlorideConfig.world.lowerVoidHorizon)
                .build()
        );

        // ENFORCES THE 32-CHUNK SKY FAR-PLANE SO THE SKYBOX DOES NOT CLIP AT LOW RENDER DISTANCES
        worldVisuals.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "far_skybox"))
                .setName(Component.translatable("chloride.world.far_skybox.title"))
                .setTooltip(Component.translatable("chloride.world.far_skybox.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.world.farSkybox = value,
                        opt -> ChlorideConfig.world.farSkybox)
                .build()
        );

        final var worldAmazings = OptionGroup.createBuilder();
        worldAmazings.add(OptionImpl.createBuilder(ChunkFade.Speed.class, STORAGE)
                .setName(Component.translatable("chloride.world.fade.title"))
                .setTooltip(Component.translatable("chloride.world.fade.desc"))
                .setControl(option -> new CyclingControl<>(option, ChunkFade.Speed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"), // a literal fade
                        Component.translatable("options.graphics.fancy") // chunks comes from the ground
                }))
                .setBinding((opts, value) -> ChlorideConfig.world.chunkFadeSpeed = value,
                        opts -> ChlorideConfig.world.chunkFadeSpeed)
                .setImpact(OptionImpact.LOW)
                .setEnabled(false)
                .build()
        );


        groups.add(band.build());
        if (!ChlorideConfig.modpackMode) {
            groups.add(customFog.build());
        }
        groups.add(worldVisuals.build());
        groups.add(worldAmazings.build());

        return ImmutableList.copyOf(groups);
    }
}
