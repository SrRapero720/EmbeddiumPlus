package vice.rubidium_extras.features.FrameCounter;

import net.minecraft.client.Minecraft;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.Mth;
import vice.rubidium_extras.mixins.FrameCounter.FpsAccessorMixin;

public class MinFrameProvider
{

    public static int getLastMinFrame() { return lastMinFrame; }

    private static int lastMinFrame = 0;

    public static void recalculate()
    {
        var client = Minecraft.getInstance();
        FrameTimer ft = client.getFrameTimer();

        int logStart = ft.getLogStart();
        int logEnd = ft.getLogEnd();

        if (logEnd == logStart)
            return;

        int fps = FpsAccessorMixin.getFPS();
        if (fps <= 0)
            fps = 1;

        long[] frames = ft.getLog();
        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        int index = Mth.positiveModulo(logEnd - 1, frames.length);
        while (index != logStart && (double) totalNS < 1000000000)
        {
            long timeNs = frames[index];
            if (timeNs > maxNS)
            {
                maxNS = timeNs;
            }

            totalNS += timeNs;
            index = Mth.positiveModulo(index - 1, frames.length);
        }

        lastMinFrame = (int) (1 / ((double) maxNS / 1000000000));
    }
}