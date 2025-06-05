package me.srrapero720.chloride.impl;

import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.TriState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

@EventBusSubscriber(modid = Chloride.ID, value = Dist.CLIENT)
public class HideNametag {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderNameTagEvent(final RenderNameTagEvent.CanRender e) {
        final EntityRenderer<?, ?> renderer = e.getEntityRenderer();
        if (!ChlorideConfig.itemNametagRendering && renderer instanceof ItemFrameRenderer || renderer instanceof ItemEntityRenderer) {
            e.setCanRender(TriState.FALSE);
            return;
        }
        if (!ChlorideConfig.playerNametagRendering && renderer instanceof PlayerRenderer) {
            e.setCanRender(TriState.FALSE);
            return;
        }
        if (!ChlorideConfig.entityNametagRendering) {
            e.setCanRender(TriState.FALSE);
        }
    }
}
