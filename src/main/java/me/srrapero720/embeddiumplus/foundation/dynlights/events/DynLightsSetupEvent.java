package me.srrapero720.embeddiumplus.foundation.dynlights.events;

import me.srrapero720.embeddiumplus.foundation.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.foundation.dynlights.item.ItemLightRegistry;
import me.srrapero720.embeddiumplus.foundation.dynlights.item.ItemLightSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event fired when Emb++ DynLights is initialized to register custom dynamic light handlers and item light sources.
 *
 * @see DynLightsHandlers#registerDynamicLightHandler(EntityType, DynLightsHandlers.Entry)
 * @see DynLightsHandlers#registerDynamicLightHandler(BlockEntityType, DynLightsHandlers.Entry)
 * @see ItemLightRegistry#registerItemLightSource(ItemLightSource)
 */
public class DynLightsSetupEvent extends Event {
    public DynLightsSetupEvent() {}

    @Override
    public boolean hasResult() {
        return false;
    }
}
