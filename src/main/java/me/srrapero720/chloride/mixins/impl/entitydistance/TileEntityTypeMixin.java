package me.srrapero720.chloride.mixins.impl.entitydistance;

import me.srrapero720.chloride.EmbyConfig;
import me.srrapero720.chloride.EmbyTools;
import me.srrapero720.chloride.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static me.srrapero720.chloride.EmbeddiumPlus.LOGGER;

@Mixin(BlockEntityType.class)
public abstract class TileEntityTypeMixin implements IWhitelistCheck {
    @Unique private static final Marker e$IT = MarkerManager.getMarker("BlockEntityType");
    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Override
    public boolean embPlus$isWhitelisted() {
        if (embPlus$checked) return embPlus$whitelisted;
        ResourceLocation resource = BlockEntityType.getKey(embPlus$cast());
        if (resource == null) {
            LOGGER.warn(e$IT, "key for '{}' is null, some mod decides to broke itself, not whitelisting", this.getClass().getName());
            return false;
        }
        this.embPlus$whitelisted = EmbyTools.isWhitelisted(resource, EmbyConfig.tileEntityWhitelist);
        this.embPlus$checked = true;

        LOGGER.debug(e$IT,"Whitelist checked for {}", resource.toString());
        return embPlus$whitelisted;
    }

    @Unique
    private BlockEntityType<?> embPlus$cast() {
        return (BlockEntityType<?>) ((Object) this);
    }
}
