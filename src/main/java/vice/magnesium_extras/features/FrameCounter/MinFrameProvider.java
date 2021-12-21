package vice.magnesium_extras.features.FrameCounter;

import lombok.Getter;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.math.MathHelper;
import vice.magnesium_extras.mixins.FrameCounter.FpsAccessorMixin;

public class MinFrameProvider
{
    @Getter
    private static int lastMinFrame = 0;

    public static void recalculate()
    {
        val client = Minecraft.getInstance();
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

        int index = MathHelper.positiveModulo(logEnd - 1, frames.length);
        while (index != logStart && (double) totalNS < 1000000000)
        {
            long timeNs = frames[index];
            if (timeNs > maxNS)
            {
                maxNS = timeNs;
            }

            totalNS += timeNs;
            index = MathHelper.positiveModulo(index - 1, frames.length);
        }

        lastMinFrame = (int) (1 / ((double) maxNS / 1000000000));
    }
}