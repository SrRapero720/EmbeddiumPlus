package me.srrapero720.embeddiumplus.foundation.frames;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class FPSDisplay {
    private String builder = "";
    private boolean split = false;
    private boolean divisor = false;

    public FPSDisplay append(String param) {
        if (split) builder += " - ";
        if (divisor) builder += " | ";
        builder += param;

        split = false;
        divisor = true;
        return this;
    }

    public FPSDisplay append(Component component) {
        return append(component.getString());
    }

    public FPSDisplay append(ChatFormatting formatting) {
        return append(formatting.toString());
    }

    public FPSDisplay add(int param) {
        builder += param;
        return this;
    }

    public FPSDisplay add(String param) {
        builder += param;
        return this;
    }

    public FPSDisplay add(Component component) {
        return add(component.getString());
    }

    public FPSDisplay add(ChatFormatting formatting) {
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
