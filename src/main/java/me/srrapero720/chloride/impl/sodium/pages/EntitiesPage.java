package me.srrapero720.chloride.impl.sodium.pages;

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
import me.srrapero720.chloride.impl.EntityCulling;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

public class EntitiesPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "culling")));
    public EntitiesPage() {
        super(ID, Component.translatable("chloride.entities"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        final var nametags = OptionGroup.createBuilder();
        if (!ChlorideConfig.modpackMode) {
            nametags.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setName(Component.translatable("chloride.entities.nametag.entities.title"))
                    .setTooltip(Component.translatable("chloride.entities.nametag.entities.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding((opt, v) -> ChlorideConfig.entityNametagRendering = v, opt -> ChlorideConfig.entityNametagRendering)
                    .build()
            );
            nametags.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setName(Component.translatable("chloride.entities.nametag.players.title"))
                    .setTooltip(Component.translatable("chloride.entities.nametag.players.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding((opt, v) -> ChlorideConfig.playerNametagRendering = v, opt -> ChlorideConfig.playerNametagRendering)
                    .build()
            );

            nametags.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setName(Component.translatable("chloride.entities.nametag.items.title"))
                    .setTooltip(Component.translatable("chloride.entities.nametag.items.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding((opt, v) -> ChlorideConfig.itemNametagRendering = v, opt -> ChlorideConfig.itemNametagRendering)
                    .build()
            );
        }

        final var entityGroup = OptionGroup.createBuilder();
        final var vsWarning = Component.translatable("chloride.entities.culling.vseureka.warning").withStyle(ChatFormatting.GOLD);
        entityGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.entityDistanceCulling = value, opt -> ChlorideConfig.entityDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        entityGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.entityCullingDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.entityCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        entityGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding(
                        (opt, value) -> ChlorideConfig.entityCullingDistanceY = value,
                        opt -> ChlorideConfig.entityCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        final var monsterGroup = OptionGroup.createBuilder();
        monsterGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterDistanceCulling = value,
                        opt -> ChlorideConfig.monsterDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        monsterGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterCullingDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.monsterCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        monsterGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding(
                        (opt, value) -> ChlorideConfig.monsterCullingDistanceY = value,
                        opt -> ChlorideConfig.monsterCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        final var tileGroup = OptionGroup.createBuilder();
        tileGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tiles.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.tileEntityDistanceCulling = value,
                        opt -> ChlorideConfig.tileEntityDistanceCulling)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        tileGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tile.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.tileEntityCullingDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.tileEntityCullingDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        tileGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tile.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding((opt, value) -> ChlorideConfig.tileEntityCullingDistanceY = value,
                        opt -> ChlorideConfig.tileEntityCullingDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        groups.add(nametags.build());
        groups.add(entityGroup.build());
        groups.add(monsterGroup.build());
        groups.add(tileGroup.build());

        return ImmutableList.copyOf(groups);
    }
}
