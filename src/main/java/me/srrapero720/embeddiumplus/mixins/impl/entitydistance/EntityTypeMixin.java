package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.List;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements IWhitelistCheck {
    @Unique private static final Marker e$IT = MarkerManager.getMarker("EntityCullingCheck");
    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Shadow public abstract ResourceLocation getDefaultLootTable();

    @Override
    @Unique
    public boolean embPlus$isAllowed() {
        EmbyConfig.load();
        if (embPlus$checked) return embPlus$whitelisted;
        ResourceLocation currentLocation = getDefaultLootTable();

        for (String item : EmbyConfig.entityWhitelist.get()) {
            String[] result = item.split(":");

            if (result[1].equals("*") && currentLocation.getNamespace().equals(result[0])) {
                embPlus$whitelisted = true;
                break;
            } else if (currentLocation.toString().equals(result[0] + ":" + result[1])) {
                embPlus$whitelisted = true;
                break;
            }
        }

        LOGGER.debug(e$IT,"Whitelist checked for {}", currentLocation);
        embPlus$checked = true;
        return embPlus$whitelisted;
    }
}
