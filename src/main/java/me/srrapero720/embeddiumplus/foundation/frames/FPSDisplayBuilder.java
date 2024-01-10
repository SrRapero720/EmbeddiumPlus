package me.srrapero720.embeddiumplus.foundation.frames;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class FPSDisplayBuilder {
    private String builder = "";
    private boolean split = false;
    private boolean divisor = false;

    public FPSDisplayBuilder append(String param) {
        if (split) builder += " - ";
        if (divisor) builder += " | ";
        builder += param;

        split = false;
        divisor = true;
        return this;
    }

    public FPSDisplayBuilder append(Component component) {
        return append(component.getString());
    }

    public FPSDisplayBuilder append(ChatFormatting formatting) {
        return append(formatting.toString());
    }

    public FPSDisplayBuilder add(int param) {
        builder += param;
        return this;
    }

    public FPSDisplayBuilder add(String param) {
        builder += param;
        return this;
    }

    public FPSDisplayBuilder add(Component component) {
        return add(component.getString());
    }

    public FPSDisplayBuilder add(ChatFormatting formatting) {
        return add(formatting.toString());
    }

    public void split() {
        split = true;
        divisor = false;
    }

    public boolean isEmpty() {
        return builder.isEmpty();
    }

    public void release() {
        builder = "";
        split = false;
        divisor = false;
    }

    @Override
    public String toString() {
        return builder;
    }
}
