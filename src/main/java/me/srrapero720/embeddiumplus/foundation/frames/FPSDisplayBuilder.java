package me.srrapero720.embeddiumplus.foundation.frames;

public class FPSDisplayBuilder {
    private String[] builder = new String[] { "", "", "" };
    private int index = 0;

    public FPSDisplayBuilder append(String param) {
        return append(param, "");
    }

    public FPSDisplayBuilder append(String param, String suffix) {
        if (!builder[index].isEmpty()) builder[index] += " | ";
        builder[index] += param + ((!suffix.isEmpty()) ? " " + suffix : "");
        return this;
    }

    public FPSDisplayBuilder append(Number param) {
        return append(param, "");
    }

    public FPSDisplayBuilder append(Number param, String suffix) {
        return append(String.valueOf(param), suffix);
    }

    public FPSDisplayBuilder append(Number param, Number suffix) {
        return append(String.valueOf(param), String.valueOf(suffix));
    }

    public FPSDisplayBuilder append(String param, Number suffix) {
        return append(param, String.valueOf(suffix));
    }

    public boolean isEmpty() {
        return builder[index].isEmpty();
    }

    public FPSDisplayBuilder split() {
        index++;
        if (index >= builder.length) throw new ArrayIndexOutOfBoundsException("Exceeded number of allowed splits");
        return this;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < index + 1; i++) {
            if (i > 0) result += " - ";
            result += builder[i];
        }
        return result;
    }

    public void release() {
        builder = null;
        index = -1;
    }
}
