package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbPlusConfig;
import me.srrapero720.embeddiumplus.features.entity_distance.IEntityTypeAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityType.class)
public abstract class EntityCullingMixin implements IEntityTypeAccess {
    @Shadow public abstract ResourceLocation getDefaultLootTable();

    @Unique private boolean embeddiumPlus$checked = false;
    @Unique private boolean embeddiumPlus$whitelisted = false;

    @Override
    @Unique
    public boolean embPlus$isWhitelisted() {
        if (!EmbPlusConfig.SPECS.isLoaded()) return false;
        if (embeddiumPlus$checked) return embeddiumPlus$whitelisted;

        List<?> entityWhitelist = EmbPlusConfig.entityWhitelist.get();
        for (int i = 0; i < entityWhitelist.size(); i++) {

            String[] result = ((String) entityWhitelist.get(i)).split(":");

            if (result[1].equals("*") && getDefaultLootTable().getNamespace().equals(result[0])) {
                embeddiumPlus$whitelisted = true;
                break;
            } else if (getDefaultLootTable().equals(new ResourceLocation(result[0], result[1]))) {
                embeddiumPlus$whitelisted = true;
                break;
            }
            embeddiumPlus$checked = true;
        }
        return embeddiumPlus$whitelisted;
    }
}
