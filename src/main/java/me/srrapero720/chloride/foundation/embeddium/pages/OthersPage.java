package me.srrapero720.chloride.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.foundation.embeddium.EmbPlusOptions.STORAGE;

public class OthersPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "others")));
    public OthersPage() {
        super(ID, Component.translatable("chloride.options.others.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(ChlorideConfig.AttachMode.class, STORAGE)
                        .setName(Component.translatable("chloride.options.others.borderless.attachmode.title"))
                        .setTooltip(Component.translatable("chloride.options.others.borderless.attachmode.desc"))
                        .setControl(option -> new CyclingControl<>(option, ChlorideConfig.AttachMode.class, new Component[] {
                                Component.translatable("chloride.options.common.attach"),
                                Component.translatable("chloride.options.common.replace"),
                                Component.translatable("chloride.options.common.off")
                        }))
                        .setBinding((options, value) -> ChlorideConfig.borderlessAttachModeF11 = value,
                                (options) -> ChlorideConfig.borderlessAttachModeF11)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, STORAGE)
                        .setName(Component.translatable("chloride.options.others.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("chloride.options.others.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> ChlorideConfig.fastLanguageReload = value,
                                (options) -> ChlorideConfig.fastLanguageReload)
                        .build()
                )
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
