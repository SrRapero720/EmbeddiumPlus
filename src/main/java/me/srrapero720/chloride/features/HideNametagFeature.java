package me.srrapero720.chloride.features;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HideNametagFeature {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(RenderNameTagEvent e) {
        if (ChlorideConfig.disableNameTagRender) e.setResult(Event.Result.DENY);
    }
}
