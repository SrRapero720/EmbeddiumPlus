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
                    .setBinding((opt, v) -> ChlorideConfig.nametags.entities = v, opt -> ChlorideConfig.nametags.entities)
                    .build()
            );
            nametags.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setName(Component.translatable("chloride.entities.nametag.players.title"))
                    .setTooltip(Component.translatable("chloride.entities.nametag.players.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding((opt, v) -> ChlorideConfig.nametags.players = v, opt -> ChlorideConfig.nametags.players)
                    .build()
            );

            nametags.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                    .setName(Component.translatable("chloride.entities.nametag.items.title"))
                    .setTooltip(Component.translatable("chloride.entities.nametag.items.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding((opt, v) -> ChlorideConfig.nametags.items = v, opt -> ChlorideConfig.nametags.items)
                    .build()
            );
        }

        final var entityGroup = OptionGroup.createBuilder();
        final var vsWarning = Component.translatable("chloride.entities.culling.vseureka.warning").withStyle(ChatFormatting.GOLD);
        entityGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opt, value) -> ChlorideConfig.culling.entities = value, opt -> ChlorideConfig.culling.entities)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        entityGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.limit.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.limit.desc"))
                .setControl(option -> new SliderControl(option, 0, 512, 1,
                        (n) -> n >= 512 ? Component.translatable("options.framerateLimit.max") : Component.literal("" + n)))
                .setBinding((opt, value) -> ChlorideConfig.culling.entityLimit = value, opt -> ChlorideConfig.culling.entityLimit)
                .build()
        );

        entityGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.entityDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.culling.entityDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        entityGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.entityDistanceY = value,
                        opt -> ChlorideConfig.culling.entityDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        final var monsterGroup = OptionGroup.createBuilder();
        monsterGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.monsters = value,
                        opt -> ChlorideConfig.culling.monsters)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        monsterGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.monsterDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.culling.monsterDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        monsterGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.monster.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.monsterDistanceY = value,
                        opt -> ChlorideConfig.culling.monsterDistanceY)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        final var tileGroup = OptionGroup.createBuilder();
        tileGroup.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tiles.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (opt, value) -> ChlorideConfig.culling.tileEntities = value,
                        opt -> ChlorideConfig.culling.tileEntities)
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        tileGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tile.distance.horizontal.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.horizontal.desc"))
                .setControl(option -> new SliderControl(option, 0, 128, 8, ControlValueFormatter.biomeBlend()))
                .setBinding((opt, value) -> ChlorideConfig.culling.tileEntityDistanceX = value * value,
                        opt -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.culling.tileEntityDistanceX))))
                .setImpact(OptionImpact.HIGH)
                .build()
        );

        tileGroup.add(OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("chloride.entities.culling.tile.distance.vertical.title"))
                .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.vertical.desc").append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                .setControl(option -> new SliderControl(option, 0, 256, 4, ControlValueFormatter.biomeBlend()))
                .setEnabledPredicate(() -> !EntityCulling.VS_I)
                .setBinding((opt, value) -> ChlorideConfig.culling.tileEntityDistanceY = value,
                        opt -> ChlorideConfig.culling.tileEntityDistanceY)
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
