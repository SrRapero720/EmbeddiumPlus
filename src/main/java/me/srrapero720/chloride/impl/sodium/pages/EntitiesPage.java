package me.srrapero720.chloride.impl.sodium.pages;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.EntityCulling;
import net.caffeinemc.mods.sodium.api.config.option.ControlValueFormatter;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.*;

public class EntitiesPage {
    private EntitiesPage() {}

    public static OptionPageBuilder build(final ConfigBuilder b) {
        final OptionPageBuilder page = b.createOptionPage().setName(Component.translatable("chloride.entities"));
        final Component vsWarning = Component.translatable("chloride.entities.culling.vseureka.warning").withStyle(ChatFormatting.GOLD);
        final ControlValueFormatter entityLimit = n -> n >= 512
                ? Component.translatable("options.framerateLimit.max")
                : Component.literal(String.valueOf(n));

        if (!ChlorideConfig.modpackMode) {
            page.addOptionGroup(b.createOptionGroup()
                    .addOption(b.createBooleanOption(Chloride.id("entityNametagRendering"))
                            .setName(Component.translatable("chloride.entities.nametag.entities.title"))
                            .setTooltip(Component.translatable("chloride.entities.nametag.entities.desc"))
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.entityNametagRendering = v, () -> ChlorideConfig.entityNametagRendering))
                    .addOption(b.createBooleanOption(Chloride.id("playerNametagRendering"))
                            .setName(Component.translatable("chloride.entities.nametag.players.title"))
                            .setTooltip(Component.translatable("chloride.entities.nametag.players.desc"))
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.playerNametagRendering = v, () -> ChlorideConfig.playerNametagRendering))
                    .addOption(b.createBooleanOption(Chloride.id("itemNametagRendering"))
                            .setName(Component.translatable("chloride.entities.nametag.items.title"))
                            .setTooltip(Component.translatable("chloride.entities.nametag.items.desc"))
                            .setStorageHandler(STORAGE)
                            .setDefaultValue(true)
                            .setBinding(v -> ChlorideConfig.itemNametagRendering = v, () -> ChlorideConfig.itemNametagRendering))
            );
        }

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("entityDistanceCulling"))
                        .setName(Component.translatable("chloride.entities.culling.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.entityDistanceCulling = v, () -> ChlorideConfig.entityDistanceCulling))
                .addOption(b.createIntegerOption(Chloride.id("entityLimit"))
                        .setName(Component.translatable("chloride.entities.culling.limit.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.limit.desc"))
                        .setValueFormatter(entityLimit)
                        .setRange(0, 512, 1)
                        .setStorageHandler(STORAGE)
                        .setDefaultValue(512)
                        .setBinding(v -> ChlorideConfig.entityLimit = v, () -> ChlorideConfig.entityLimit))
                .addOption(b.createIntegerOption(Chloride.id("entityCullingDistanceX"))
                        .setName(Component.translatable("chloride.entities.culling.distance.horizontal.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.distance.horizontal.desc"))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 128, 8)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(64)
                        .setBinding(v -> ChlorideConfig.entityCullingDistanceX = v * v,
                                () -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.entityCullingDistanceX)))))
                .addOption(b.createIntegerOption(Chloride.id("entityCullingDistanceY"))
                        .setName(Component.translatable("chloride.entities.culling.distance.vertical.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.distance.vertical.desc")
                                .append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 256, 4)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setEnabled(!EntityCulling.VS_I)
                        .setDefaultValue(32)
                        .setBinding(v -> ChlorideConfig.entityCullingDistanceY = v, () -> ChlorideConfig.entityCullingDistanceY))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("monsterDistanceCulling"))
                        .setName(Component.translatable("chloride.entities.culling.monster.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.monster.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(false)
                        .setBinding(v -> ChlorideConfig.monsterDistanceCulling = v, () -> ChlorideConfig.monsterDistanceCulling))
                .addOption(b.createIntegerOption(Chloride.id("monsterCullingDistanceX"))
                        .setName(Component.translatable("chloride.entities.culling.monster.distance.horizontal.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.horizontal.desc"))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 128, 8)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(128)
                        .setBinding(v -> ChlorideConfig.monsterCullingDistanceX = v * v,
                                () -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.monsterCullingDistanceX)))))
                .addOption(b.createIntegerOption(Chloride.id("monsterCullingDistanceY"))
                        .setName(Component.translatable("chloride.entities.culling.monster.distance.vertical.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.monster.distance.vertical.desc")
                                .append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 256, 4)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setEnabled(!EntityCulling.VS_I)
                        .setDefaultValue(64)
                        .setBinding(v -> ChlorideConfig.monsterCullingDistanceY = v, () -> ChlorideConfig.monsterCullingDistanceY))
        );

        page.addOptionGroup(b.createOptionGroup()
                .addOption(b.createBooleanOption(Chloride.id("tileEntityDistanceCulling"))
                        .setName(Component.translatable("chloride.entities.culling.tiles.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.tiles.desc"))
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(true)
                        .setBinding(v -> ChlorideConfig.tileEntityDistanceCulling = v, () -> ChlorideConfig.tileEntityDistanceCulling))
                .addOption(b.createIntegerOption(Chloride.id("tileEntityCullingDistanceX"))
                        .setName(Component.translatable("chloride.entities.culling.tile.distance.horizontal.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.horizontal.desc"))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 128, 8)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setDefaultValue(64)
                        .setBinding(v -> ChlorideConfig.tileEntityCullingDistanceX = v * v,
                                () -> Math.toIntExact(Math.round(Math.sqrt(ChlorideConfig.tileEntityCullingDistanceX)))))
                .addOption(b.createIntegerOption(Chloride.id("tileEntityCullingDistanceY"))
                        .setName(Component.translatable("chloride.entities.culling.tile.distance.vertical.title"))
                        .setTooltip(Component.translatable("chloride.entities.culling.tile.distance.vertical.desc")
                                .append(EntityCulling.VS_I ? Component.literal("\n\n").append(vsWarning) : Component.empty()))
                        .setValueFormatter(BLOCKS)
                        .setRange(0, 256, 4)
                        .setStorageHandler(STORAGE)
                        .setImpact(OptionImpact.HIGH)
                        .setEnabled(!EntityCulling.VS_I)
                        .setDefaultValue(32)
                        .setBinding(v -> ChlorideConfig.tileEntityCullingDistanceY = v, () -> ChlorideConfig.tileEntityCullingDistanceY))
        );

        return page;
    }
}
