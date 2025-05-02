package me.srrapero720.chloride.api;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.features.FastBlocksFeature;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class is used to handle the fast models resource pack.
 */
@ApiStatus.Experimental
public sealed abstract class FastModelSettingsUpdate extends Event {
    /**
     * Event to enable or disable the fast models resource pack.
     * This event is fired when the setting is updated. by default chloride takes the highest priority.
     */
    public static final class ChestEvent extends FastModelSettingsUpdate {
        @Override
        public boolean isEnabled() {
            return FastBlocksFeature.canUseOnChests() && ChlorideConfig.fastChests;
        }
    }

    /**
     * Event to enable or disable the fast models resource pack.
     * This event is fired when the setting is updated. by default chloride takes the highest priority.
     */
    public static final class BedEvent extends FastModelSettingsUpdate {
        public BedEvent() {
        }

        @Override
        public boolean isEnabled() {
            return ChlorideConfig.fastBeds;
        }
    }

    /**
     * Event to enable or disable the fast models resource pack.
     * This event is fired when the setting is updated. by default chloride takes the highest priority.
     */
    public abstract boolean isEnabled();
}
