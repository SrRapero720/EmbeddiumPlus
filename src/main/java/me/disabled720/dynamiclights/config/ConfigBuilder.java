package me.disabled720.dynamiclights.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.function.Consumer;

public class ConfigBuilder
{
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();


    public ConfigBuilder(String comment)
    {
        COMMON_BUILDER.comment(comment).push("Settings");
    }

    public ForgeConfigSpec Save()
    {
        COMMON_BUILDER.pop();
        return COMMON_BUILDER.build();
    }

    public void Block(String name, Consumer<ForgeConfigSpec.Builder> func)
    {
        COMMON_BUILDER.push(name);
        func.accept(COMMON_BUILDER);
        COMMON_BUILDER.pop();
    }
}