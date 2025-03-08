package me.srrapero720.chloride.features.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.features.sodium.ChlorideOptions.STORAGE;

public class EntityCullingPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "culling")));
    public EntityCullingPage() {
        super(ID, Component.translatable("chloride.options.culling.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var enableDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.entity.title"))
                .setTooltip(Component.translatable("chloride.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.entityDistanceCulling = value, opt -> ChlorideConfig.entityDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.entity.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.options.culling.entity.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.entityCullingDistanceX = value * value, 
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.entityCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.entity.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.options.culling.entity.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.entityCullingDistanceY = value,
                        (opt) -> ChlorideConfig.entityCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );

        var monsterDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.monster.title"))
                .setTooltip(Component.translatable("chloride.options.culling.monster.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterDistanceCulling = value,
                        (opt) -> ChlorideConfig.monsterDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxMonsterDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.monster.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.options.culling.monster.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterCullingDistanceX = value * value,
                        (opt) -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.monsterCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxMonsterDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.monster.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.options.culling.monster.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterCullingDistanceY = value,
                        (opt) -> ChlorideConfig.monsterCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup.createBuilder()
                .add(monsterDistanceChecks)
                .add(maxMonsterDistance)
                .add(maxMonsterDistanceVertical)
                .build()
        );


        var enableTileDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.tiles.title"))
                .setTooltip(Component.translatable("chloride.options.culling.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.tileEntityDistanceCulling = value,
                        (opt) -> ChlorideConfig.tileEntityDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build();


        var maxTileEntityDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.tile.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.options.culling.tile.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.tileEntityCullingDistanceX = value * value,
                        (opt) -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.tileEntityCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxTileEntityDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.options.culling.tile.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.options.culling.tile.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.tileEntityCullingDistanceY = value,
                        (opt) -> ChlorideConfig.tileEntityCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(enableTileDistanceChecks)
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
