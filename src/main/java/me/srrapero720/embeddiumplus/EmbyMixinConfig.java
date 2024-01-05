package me.srrapero720.embeddiumplus;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class EmbyMixinConfig {
    public static final ForgeConfigSpec SPECS;

    public static final ForgeConfigSpec.BooleanValue mixin$Borderless$F11;
    public static final ForgeConfigSpec.BooleanValue mixin$LanguageScreen$fastreload;

    static {
        var BUILDER = new ForgeConfigSpec.Builder();

        mixin$Borderless$F11 = BUILDER.define("mixin.borderless.f11", true);
        mixin$LanguageScreen$fastreload = BUILDER.define("mixin.languageScreen.fastreload", true);

        SPECS = BUILDER.build();
    }

    public static boolean isLoaded() {
        return SPECS.isLoaded();
    }

    public static void load() {
        if (isLoaded()) return;

        EmbeddiumPlus.LOGGER.warn("Force-loading Embeddium++Mixin config");

        // FORCE LOAD
        var path = FMLPaths.CONFIGDIR.get().resolve("embeddium++mixin.toml");
        try {
            final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

            configData.load();
            SPECS.setConfig(configData);
        } catch (Exception e) {
            var file = path.toFile();
            if (!file.exists()) throw new RuntimeException("Failed to read configuration file");
            if (!file.delete()) throw new RuntimeException("Failed to remove corrupted configuration file");
            load();
        }
    }
}
