package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

@Mixin(BlockEntityType.class)
public abstract class TileEntityTypeMixin implements IWhitelistCheck {
    @Unique private static final Marker e$IT = MarkerManager.getMarker("TileEntityCullingCheck");
    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Override
    public boolean embPlus$isAllowed() {
        EmbyConfig.load();
        if (embPlus$checked) return embPlus$whitelisted;
        ResourceLocation currentLocation = getKey((BlockEntityType<?>) (Object) this); // CAN'T BE NULL, BECAUSE IT EXISTS

        for (String item : EmbyConfig.tileEntityWhitelist.get()) {
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

    @Shadow
    @Nullable
    public static ResourceLocation getKey(BlockEntityType<?> pBlockEntityType) {
        throw new UnsupportedOperationException("stub!");
    }
}
