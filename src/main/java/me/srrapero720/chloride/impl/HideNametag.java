package me.srrapero720.chloride.impl;

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
public class HideNametag {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(final RenderNameTagEvent e) {
        final EntityRenderer<?> renderer = e.getEntityRenderer();
        if (!ChlorideConfig.nametags.items && (renderer instanceof ItemFrameRenderer || renderer instanceof ItemEntityRenderer)) {
            e.setResult(Event.Result.DENY);
            return;
        }
        if (!ChlorideConfig.nametags.players && renderer instanceof PlayerRenderer) {
            e.setResult(Event.Result.DENY);
            return;
        }
        if (!ChlorideConfig.nametags.entities) {
            e.setResult(Event.Result.DENY);
        }
    }
}
