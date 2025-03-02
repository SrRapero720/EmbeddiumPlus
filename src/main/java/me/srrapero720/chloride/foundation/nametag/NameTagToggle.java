package me.srrapero720.chloride.foundation.nametag;

import me.srrapero720.chloride.EmbeddiumPlus;
import me.srrapero720.chloride.EmbyConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class NameTagToggle {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(RenderNameTagEvent e) {
        if (EmbyConfig.disableNameTagRenderCache) e.setResult(Event.Result.DENY);
    }
}
