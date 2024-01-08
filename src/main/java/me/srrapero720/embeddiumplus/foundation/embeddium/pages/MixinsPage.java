package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbyMixinConfig;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class MixinsPage extends OptionPage {
    private static final SodiumOptionsStorage mixinsOptionsStorage = new SodiumOptionsStorage();

    public MixinsPage() {
        super(Component.literal("Mixins"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
                        .setName(Component.translatable("embeddium.plus.mixins.borderless.f11.title"))
                        .setTooltip(Component.translatable("embeddium.plus.mixins.borderless.f11.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> EmbyMixinConfig.mixin$Borderless$F11.set(value),
                                (options) -> EmbyMixinConfig.mixin$Borderless$F11.get())
                        .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
                        .setName(Component.translatable("embeddium.plus.mixins.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("embeddium.plus.mixins.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> EmbyMixinConfig.mixin$LanguageScreen$fastreload.set(value),
                                (options) -> EmbyMixinConfig.mixin$LanguageScreen$fastreload.get())
                        .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
                        .build()
                )
//                .add(OptionImpl.createBuilder(boolean.class, mixinsOptionsStorage)
//                        .setName(Component.translatable("embeddium.plus.options.common.experimental").withStyle(ChatFormatting.RED).append(" ").append(Component.translatable("embeddium.plus.mixins.dynlights.title")))
//                        .setTooltip(Component.translatable("embeddium.plus.mixins.dynlights.desc"))
//                        .setControl(TickBoxControl::new)
//                        .setBinding((options, value) -> EmbyMixinConfig.mixin$DynLights.set(value),
//                                (options) -> EmbyMixinConfig.mixin$DynLights.get())
//                        .setFlags(OptionFlag.REQUIRES_GAME_RESTART)
//                        .build()
//                )



                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
