package me.srrapero720.chloride.features;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chloride.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HideNametagFeature {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(final RenderNameTagEvent e) {
        final EntityRenderer<?> renderer = e.getEntityRenderer();
        if (!ChlorideConfig.itemNametagRendering && renderer instanceof ItemFrameRenderer || renderer instanceof ItemEntityRenderer) {
            e.setResult(Event.Result.DENY);
            return;
        }
        if (!ChlorideConfig.playerNametagRendering && renderer instanceof PlayerRenderer) {
            e.setResult(Event.Result.DENY);
            return;
        }
        if (!ChlorideConfig.entityNametagRendering) {
            e.setResult(Event.Result.DENY);
        }
    }
}
