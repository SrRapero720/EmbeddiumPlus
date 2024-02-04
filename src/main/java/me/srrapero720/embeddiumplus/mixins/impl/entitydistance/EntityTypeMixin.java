package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.entitydistance.EntityWhitelist;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

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
        if (embPlus$checked) return embPlus$whitelisted;

        this.embPlus$whitelisted = EntityWhitelist.isAllowed(getDefaultLootTable(), EmbyConfig.entityWhitelist);
        this.embPlus$checked = true;

        return embPlus$whitelisted;
    }
}
