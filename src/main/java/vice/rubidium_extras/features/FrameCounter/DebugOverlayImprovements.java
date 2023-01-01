package vice.rubidium_extras.features.FrameCounter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "rubidium_extras", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DebugOverlayImprovements
{
    @SubscribeEvent
    public static void onRenderDebugText(RenderGuiOverlayEvent.Pre event) {
        if (!event.getOverlay().id().getPath().equals("debug_text"))
            return;

        // cancel rendering text if chart is displaying
        var minecraft = Minecraft.getInstance();
        if (minecraft.options.renderFpsChart)
            event.setCanceled(true);

    }
}
