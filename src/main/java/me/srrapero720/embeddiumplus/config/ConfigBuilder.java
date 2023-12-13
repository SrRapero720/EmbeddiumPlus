package me.srrapero720.embeddiumplus.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Consumer;

public class ConfigBuilder {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public ConfigBuilder(String name, String... comments) {
        for (int i = 0; i < comments.length; i++) COMMON_BUILDER.comment(comments[i]);
        COMMON_BUILDER.push(name);
    }

    public ForgeConfigSpec save() {
        COMMON_BUILDER.pop();
        return COMMON_BUILDER.build();
    }

    public ConfigBuilder comment(String... comments) {
        for (int i = 0; i < comments.length; i++) COMMON_BUILDER.comment(comments[i]);
        return this;
    }

    public void block(String name, Consumer<ForgeConfigSpec.Builder> func) {
        COMMON_BUILDER.push(name);
        func.accept(COMMON_BUILDER);
        COMMON_BUILDER.pop();
    }
}