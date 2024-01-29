package me.srrapero720.embeddiumplus;

import me.srrapero720.embeddiumplus.foundation.keybinding.KeyboardInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;

public enum
WiZoom
{
    INSTANCE;

    public static final Minecraft MC = Minecraft.getInstance();

    private final double defaultLevel = 3;
    private Double currentLevel;
    private Double defaultMouseSensitivity;

    public double changeFovBasedOnZoom(double fov)
    {
        OptionInstance<Double> mouseSensitivitySetting = MC.options.sensitivity();

        if(currentLevel == null)
            currentLevel = defaultLevel;

        if(!KeyboardInput.zoomKey.isDown())
        {
            currentLevel = defaultLevel;

            if(defaultMouseSensitivity != null)
            {
                mouseSensitivitySetting.set(defaultMouseSensitivity);
                defaultMouseSensitivity = null;
            }

            return fov;
        }

        if(defaultMouseSensitivity == null)
            defaultMouseSensitivity = mouseSensitivitySetting.get();

        // Adjust mouse sensitivity in relation to zoom level.
        // 1.0 / currentLevel is a value between 0.02 (50x zoom)
        // and 1 (no zoom).
        mouseSensitivitySetting
                .set(defaultMouseSensitivity * (1.0 / currentLevel));

        return fov / currentLevel;
    }

    public void onMouseScroll(double amount)
    {
        if(!KeyboardInput.zoomKey.isDown())
            return;

        if(currentLevel == null)
            currentLevel = defaultLevel;

        if(amount > 0)
            currentLevel *= 1.1;
        else if(amount < 0)
            currentLevel *= 0.9;

        currentLevel = Mth.clamp(currentLevel, 1, 50);
    }
}